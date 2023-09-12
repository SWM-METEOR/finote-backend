package kr.co.finote.backend.src.qna.repository;

import java.util.Optional;
import kr.co.finote.backend.src.qna.domain.Question;
import kr.co.finote.backend.src.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findByUserAndTitleAndIsDeleted(User user, String title, boolean isDeleted);

    Optional<Question> findByIdAndIsDeleted(Long questionId, boolean isDeleted);
}
