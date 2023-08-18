package kr.co.finote.backend.src.article.repository;

import kr.co.finote.backend.src.article.domain.ArticleKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleKeywordRepository extends JpaRepository<ArticleKeyword, Long> {}
