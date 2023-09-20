package kr.co.finote.backend.src.qna.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import kr.co.finote.backend.global.annotation.Login;
import kr.co.finote.backend.src.qna.dto.request.PostAnswerRequest;
import kr.co.finote.backend.src.qna.dto.response.PostAnswerResponse;
import kr.co.finote.backend.src.qna.service.AnswerService;
import kr.co.finote.backend.src.user.domain.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qna")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Answer API", description = "답변 API")
public class AnswerApi {

    AnswerService answerService;

    @Operation(summary = "질문 글에 대해 답변 생성")
    @PostMapping("/answers/write/{nickname}/{title}")
    public PostAnswerResponse postAnswer(
            @Login User loginUser,
            @PathVariable String nickname,
            @PathVariable String title,
            @RequestBody @Valid PostAnswerRequest request) {
        return answerService.postAnswer(loginUser, nickname, title, request);
    }
}
