package kr.co.finote.backend.src.qna.service;

import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.ConnectException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionEsService questionEsService;

    private final UserService userService;

    private final int MAX_CALL_COUNT = 3;

    @Transactional
    public PostQuestionResponse postQuestion(User author, PostQuestionRequest request) {
        isDuplicatedTitle(author, request);

        int callCount = 0;
        boolean isSaved = false;
        Question savedQuestion = questionRepository.save(Question.createQuestion(author, request));

        while (callCount < MAX_CALL_COUNT) {
            callCount += 1;
            try {
                questionEsService.saveDocument(savedQuestion, author);
                isSaved = true;
                break;
            } catch (Exception e) {
                log.info("Save Question Document : {}", callCount);
            }
        }
        if (!isSaved) throw new ConnectException(ResponseCode.ES_NOT_CONNECT);

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

        checkQuestionAuthority(loginUser, question);

        question.edit(request);
        questionEsService.editDocumentByQuestion(question);
        return PostQuestionResponse.of(loginUser, question);
    }

    @Transactional
    public void deleteQuestion(User loginUser, Long questionId) {
        Question question =
                questionRepository
                        .findByIdAndIsDeleted(questionId, false)
                        .orElseThrow(() -> new NotFoundException(ResponseCode.QUESTION_NOT_FOUND));

        checkQuestionAuthority(loginUser, question);

        question.delete();
        questionEsService.deleteDocument(question.getId());
    }

    private static void checkQuestionAuthority(User loginUser, Question question) {
        if (!question.getUser().getNickname().equals(loginUser.getNickname())) {
            throw new InvalidInputException(ResponseCode.QUESTION_NOT_WRITER);
        }
    }

    private Question findByUserAndTitle(User user, String title) {
        return questionRepository
                .findByUserAndTitleAndIsDeleted(user, title, false)
                .orElseThrow(() -> new NotFoundException(ResponseCode.QUESTION_NOT_FOUND));
    }

    public Question findByNicknameAndTitle(String nickname, String title) {
        User author = userService.findByNickname(nickname);

        return questionRepository
                .findByUserAndTitleAndIsDeleted(author, title, false)
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
