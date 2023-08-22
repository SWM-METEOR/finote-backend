package kr.co.finote.backend.src.article.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.NotFoundException;
import kr.co.finote.backend.src.article.document.ArticleDocument;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.domain.ArticleKeyword;
import kr.co.finote.backend.src.article.dto.KeywordScore;
import kr.co.finote.backend.src.article.dto.request.ArticleRequest;
import kr.co.finote.backend.src.article.dto.request.DragArticleRequest;
import kr.co.finote.backend.src.article.dto.response.ArticlePreviewListResponse;
import kr.co.finote.backend.src.article.dto.response.ArticlePreviewResponse;
import kr.co.finote.backend.src.article.dto.response.KeywordDataResponse;
import kr.co.finote.backend.src.article.dto.response.RelatedArticleResponse;
import kr.co.finote.backend.src.article.repository.ArticleEsRepository;
import kr.co.finote.backend.src.article.repository.ArticleRepository;
import kr.co.finote.backend.src.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleService {

    private static final int PREVIEW_TEXT_MAX_LENGTH = 3;

    // article 관련 레포지토리만 의존
    private final ArticleRepository articleRepository;
    private final ArticleEsRepository articleEsRepository;

    // 나머지는 다른 도메인 서비스에 의존
    private final KeywordService keywordService;
    private final ArticleKeywordService articleKeywordService;
    private final ElasticService elasticService;

    public void saveDocument(Long articleId, ArticleRequest articleRequest, User loginUser) {
        ArticleDocument document = ArticleDocument.createDocument(articleId, articleRequest, loginUser);
        articleEsRepository.save(document);
    }

    public Article findById(Long articleId) {
        return articleRepository
                .findByIdAndIsDeleted(articleId, false)
                .orElseThrow(() -> new NotFoundException(ResponseCode.ARTICLE_NOT_FOUND));
    }

    public ArticlePreviewListResponse getDragRelatedArticle(
            int page, int size, DragArticleRequest request) {
        SearchHits<ArticleDocument> byTitle = elasticService.search(page, size, request.getDragText());

        List<SearchHit<ArticleDocument>> searchHits =
                byTitle.getSearchHits().stream()
                        .sorted(elasticService.Scorecomparator())
                        .collect(Collectors.toList());

        List<ArticleDocument> documents =
                searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());

        List<ArticlePreviewResponse> articlePreviewResponseList = new ArrayList<>();
        for (ArticleDocument document : documents) {
            String previewBody = markdownToPreviewText(document.getBody());
            ArticlePreviewResponse articlePreviewResponse =
                    ArticlePreviewResponse.of(document, previewBody);
            articlePreviewResponseList.add(articlePreviewResponse);
        }

        return ArticlePreviewListResponse.of(page, size, articlePreviewResponseList);
    }

    @Transactional
    public Long save(ArticleRequest articleRequest, User loginUser) throws JsonProcessingException {
        Article article = Article.createArticle(articleRequest, loginUser);
        Article saveArticle = articleRepository.save(article); // 새로운 아티클 저장

        KeywordDataResponse[] keywordDataResponses =
                keywordService.extractKeywords(saveArticle.getBody()); // 키워드 추출
        if (keywordDataResponses != null) {
            List<KeywordScore> keywordScoreList =
                    keywordService.saveNewKeywords(keywordDataResponses); // 새로들어온 키워드 저장 및 키워드와 스코어 반환
            articleKeywordService.saveArticleKeywordList(
                    saveArticle, keywordScoreList); // 키워드와 아티클 연관관계 저장
        }
        return saveArticle.getId();
    }

    public List<RelatedArticleResponse> getRelatedArticle(Long articleId) {
        List<RelatedArticleResponse> relatedArticleList = new ArrayList<>();
        Article article =
                articleRepository
                        .findByIdAndIsDeleted(articleId, false)
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
                            .sorted(elasticService.Scorecomparator())
                            .collect(Collectors.toList());

            List<ArticleDocument> documents =
                    searchHits.stream().map(SearchHit::getContent).limit(10).collect(Collectors.toList());

            // document list -> response dto list 변환
            List<ArticlePreviewResponse> articlePreviewResponseList = new ArrayList<>();
            for (ArticleDocument document : documents) {
                String previewBody = markdownToPreviewText(document.getBody());
                ArticlePreviewResponse articlePreviewResponse =
                        ArticlePreviewResponse.of(document, previewBody);
                articlePreviewResponseList.add(articlePreviewResponse);
            }

            relatedArticleList.add(
                    RelatedArticleResponse.createRelatedArticleResponse(keyword, articlePreviewResponseList));
        }
        return relatedArticleList;
    }

    private String markdownToPreviewText(String originBody) {
        // markdown to html
        MutableDataSet options = new MutableDataSet();

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        Node document = parser.parse(originBody);
        String html = renderer.render(document);

        // html to text
        Document doc = Jsoup.parse(html);

        // preview text limit length 200
        if (doc.text().length() > PREVIEW_TEXT_MAX_LENGTH)
            return doc.text().substring(0, PREVIEW_TEXT_MAX_LENGTH);
        else return doc.text();
    }
}
