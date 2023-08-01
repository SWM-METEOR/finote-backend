package kr.co.finote.backend.src.user.api;

import io.swagger.v3.oas.annotations.Operation;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.dto.request.AdditionalInfoRequest;
import kr.co.finote.backend.src.user.dto.request.BlogNameDuplicateCheckRequest;
import kr.co.finote.backend.src.user.dto.request.BlogUrlDuplicateCheckRequest;
import kr.co.finote.backend.src.user.dto.request.NicknameDuplicateCheckRequest;
import kr.co.finote.backend.src.user.dto.response.BlogResponse;
import kr.co.finote.backend.src.user.dto.response.NicknameResponse;
import kr.co.finote.backend.src.user.service.UserService;
import kr.co.finote.backend.src.user.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserApi {

    private final UserService userService;

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
    public NicknameResponse getNickname(HttpSession httpSession) {
        User loginUser = (User) httpSession.getAttribute(SessionUtils.LOGIN_USER);
        return NicknameResponse.of(loginUser);
    }

    @Operation(summary = "블로그 정보(이름, url) 가져오기")
    @GetMapping("/blog-info")
    public BlogResponse getBlogInfo(HttpSession httpSession) {
        User loginUser = (User) httpSession.getAttribute(SessionUtils.LOGIN_USER);
        return BlogResponse.of(loginUser);
    }

    /* API related to additional-info */
    @Operation(summary = "추가 정보 입력")
    @PostMapping("/additional-info")
    public void additionalInfo(
            HttpSession httpSession, @RequestBody @Valid AdditionalInfoRequest request) {
        User loginUser = (User) httpSession.getAttribute(SessionUtils.LOGIN_USER);
        userService.editAdditionalInfo(loginUser, request);
    }
}
