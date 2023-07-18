package kr.co.finote.backend.src.article.repository;

import kr.co.finote.backend.src.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    //    Optional<Article> findByIdAndIsDeleted(Long id, Boolean isDeleted);
}
