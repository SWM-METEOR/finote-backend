package kr.co.finote.backend.src.common.api;

import io.swagger.v3.oas.annotations.Operation;
import kr.co.finote.backend.global.annotation.Login;
import kr.co.finote.backend.src.article.dto.response.ArticlePreviewListResponse;
import kr.co.finote.backend.src.article.service.ArticleService;
import kr.co.finote.backend.src.common.dto.FollowResultResponse;
import kr.co.finote.backend.src.common.service.FollowService;
import kr.co.finote.backend.src.user.domain.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommonApi {

    FollowService followService;
    ArticleService articleService;

    @Operation(summary = "유저 팔로우", description = "팔로우 할 유저의 닉네임을 Path-variable로 전달")
    @PostMapping("/follow/{nickname}")
    public FollowResultResponse follow(
            @Login User loginUser, @PathVariable("nickname") String nickname) {
        return followService.follow(loginUser, nickname);
    }

    @Operation(summary = "유저 언팔로우", description = "언팔로우 할 유저의 닉네임을 Path-variable로 전달")
    @PostMapping("/unfollow/{nickname}")
    public FollowResultResponse unfollow(
            @Login User loginUser, @PathVariable("nickname") String nickname) {
        return followService.unfollow(loginUser, nickname);
    }

    @Operation(summary = "트렌딩 아티클", description = "글 30개씩 무한 스크롤에 대응하여 결과 제공")
    @GetMapping("/trend-articles")
    public ArticlePreviewListResponse trendArticles(
            @RequestParam int page, @RequestParam(required = false, defaultValue = "30") int size) {
        return articleService.trendArticles(page, size);
    }
}
