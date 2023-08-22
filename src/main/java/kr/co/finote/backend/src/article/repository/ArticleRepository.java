package kr.co.finote.backend.src.article.repository;

import java.util.Optional;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findByIdAndIsDeleted(Long id, Boolean isDeleted);

    Page<Article> findByUserAndIsDeleted(User user, Boolean isDeleted, Pageable pageable);
}