package kr.co.finote.backend.src.article.service;

import java.time.LocalDate;
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
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
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

    public UserArticlesResponse getDragRelatedArticle(
            int page, int size, dragArticleRequest request) {
        SearchHits<ArticleDocument> byTitle = search(page, size, request.getDragText());

        List<SearchHit<ArticleDocument>> searchHits =
                byTitle.getSearchHits().stream()
                        .sorted(
                                (o1, o2) -> {
                                    if (o1.getScore() != o2.getScore()) {
                                        return Float.compare(o2.getScore(), o1.getScore());
                                    }
                                    LocalDate date1 = LocalDate.parse(o1.getContent().getCreatedDate());
                                    LocalDate date2 = LocalDate.parse(o2.getContent().getCreatedDate());

                                    return date2.compareTo(date1);
                                })
                        .collect(Collectors.toList());

        List<ArticleDocument> documents =
                searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());

        return UserArticlesResponse.fromDocuments(page, size, documents);
    }

    public SearchHits<ArticleDocument> search(int page, int size, String dragText) {
        int newPage = page - 1;
        Pageable pageable = PageRequest.of(newPage, size);

        QueryStringQueryBuilder builder = QueryBuilders.queryStringQuery(dragText);

        NativeSearchQuery searchQuery = new NativeSearchQuery(builder);
        searchQuery.setPageable(pageable);

        return restTemplate.search(searchQuery, ArticleDocument.class);
    }
}
