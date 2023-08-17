package kr.co.finote.backend.src.article.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.NotFoundException;
import kr.co.finote.backend.src.article.document.ArticleDocument;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.dto.request.ArticleRequest;
import kr.co.finote.backend.src.article.dto.request.dragArticleRequest;
import kr.co.finote.backend.src.article.repository.ArticleEsRepository;
import kr.co.finote.backend.src.article.repository.ArticleRepository;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.dto.response.UserArticlesResponse;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ElasticsearchRestTemplate restTemplate;
    private final ArticleEsRepository articleEsRepository;

    @Transactional
    public Long save(ArticleRequest articleRequest, User loginUser) {
        Article article = Article.createArticle(articleRequest, loginUser);
        return articleRepository.save(article).getId();
    }

    public void saveDocument(Long articleId, ArticleRequest articleRequest, User loginUser) {
        ArticleDocument document = ArticleDocument.createDocument(articleId, articleRequest, loginUser);
        articleEsRepository.save(document);
    }

    public Article findById(Long articleId) {
        return articleRepository
                .findByIdAndIsDeleted(articleId, false)
                .orElseThrow(() -> new NotFoundException(ResponseCode.ARTICLE_NOT_FOUND));
    }

    public UserArticlesResponse getDragRelatedArticle(dragArticleRequest request) {
        SearchHits<ArticleDocument> byTitle = searchByBody(request.getDragText());

        List<SearchHit<ArticleDocument>> searchHits =
                byTitle.getSearchHits().stream()
                        .sorted(
                                new Comparator<>() {
                                    @Override
                                    public int compare(SearchHit<ArticleDocument> o1, SearchHit<ArticleDocument> o2) {
                                        if (o1.getScore() != o2.getScore()) {
                                            return Float.compare(o2.getScore(), o1.getScore());
                                        }
                                        Article article1 =
                                                articleRepository
                                                        .findByIdAndIsDeleted(o1.getContent().getArticleId(), false)
                                                        .get();
                                        Article article2 =
                                                articleRepository
                                                        .findByIdAndIsDeleted(o2.getContent().getArticleId(), false)
                                                        .get();
                                        return article2.getCreatedDate().compareTo(article1.getCreatedDate());
                                    }
                                })
                        .collect(Collectors.toList());

        List<ArticleDocument> documents =
                searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());

        return UserArticlesResponse.of(documents);
    }

    public SearchHits<ArticleDocument> searchByTitle(String title) {
        return restTemplate.search(
                (Query) QueryBuilders.matchQuery("title", title), ArticleDocument.class);
    }

    public SearchHits<ArticleDocument> searchByBody(String body) {
        return restTemplate.search(
                (Query) QueryBuilders.matchQuery("body", body), ArticleDocument.class);
    }
}
