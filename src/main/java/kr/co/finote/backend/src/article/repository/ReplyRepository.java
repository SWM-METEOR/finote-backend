package kr.co.finote.backend.src.article.repository;

import java.util.List;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findByArticleAndIsDeleted(Article article, boolean isDeleted);
}
