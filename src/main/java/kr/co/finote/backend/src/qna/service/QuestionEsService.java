package kr.co.finote.backend.src.qna.service;

import java.util.List;
import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.NotFoundException;
import kr.co.finote.backend.src.qna.document.QuestionDocument;
import kr.co.finote.backend.src.qna.domain.Question;
import kr.co.finote.backend.src.qna.repository.QuestionEsRepository;
import kr.co.finote.backend.src.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionEsService {

    private final QuestionEsRepository questionEsRepository;

    public void saveDocument(Question question, User user) {
        QuestionDocument questionDocument = QuestionDocument.createQuestionDocument(question, user);
        questionEsRepository.save(questionDocument);
    }

    public void editDocumentByQuestion(Question question) {
        List<QuestionDocument> questionDocuments =
                questionEsRepository.findByQuestionId(question.getId());

        if (questionDocuments.isEmpty()) {
            throw new NotFoundException(ResponseCode.QUESTION_NOT_FOUND);
        }

        QuestionDocument questionDocument = questionDocuments.get(0);
        questionDocument.editByQuestion(question);
        // Dirty checking 지원 안되므로, 명시적으로 처리
        questionEsRepository.save(questionDocument);
    }

    public void editDocumentByUser(User user) {
        List<QuestionDocument> questionDocuments =
                questionEsRepository.findByAuthorNickname(user.getNickname());

        questionDocuments.forEach(questionDocument -> questionDocument.editByUser(user));
        questionEsRepository.saveAll(questionDocuments);
    }

    public void deleteDocument(Long questionId) {
        questionEsRepository.deleteByQuestionId(questionId);
    }
}
