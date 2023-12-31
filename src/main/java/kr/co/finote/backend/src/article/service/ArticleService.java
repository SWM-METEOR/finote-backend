package kr.co.finote.backend.src.article.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.InvalidInputException;
import kr.co.finote.backend.global.exception.NotFoundException;
import kr.co.finote.backend.global.utils.StringUtils;
import kr.co.finote.backend.src.article.document.ArticleDocument;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.domain.ArticleKeyword;
import kr.co.finote.backend.src.article.domain.ArticleLike;
import kr.co.finote.backend.src.article.dto.cache.ArticleLikeCache;
import kr.co.finote.backend.src.article.dto.request.ArticleRequest;
import kr.co.finote.backend.src.article.dto.request.DragArticleRequest;
import kr.co.finote.backend.src.article.dto.request.FeedRequest;
import kr.co.finote.backend.src.article.dto.response.*;
import kr.co.finote.backend.src.article.repository.ArticleRepository;
import kr.co.finote.backend.src.common.service.FeedService;
import kr.co.finote.backend.src.user.domain.Category;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleService {

    private static final int RELATED_ARTICLE_MAX_COUNT = 6;

    // article 관련 레포지토리만 의존
    private final ArticleRepository articleRepository;

    // 나머지는 다른 도메인 서비스에 의존
    private final ArticleKeywordService articleKeywordService;
    private final ArticleEsService articleEsService;
    private final UserService userService;
    private final ArticleLikeCacheService articleLikeCacheService;
    private final ArticleLikeService articleLikeService;
    private final ArticleViewCacheService articleViewCacheService;
    private final KeywordService keywordService;
    private final FeedService feedService;

    @Transactional
    public PostArticleResponse save(ArticleRequest articleRequest, User loginUser)
            throws JsonProcessingException {

        // RDB 저장
        isDuplicateTitle(articleRequest, loginUser);
        Article saveArticle = articleRepository.save(Article.createArticle(articleRequest, loginUser));

        // 키워드 추출 및 저장
        keywordService.extractAndSaveKeywords(saveArticle);

        // ES 저장로직은 별도 ES 서비스로 분리
        articleEsService.save(articleRequest, loginUser, saveArticle.getId());

        // Message Queue에 데이터 추가
        feedService.publish(FeedRequest.createFeedRequest(loginUser.getId(), saveArticle.getId()));

        return PostArticleResponse.of(saveArticle);
    }

    private void isDuplicateTitle(ArticleRequest articleRequest, User loginUser) {
        articleRepository
                .findByUserAndTitleAndIsDeleted(loginUser, articleRequest.getTitle(), false)
                .ifPresent(
                        article -> {
                            throw new InvalidInputException(ResponseCode.ARTICLE_ALREADY_EXIST);
                        });
    }

    @Transactional
    public ArticleResponse lookupById(Long articleId) {
        Article article = findById(articleId);

        return ArticleResponse.of(article);
    }

    public Article findById(Long articleId) {
        return articleRepository
                .findByIdAndIsDeleted(articleId, false)
                .orElseThrow(() -> new NotFoundException(ResponseCode.ARTICLE_NOT_FOUND));
    }

    @Transactional
    public ArticleResponse lookupByNicknameAndTitle(
            String nickname, String title, HttpServletRequest request) {
        Article article = findByNicknameAndTitle(nickname, title);
        updateViewOrNot(request, article);
        return ArticleResponse.of(article);
    }

    public void updateViewOrNot(HttpServletRequest request, Article article) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR"); // 로드밸런서를 통해 들들어올 경우 IP 변경
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        } else {
            ipAddress = ipAddress.split(",")[0];
        }
        log.info("ipAddress : {}", ipAddress);
        String key = ipAddress + "-" + article.getId();
        boolean hasViewCache = articleViewCacheService.hasViewCache(key);
        if (!hasViewCache) {
            // 만약 24시간 내 조회 이력이 없을 경우
            article.editTotalView(); // 조회수 업데이트
            articleViewCacheService.cacheView(key); // 조회 내역 캐싱
        }
    }

    public ArticlePreviewListResponse getDragRelatedArticle(
            int page, int size, DragArticleRequest request) {
        SearchHits<ArticleDocument> byTitle =
                articleEsService.search(page, size, request.getDragText());

        List<SearchHit<ArticleDocument>> searchHits =
                byTitle.getSearchHits().stream()
                        .sorted(articleEsService.scorecomparator())
                        .collect(Collectors.toList());

        List<ArticleDocument> documents =
                searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());

        List<ArticlePreviewResponse> articlePreviewResponseList = ToArticlesPreviewResponses(documents);

        return ArticlePreviewListResponse.of(page, size, articlePreviewResponseList);
    }

    public List<RelatedArticleResponse> getRelatedArticle(String nickname, String title) {
        User user = userService.findByNickname(nickname);
        List<RelatedArticleResponse> relatedArticleList = new ArrayList<>();
        Article article =
                articleRepository
                        .findByUserAndTitleAndIsDeleted(user, title, false)
                        .orElseThrow(() -> new NotFoundException(ResponseCode.ARTICLE_NOT_FOUND));

        // article이 가지고 있는 상위 3개의 Keyword
        List<ArticleKeyword> top3ArticleKeywordList =
                articleKeywordService.findTop3ArticleKeywordList(article);

        for (ArticleKeyword articleKeyword : top3ArticleKeywordList) {
            String keyword = articleKeyword.getKeyword().getValue();

            // 각각의 키워드에 대해 es 검색
            SearchHits<ArticleDocument> byKeyword = articleEsService.search(keyword);
            List<SearchHit<ArticleDocument>> searchHits =
                    byKeyword.getSearchHits().stream()
                            .sorted(articleEsService.scorecomparator())
                            .collect(Collectors.toList());

            List<ArticleDocument> documents =
                    searchHits.stream()
                            .map(SearchHit::getContent)
                            .limit(RELATED_ARTICLE_MAX_COUNT)
                            .collect(Collectors.toList());

            // document list -> response dto list 변환
            List<ArticlePreviewResponse> articlePreviewResponseList =
                    ToArticlesPreviewResponses(documents);

            relatedArticleList.add(
                    RelatedArticleResponse.createRelatedArticleResponse(keyword, articlePreviewResponseList));
        }
        return relatedArticleList;
    }

    public ArticlePreviewListResponse trendArticles(int page, int size) {
        int pageNum = page - 1;
        // TODO : 이후 좋아요 순, 조회순 등으로 정렬 기준 구현하여 제공하기
        Pageable pageable = PageRequest.of(pageNum, size, Sort.by("createdDate").descending());

        Page<Article> result = articleRepository.findAllByIsDeleted(false, pageable);
        List<Article> contents = result.getContent();

        List<ArticlePreviewResponse> articlePreviewResponseList = ToArticlesPreviewResponses(contents);

        return ArticlePreviewListResponse.of(page, size, articlePreviewResponseList);
    }

    public ArticlePreviewListResponse articlesAll(String nickname, int page, int size) {
        int pageNum = page - 1;
        PageRequest pageable = PageRequest.of(pageNum, size, Sort.by("createdDate").descending());

        User findUser = userService.findByNickname(nickname);
        Page<Article> result = articleRepository.findByUserAndIsDeleted(findUser, false, pageable);

        List<Article> contents = result.getContent();
        List<ArticlePreviewResponse> articlePreviewResponseList = ToArticlesPreviewResponses(contents);

        return ArticlePreviewListResponse.of(page, size, articlePreviewResponseList);
    }

    private List<ArticlePreviewResponse> ToArticlesPreviewResponses(List<?> list) {

        List<ArticlePreviewResponse> articlePreviewResponseList = new ArrayList<>();
        for (Object object : list) {
            if (object instanceof Article) {
                Article article = (Article) object;
                String previewBody = StringUtils.markdownToPreviewText(article.getBody());
                articlePreviewResponseList.add(ArticlePreviewResponse.of(article, previewBody));
            } else if (object instanceof ArticleDocument) {
                ArticleDocument document = (ArticleDocument) object;
                String previewBody = StringUtils.markdownToPreviewText(document.getBody());
                articlePreviewResponseList.add(ArticlePreviewResponse.of(document, previewBody));
            }
        }

        return articlePreviewResponseList;
    }

    @CacheEvict(key = "#user.id + '-' + #article.id", value = "ArticleLikeLog")
    @Transactional
    public LikeResponse postLikeByNicknameAndTitle(User user, Article article) {
        ArticleLikeCache articleLikeCache = articleLikeCacheService.findLikeLog(user, article);

        Optional<ArticleLike> byUserAndArticle = articleLikeService.findByUser(user, article);

        if (articleLikeCache != null) {
            if (articleLikeCache.getIsDeleted()) {
                article.editTotalLike(1);
                byUserAndArticle.get().updateIsDeleted(false);
            } else {
                article.editTotalLike(-1);
                byUserAndArticle.get().updateIsDeleted(true);
            }
            return LikeResponse.of(article);
        }

        article.editTotalLike(1);
        articleLikeService.save(user, article);
        articleEsService.editTotalLike(article.getId(), article.getTotalLike());

        return LikeResponse.of(article);
    }

    @Transactional
    public PostArticleResponse editArticle(User loginUser, Long articleId, ArticleRequest request) {
        Article article = findById(articleId);
        checkArticleAuthority(loginUser, article);
        // ES 수정
        articleEsService.editDocument(article);
        article.editArticle(request);
        return PostArticleResponse.of(article);
    }

    private static void checkArticleAuthority(User loginUser, Article article) {
        if (!article.getUser().getEmail().equals((loginUser.getEmail()))) {
            throw new InvalidInputException(ResponseCode.ARTICLE_NOT_WRITER);
        }
    }

    @Transactional
    public void deleteArticle(User loginUser, Long articleId) {
        Article article = findById(articleId);
        checkArticleAuthority(loginUser, article);
        // ES 삭제
        article.deleteArticle();
        articleEsService.deleteArticle(article.getId());
        // 좋아요 내역 완전 삭제
        articleLikeService.deleteAllByArticle(article);
        // 키워드 내역 soft delete
        articleKeywordService.deleteArticleKeyword(article);
    }

    public Article findByNicknameAndTitle(String nickname, String title) {
        User findUser = userService.findByNickname(nickname); // 유저가 존재하는지 확인
        return articleRepository
                .findByUserAndTitleAndIsDeleted(findUser, title, false)
                .orElseThrow(() -> new NotFoundException(ResponseCode.ARTICLE_NOT_FOUND));
    }

    public ArticleLikeCheckResponse checkLike(User reader, String authorNickname, String title) {
        if (reader == null) {
            return ArticleLikeCheckResponse.createArticleLikeCheckResponse(false);
        }

        Article article = findByNicknameAndTitle(authorNickname, title);
        ArticleLikeCache likeLog = articleLikeCacheService.findLikeLog(reader, article);

        boolean isLiked = likeLog != null && !likeLog.getIsDeleted();

        return ArticleLikeCheckResponse.createArticleLikeCheckResponse(isLiked);
    }

    public ArticleTotalLikeResponse totalLike(String authorNickname, String title) {
        Article article = findByNicknameAndTitle(authorNickname, title);
        return ArticleTotalLikeResponse.createArticleTotalLikeResponse(article.getTotalLike());
    }

    public void editTotalReply(Article article, int num) {
        article.editTotalReply(num);
    }

    public void updateEs() {
        List<Article> articleList = articleRepository.findAllByIsDeleted(false);
        for (Article article : articleList) {
            articleEsService.addArticle(article); // article ES에 추가
        }
    }

    public boolean existsByCategory(Category category) {
        return articleRepository.existsByCategoryAndIsDeleted(category, false);
    }

    public Long countByCategoryAndUser(Category category, User loginUser) {
        return articleRepository.countAllByCategoryAndUserAndIsDeleted(category, loginUser, false);
    }
}
