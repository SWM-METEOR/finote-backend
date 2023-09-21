package kr.co.finote.backend.src.article.repository;

import java.util.List;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Query("select r from Reply r join fetch r.user where r.article = :article")
    List<Reply> findAllWithArticle(@Param("article") Article article);
}
