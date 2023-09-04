package kr.co.finote.backend.src.user.api;

import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import kr.co.finote.backend.global.annotation.Login;
import kr.co.finote.backend.global.authentication.PrincipalDetails;
import kr.co.finote.backend.src.article.service.ArticleLikeService;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.dto.request.*;
import kr.co.finote.backend.src.user.dto.response.BlogResponse;
import kr.co.finote.backend.src.user.dto.response.LikeCountResponse;
import kr.co.finote.backend.src.user.dto.response.NicknameResponse;
import kr.co.finote.backend.src.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserApi {

    UserService userService;
    ArticleLikeService articleLikeService;

    /* Field Validation API */
    @Operation(summary = "닉네임 중복 검사")
    @PostMapping(value = "/validation/nickname")
    public void validateNickname(@RequestBody @Valid NicknameDuplicateCheckRequest request) {
        userService.validateNickname(
                request.getNickname()); // throws exception if nickname is invalid (e.g. duplicated)
    }

    @Operation(summary = "블로그 이름 중복 검사")
    @PostMapping(value = "/validation/blog-name")
    public void validateBlogName(@RequestBody @Valid BlogNameDuplicateCheckRequest request) {
        userService.validateBlogName(request.getBlogName());
    }

    @Operation(summary = "블로그 URL 중복 검사")
    @PostMapping(value = "/validation/blog-url")
    public void validateBlogUrl(@RequestBody @Valid BlogUrlDuplicateCheckRequest request) {
        userService.validateBlogUrl(request.getBlogUrl());
    }

    /* Field Getter API */
    @Operation(summary = "닉네임 가져오기")
    @GetMapping("/nickname")
    public NicknameResponse getNickname(@Login User loginUser) {
        return NicknameResponse.of(loginUser);
    }

    @Operation(summary = "블로그 정보(이름, url) 가져오기")
    @GetMapping("/blog-info")
    public BlogResponse getBlogInfo(@Login User loginUser) {
        return BlogResponse.of(loginUser);
    }

    /* API related to additional-info */
    @Operation(summary = "추가 정보 입력")
    @PostMapping("/additional-info")
    public void additionalInfo(@RequestBody @Valid AdditionalInfoRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        User loginUser = principal.getUser();
        userService.editAdditionalInfo(loginUser, request);
    }

    @Operation(summary = "내가 좋아요 한 글의 갯수")
    @GetMapping("/articles/like/count")
    public LikeCountResponse getLikeCount(@Login User loginUser) {
        return articleLikeService.getLikeCount(loginUser);
    }
}
