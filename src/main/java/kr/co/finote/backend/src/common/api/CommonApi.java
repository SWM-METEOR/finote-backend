package kr.co.finote.backend.src.common.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.finote.backend.global.annotation.Login;
import kr.co.finote.backend.src.article.dto.response.ArticlePreviewListResponse;
import kr.co.finote.backend.src.article.service.ArticleService;
import kr.co.finote.backend.src.common.dto.request.FileUploadRequest;
import kr.co.finote.backend.src.common.dto.response.*;
import kr.co.finote.backend.src.common.service.FollowService;
import kr.co.finote.backend.src.common.service.S3Service;
import kr.co.finote.backend.src.user.domain.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Common API", description = "공통 API")
public class CommonApi {

    FollowService followService;
    ArticleService articleService;
    S3Service s3Service;

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

    @Operation(summary = "유저 팔로잉 목록", description = "닉네임에 해당하는 유저의 팔로잉 목록 조회")
    @GetMapping("/followings/{nickname}")
    public FollowUserListResponse followings(@PathVariable String nickname) {
        return followService.followings(nickname);
    }

    @Operation(summary = "유저 팔로워 목록", description = "닉네임에 해당하는 유저의 팔로워 목록 조회")
    @GetMapping("/followers/{nickname}")
    public FollowUserListResponse followers(@PathVariable String nickname) {
        return followService.followers(nickname);
    }

    @Operation(summary = "pre-signed url 조회", description = "이미지 업로드 위한 url 제공")
    @PostMapping("/pre-signed-url")
    public PresignedUrlResponse getPreSignedUrl(@RequestBody FileUploadRequest request) {
        return s3Service.getPresignedUrl(request);
    }

    @Operation(summary = "내 팔로워 수")
    @GetMapping("/followers/count")
    public FollowersCountResponse getFollowerCount(@Login User loginUser) {
        return followService.followersCount(loginUser);
    }

    @Operation(summary = "내 팔로잉 수")
    @GetMapping("/followings/count")
    public FollowingsCountResponse getFollowingCount(@Login User loginUser) {
        return followService.followingsCount(loginUser);
    }

    @Operation(summary = "다른 유저의 팔로워 수")
    @GetMapping("/followers/count/{nickname}")
    public FollowersCountResponse getFollowingCount(@PathVariable String nickname) {
        return followService.followersCount(nickname);
    }
}
