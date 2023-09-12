package kr.co.finote.backend.src.qna.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import kr.co.finote.backend.global.annotation.Login;
import kr.co.finote.backend.src.qna.dto.request.PostQuestionRequest;
import kr.co.finote.backend.src.qna.dto.response.PostQuestionResponse;
import kr.co.finote.backend.src.qna.service.QuestionService;
import kr.co.finote.backend.src.user.domain.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/qna")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Question API", description = "질문 API")
public class QuestionApi {

    QuestionService questionService;

    @Operation(summary = "질문글 생성")
    @PostMapping("/write")
    public PostQuestionResponse postQuestion(
            @Login User LoginUser, @RequestBody @Valid PostQuestionRequest request) {
        return questionService.postQuestion(LoginUser, request);
    }
}
