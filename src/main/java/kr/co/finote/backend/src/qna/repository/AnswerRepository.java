package kr.co.finote.backend.src.qna.repository;

import kr.co.finote.backend.src.qna.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {}
