package kr.co.finote.backend.src.qna.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import kr.co.finote.backend.global.annotation.Login;
import kr.co.finote.backend.src.qna.dto.request.PostQuestionRequest;
import kr.co.finote.backend.src.qna.dto.response.PostQuestionResponse;
import kr.co.finote.backend.src.qna.dto.response.QuestionPreviewListResponse;
import kr.co.finote.backend.src.qna.dto.response.QuestionResponse;
import kr.co.finote.backend.src.qna.service.QuestionService;
import kr.co.finote.backend.src.user.domain.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qna")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Question API", description = "질문 API")
public class QuestionApi {

    QuestionService questionService;

    @Operation(summary = "질문답변 커뮤니티 메인 페이지", description = "무한 스크롤에 대응하여 결과 제공")
    @GetMapping("/question-list")
    public QuestionPreviewListResponse questionList(
            @RequestParam int page, @RequestParam(required = false, defaultValue = "30") int size) {
        return questionService.questionList(page, size);
    }

    @Operation(summary = "질문글 생성")
    @PostMapping("/write")
    public PostQuestionResponse postQuestion(
            @Login User LoginUser, @RequestBody @Valid PostQuestionRequest request) {
        return questionService.postQuestion(LoginUser, request);
    }

    @Operation(summary = "닉네임/제목 질문글 조회")
    @GetMapping("/{nickname}/{title}")
    public QuestionResponse getQuestionByNicknameAndTitle(
            @PathVariable String nickname, @PathVariable String title) {
        return questionService.lookupByNicknameAndTitle(nickname, title);
    }

    @Operation(summary = "id 질문글 조회")
    @GetMapping("/{question-id}")
    public QuestionResponse getQuestionById(@PathVariable("question-id") Long questionId) {
        return questionService.lookupById(questionId);
    }

    @Operation(summary = "질문글 수정")
    @PostMapping("/edit/{question-id}")
    public PostQuestionResponse editQuestion(
            @Login User loginUser,
            @PathVariable("question-id") Long questionId,
            @RequestBody @Valid PostQuestionRequest request) {
        return questionService.editQuestion(loginUser, questionId, request);
    }

    @Operation(summary = "질문글 삭제")
    @PostMapping("/delete/{question-id}")
    public void deleteQuestion(@Login User loginUser, @PathVariable("question-id") Long questionId) {
        questionService.deleteQuestion(loginUser, questionId);
    }
}
