package kr.co.finote.backend.src.qna.repository;

import java.util.List;
import kr.co.finote.backend.src.qna.domain.Answer;
import kr.co.finote.backend.src.qna.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query(
            "select a from Answer a join fetch a.user where a.question = :question and a.isDeleted = false")
    List<Answer> findAllWithQuestion(@Param("question") Question question);
}
