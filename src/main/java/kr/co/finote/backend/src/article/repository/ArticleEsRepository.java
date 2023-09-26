package kr.co.finote.backend.src.article.repository;

import java.util.List;
import kr.co.finote.backend.src.article.document.ArticleDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleEsRepository extends ElasticsearchRepository<ArticleDocument, String> {

    List<ArticleDocument> findByArticleId(Long articleId);

    void deleteByArticleId(Long articleId);

    List<ArticleDocument> findByAuthorNickname(String authorNickName);
}
