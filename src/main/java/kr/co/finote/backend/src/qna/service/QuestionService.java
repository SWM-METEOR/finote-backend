package kr.co.finote.backend.src.qna.service;

import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.InvalidInputException;
import kr.co.finote.backend.src.qna.domain.Question;
import kr.co.finote.backend.src.qna.dto.request.PostQuestionRequest;
import kr.co.finote.backend.src.qna.dto.response.PostQuestionResponse;
import kr.co.finote.backend.src.qna.repository.QuestionRepository;
import kr.co.finote.backend.src.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Transactional
    public PostQuestionResponse postQuestion(User author, PostQuestionRequest request) {
        isDuplicatedTitle(author, request);

        Question savedQuestion = questionRepository.save(Question.createQuestion(author, request));
        return PostQuestionResponse.of(author, savedQuestion);
    }

    private void isDuplicatedTitle(User author, PostQuestionRequest request) {
        questionRepository
                .findByUserAndTitleAndIsDeleted(author, request.getTitle(), false)
                .ifPresent(
                        question -> {
                            throw new InvalidInputException(ResponseCode.QUESTION_ALREADY_EXIST);
                        });
    }
}
