package kr.co.finote.backend.src.article.repository;

import java.util.Optional;
import kr.co.finote.backend.src.article.document.ArticleDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleEsRepository extends ElasticsearchRepository<ArticleDocument, String> {

    Optional<ArticleDocument> findByArticleId(Long articleId);

    void deleteByArticleId(Long articleId);
}
