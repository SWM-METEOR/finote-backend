package kr.co.finote.backend.src.qna.repository;

import java.util.List;
import kr.co.finote.backend.src.qna.document.QuestionDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionEsRepository extends ElasticsearchRepository<QuestionDocument, String> {

    List<QuestionDocument> findByQuestionId(Long questionId);

    List<QuestionDocument> findByAuthorNickname(String authorNickname);

    void deleteByQuestionId(Long questionId);
}
