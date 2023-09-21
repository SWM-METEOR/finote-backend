package kr.co.finote.backend.src.article.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import kr.co.finote.backend.global.annotation.Login;
import kr.co.finote.backend.src.article.dto.request.ReplyRequest;
import kr.co.finote.backend.src.article.dto.response.PostReplyResponse;
import kr.co.finote.backend.src.article.dto.response.ReplyListResponse;
import kr.co.finote.backend.src.article.service.ReplyService;
import kr.co.finote.backend.src.user.domain.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Reply API", description = "블로그 댓글 API")
public class ReplyApi {

    ReplyService replyService;

    /* 댓글 API */
    @Operation(summary = "댓글 전체 조회")
    @GetMapping("/replies/{nickname}/{title}")
    public ReplyListResponse getReplies(@PathVariable String nickname, @PathVariable String title) {
        return replyService.getReplies(nickname, title);
    }

    @Operation(summary = "댓글 작성")
    @PostMapping("/replies/write/{nickname}/{title}")
    public PostReplyResponse postReply(
            @Login User loginUser,
            @PathVariable String nickname,
            @PathVariable String title,
            @RequestBody @Valid ReplyRequest request) {
        return replyService.postReply(loginUser, nickname, title, request);
    }
}
