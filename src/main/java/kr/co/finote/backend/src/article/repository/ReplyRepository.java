package kr.co.finote.backend.src.article.repository;

import kr.co.finote.backend.src.article.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {}
