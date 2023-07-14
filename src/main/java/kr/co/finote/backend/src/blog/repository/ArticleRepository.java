package kr.co.finote.backend.src.blog.repository;

import java.util.Optional;
import kr.co.finote.backend.src.blog.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, String> {

    Optional<Article> findByIdAndIsDeleted(String id, Boolean isDeleted);
}
