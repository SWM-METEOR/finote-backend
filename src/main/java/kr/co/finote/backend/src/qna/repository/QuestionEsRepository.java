package kr.co.finote.backend.src.qna.repository;

import java.util.Optional;
import kr.co.finote.backend.src.qna.document.QuestionDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionEsRepository extends ElasticsearchRepository<QuestionDocument, String> {

    Optional<QuestionDocument> findByQuestionId(Long questionId);

    void deleteByQuestionId(Long questionId);
}
