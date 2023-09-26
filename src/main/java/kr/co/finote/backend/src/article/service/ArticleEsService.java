package kr.co.finote.backend.src.article.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.ConnectException;
import kr.co.finote.backend.global.exception.NotFoundException;
import kr.co.finote.backend.src.article.document.ArticleDocument;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.dto.request.ArticleRequest;
import kr.co.finote.backend.src.article.repository.ArticleEsRepository;
import kr.co.finote.backend.src.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ArticleEsService {

    private final ElasticsearchRestTemplate elasticSearchrestTemplate;
    private final ArticleEsRepository articleEsRepository;

    private final int MAX_CALL_COUNT = 3;

    public void save(ArticleRequest articleRequest, User loginUser, Long articleId) {
        int callCount = 0;
        boolean isSaved = false;
        while (callCount < MAX_CALL_COUNT) {
            callCount += 1;
            try {
                articleEsRepository.save(
                        ArticleDocument.createDocument(articleId, articleRequest, loginUser));
                isSaved = true;
                break;
            } catch (Exception e) {
                log.info("Save Article Document : {}", callCount);
            }
        }
        if (!isSaved) throw new ConnectException(ResponseCode.ES_NOT_CONNECT);
    }

    public void editArticleByUser(User user) {
        List<ArticleDocument> articleDocumentList =
                articleEsRepository.findByAuthorNickname(user.getNickname());
        articleDocumentList.forEach(articleDocument -> articleDocument.editByUser(user));
        articleEsRepository.saveAll(articleDocumentList);
    }

    public void editDocument(Article article) {
        List<ArticleDocument> articleDocumentList =
                articleEsRepository.findByArticleId(article.getId()); // articleId를 가지는 document를 가져온다

        if (articleDocumentList.isEmpty()) {
            throw new NotFoundException(ResponseCode.ARTICLE_NOT_FOUND);
        }

        ArticleDocument articleDocument = articleDocumentList.get(0);
        articleDocument.editDocument(article);
        articleEsRepository.save(articleDocument);
    }

    public void editTotalLike(Long articleId, int totalLike) {
        List<ArticleDocument> articleDocumentList = articleEsRepository.findByArticleId(articleId);

        ArticleDocument articleDocument = articleDocumentList.get(0);
        articleDocument.editTotalLike(totalLike);
        articleEsRepository.save(articleDocument);
    }

    public void editTotalReply(Long articleId, int totalReply) {
        List<ArticleDocument> articleDocumentList = articleEsRepository.findByArticleId(articleId);

        ArticleDocument articleDocument = articleDocumentList.get(0);
        articleDocument.editTotalReply(totalReply);
        articleEsRepository.save(articleDocument);
    }

    public Comparator<SearchHit<ArticleDocument>> scorecomparator() {
        return (o1, o2) -> {
            if (o1.getScore() != o2.getScore()) {
                return Float.compare(o2.getScore(), o1.getScore());
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            LocalDate date1 = LocalDate.parse(o1.getContent().getCreatedDate(), formatter);
            LocalDate date2 = LocalDate.parse(o2.getContent().getCreatedDate(), formatter);
            return date2.compareTo(date1);
        };
    }

    // 페이징 있는 search
    public SearchHits<ArticleDocument> search(int page, int size, String searchText) {
        int newPage = page - 1;
        Pageable pageable = PageRequest.of(newPage, size);
        NativeSearchQuery searchQuery = getNativeSearchQuery(searchText);
        searchQuery.setPageable(pageable);

        return elasticSearchrestTemplate.search(searchQuery, ArticleDocument.class);
    }

    // 페이징 없는 search
    public SearchHits<ArticleDocument> search(String searchText) {
        NativeSearchQuery searchQuery = getNativeSearchQuery(searchText);
        return elasticSearchrestTemplate.search(searchQuery, ArticleDocument.class);
    }

    public NativeSearchQuery getNativeSearchQuery(String searchText) {
        QueryStringQueryBuilder builder = QueryBuilders.queryStringQuery(searchText);
        return new NativeSearchQuery(builder);
    }

    public void deleteArticle(Long articleId) {
        articleEsRepository.deleteByArticleId(articleId);
    }
}
