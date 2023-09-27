package kr.co.finote.backend.src.qna.service;

import java.util.ArrayList;
import java.util.List;
import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.InvalidInputException;
import kr.co.finote.backend.global.exception.NotFoundException;
import kr.co.finote.backend.src.qna.document.QuestionDocument;
import kr.co.finote.backend.src.qna.domain.Question;
import kr.co.finote.backend.src.qna.dto.request.InlineQnaRequest;
import kr.co.finote.backend.src.qna.dto.request.PostQuestionRequest;
import kr.co.finote.backend.src.qna.dto.response.*;
import kr.co.finote.backend.src.qna.repository.QuestionRepository;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionEsService questionEsService;

    private final UserService userService;

    @Transactional
    public PostQuestionResponse postQuestion(User author, PostQuestionRequest request) {
        isDuplicatedTitle(author, request);
        Question savedQuestion = questionRepository.save(Question.createQuestion(author, request));

        questionEsService.save(savedQuestion, author);

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

    public QuestionPreviewListResponse questionList(int page, int size) {
        int pageNum = page - 1;
        Pageable pageable = PageRequest.of(pageNum, size, mainPageSort());

        List<Question> contents = questionRepository.findAllByIsDeleted(false, pageable);

        List<QuestionPreviewResponse> questionPreviewResponseList =
                ToQuestionPreviewResponses(contents);

        return QuestionPreviewListResponse.createdQuestionPreviewListResponse(
                page, size, questionPreviewResponseList);
    }

    private Sort mainPageSort() {
        return Sort.by(Sort.Order.desc("totalAnswer"), Sort.Order.desc("createdDate"));
    }

    private List<QuestionPreviewResponse> ToQuestionPreviewResponses(List<?> list) {
        List<QuestionPreviewResponse> questionPreviewResponseList = new ArrayList<>();

        for (Object object : list) {
            if (object instanceof Question) {
                Question question = (Question) object;
                questionPreviewResponseList.add(QuestionPreviewResponse.of(question));
            } else if (object instanceof QuestionDocument) {
                QuestionDocument document = (QuestionDocument) object;
                questionPreviewResponseList.add(QuestionPreviewResponse.of(document));
            }
        }

        return questionPreviewResponseList;
    }

    public InlineQnaResponse inlineQna(InlineQnaRequest request) {
        List<QuestionDocument> documents = questionEsService.inlineQna(request.getDragText());
        List<QuestionPreviewResponse> questionPreviewResponseList =
                ToQuestionPreviewResponses(documents);

        return InlineQnaResponse.createInlineQnaResponse(questionPreviewResponseList);
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
