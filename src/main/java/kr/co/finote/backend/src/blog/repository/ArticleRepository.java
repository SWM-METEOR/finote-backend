package kr.co.finote.backend.src.blog.repository;

import kr.co.finote.backend.src.blog.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {}
