package kr.co.finote.backend.src.qna.service;

import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.InvalidInputException;
import kr.co.finote.backend.global.exception.NotFoundException;
import kr.co.finote.backend.src.qna.domain.Question;
import kr.co.finote.backend.src.qna.dto.request.PostQuestionRequest;
import kr.co.finote.backend.src.qna.dto.response.PostQuestionResponse;
import kr.co.finote.backend.src.qna.dto.response.QuestionResponse;
import kr.co.finote.backend.src.qna.repository.QuestionRepository;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    private final UserService userService;

    @Transactional
    public PostQuestionResponse postQuestion(User author, PostQuestionRequest request) {
        isDuplicatedTitle(author, request);

        Question savedQuestion = questionRepository.save(Question.createQuestion(author, request));
        return PostQuestionResponse.of(author, savedQuestion);
    }

    public QuestionResponse lookupByNicknameAndTitle(String nickname, String title) {
        User author = userService.findByNickname(nickname);
        Question findQuestion = findByUserAndTitle(author, title);

        return QuestionResponse.of(author, findQuestion);
    }

    public QuestionResponse lookupById(Long questionId) {
        Question findQuestion =
                questionRepository
                        .findByIdAndIsDeleted(questionId, false)
                        .orElseThrow(() -> new NotFoundException(ResponseCode.QUESTION_NOT_FOUND));
        User author = findQuestion.getUser();

        return QuestionResponse.of(author, findQuestion);
    }

    @Transactional
    public PostQuestionResponse editQuestion(
            User loginUser, Long questionId, PostQuestionRequest request) {
        Question question =
                questionRepository
                        .findByIdAndIsDeleted(questionId, false)
                        .orElseThrow(() -> new NotFoundException(ResponseCode.QUESTION_NOT_FOUND));

        if (!question.getUser().getNickname().equals(loginUser.getNickname())) {
            throw new InvalidInputException(ResponseCode.QUESTION_NOT_WRITER);
        }

        question.edit(request);
        return PostQuestionResponse.of(loginUser, question);
    }

    @Transactional
    public void deleteQuestion(User loginUser, Long questionId) {
        Question question =
                questionRepository
                        .findByIdAndIsDeleted(questionId, false)
                        .orElseThrow(() -> new NotFoundException(ResponseCode.QUESTION_NOT_FOUND));

        if (!question.getUser().getNickname().equals(loginUser.getNickname())) {
            throw new InvalidInputException(ResponseCode.QUESTION_NOT_WRITER);
        }

        question.delete();
    }

    private Question findByUserAndTitle(User user, String title) {
        return questionRepository
                .findByUserAndTitleAndIsDeleted(user, title, false)
                .orElseThrow(() -> new NotFoundException(ResponseCode.QUESTION_NOT_FOUND));
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
