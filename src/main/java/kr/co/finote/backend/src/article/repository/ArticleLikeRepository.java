package kr.co.finote.backend.src.article.repository;

import java.util.Optional;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.domain.ArticleLike;
import kr.co.finote.backend.src.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {
    Optional<ArticleLike> findByUserAndArticle(User user, Article article);

    void deleteAllByArticle(Article article);

    int countByUserAndIsDeleted(User user, boolean isDeleted);

    Page<ArticleLike> findAllByUserAndIsDeleted(User user, boolean isDeleted, Pageable pageable);
}
