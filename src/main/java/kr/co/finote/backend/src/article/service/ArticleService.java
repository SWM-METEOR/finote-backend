package kr.co.finote.backend.src.article.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
import kr.co.finote.backend.src.article.dto.response.*;
import kr.co.finote.backend.src.article.repository.ArticleEsRepository;
import kr.co.finote.backend.src.article.repository.ArticleLikeRepository;
import kr.co.finote.backend.src.article.repository.ArticleRepository;
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
    private final ArticleEsRepository articleEsRepository;
    private final ArticleLikeRepository articleLikeRepository;

    // 나머지는 다른 도메인 서비스에 의존
    private final KeywordService keywordService;
    private final ArticleKeywordService articleKeywordService;
    private final ElasticService elasticService;
    private final UserService userService;
    private final CacheService cacheService;

    @Transactional
    public PostArticleResponse save(ArticleRequest articleRequest, User loginUser)
            throws JsonProcessingException {
        isDuplicateTitle(articleRequest, loginUser); // 동일 title에 대한 중복 체크

        Article article = Article.createArticle(articleRequest, loginUser);
        Article saveArticle = articleRepository.save(article); // 새로운 아티클 RDB 저장
        ArticleDocument document =
                ArticleDocument.createDocument(saveArticle.getId(), articleRequest, loginUser);
        articleEsRepository.save(document); // 새로운 아티클 ES 저장

        keywordService.extractAndSaveKeywords(saveArticle); // 키워드 추출 및 저장
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
    public ArticleResponse findById(User likedUser, Long articleId) {
        Article article =
                articleRepository
                        .findByIdAndIsDeleted(articleId, false)
                        .orElseThrow(() -> new NotFoundException(ResponseCode.ARTICLE_NOT_FOUND));
        boolean isLiked = false;
        if (likedUser != null) {
            ArticleLikeCache articleLikeCache = cacheService.findLikelog(likedUser, article);
            isLiked = articleLikeCache != null && !articleLikeCache.getIsDeleted();
        }

        return ArticleResponse.of(article, isLiked);
    }

    @Transactional
    public ArticleResponse findByNicknameAndTitle(User likedUser, String nickname, String title) {
        User findUser = userService.findByNickname(nickname); // 유저가 존재하는지 확인
        Article article =
                articleRepository
                        .findByUserAndTitleAndIsDeleted(findUser, title, false)
                        .orElseThrow(() -> new NotFoundException(ResponseCode.ARTICLE_NOT_FOUND));
        boolean isLiked = false;
        if (likedUser != null) {
            ArticleLikeCache articleLikeCache = cacheService.findLikelog(likedUser, article);
            isLiked = articleLikeCache != null && !articleLikeCache.getIsDeleted();
        }

        return ArticleResponse.of(article, isLiked);
    }

    public ArticlePreviewListResponse getDragRelatedArticle(
            int page, int size, DragArticleRequest request) {
        SearchHits<ArticleDocument> byTitle = elasticService.search(page, size, request.getDragText());

        List<SearchHit<ArticleDocument>> searchHits =
                byTitle.getSearchHits().stream()
                        .sorted(elasticService.scorecomparator())
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
            SearchHits<ArticleDocument> byKeyword = elasticService.search(keyword);
            List<SearchHit<ArticleDocument>> searchHits =
                    byKeyword.getSearchHits().stream()
                            .sorted(elasticService.scorecomparator())
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

    @CacheEvict(key = "#user.id + '-' + #articleId", value = "ArticleLikeLog")
    @Transactional
    public LikeResponse postLike(User user, Long articleId) {
        Article article =
                articleRepository
                        .findByIdAndIsDeleted(articleId, false)
                        .orElseThrow(() -> new NotFoundException(ResponseCode.ARTICLE_NOT_FOUND));

        ArticleLikeCache articleLikeCache = cacheService.findLikelog(user, article);

        Optional<ArticleLike> byUserAndArticle =
                articleLikeRepository.findByUserAndArticle(user, article);

        if (articleLikeCache != null) {
            if (articleLikeCache.getIsDeleted()) {
                article.updateLikeCount(1);
                byUserAndArticle.get().updateIsDeleted(false);
            } else {
                article.updateLikeCount(-1);
                byUserAndArticle.get().updateIsDeleted(true);
            }
            return LikeResponse.of(article);
        }

        article.updateLikeCount(1);
        articleLikeRepository.save(ArticleLike.createArticleLike(user, article));

        return LikeResponse.of(article);
    }

    @Transactional
    public PostArticleResponse editArticle(User loginUser, Long articleId, ArticleRequest request) {
        Article article =
                articleRepository
                        .findByIdAndIsDeleted(articleId, false)
                        .orElseThrow(() -> new NotFoundException(ResponseCode.ARTICLE_NOT_FOUND));

        if (!article.getUser().getEmail().equals((loginUser.getEmail()))) {
            throw new InvalidInputException(ResponseCode.ARTICLE_NOT_WRITER);
        }

        article.editArticle(request);
        return PostArticleResponse.of(article);
    }

    @Transactional
    public void deleteArticle(User loginUser, Long articleId) {
        Article article =
                articleRepository
                        .findByIdAndIsDeleted(articleId, false)
                        .orElseThrow(() -> new NotFoundException(ResponseCode.ARTICLE_NOT_FOUND));

        if (!article.getUser().getEmail().equals((loginUser.getEmail()))) {
            throw new InvalidInputException(ResponseCode.ARTICLE_NOT_WRITER);
        }

        article.deleteArticle();

        // 좋아요 내역 완전 삭제
        articleLikeRepository.deleteAllByArticle(article);
        // 키워드 내역 soft delete
        articleKeywordService.deleteArticleKeyword(article);
    }
}
