package kr.co.finote.backend.src.user.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.swagger.v3.oas.annotations.Operation;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.dto.request.AdditionalInfoRequest;
import kr.co.finote.backend.src.user.dto.response.BlogResponse;
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
    @PostMapping(value = "/validation/nickname", consumes = APPLICATION_JSON_VALUE)
    public void validateNickname(@RequestBody Map<String, String> requestData) {
        String nickname = requestData.get("nickname");
        userService.validateNickname(
                nickname); // throws exception if nickname is invalid (e.g. duplicated)
    }

    @Operation(summary = "블로그 이름 중복 검사")
    @PostMapping(value = "/validation/blog-name", consumes = APPLICATION_JSON_VALUE)
    public void validateBlogName(@RequestBody Map<String, String> requestData) {
        String blogName = requestData.get("blogName");
        userService.validateBlogName(blogName);
    }

    @Operation(summary = "블로그 URL 중복 검사")
    @PostMapping(value = "/validation/blog-url", consumes = APPLICATION_JSON_VALUE)
    public void validateBlogUrl(@RequestBody Map<String, String> requestData) {
        String blogUrl = requestData.get("blogUrl");
        userService.validateBlogUrl(blogUrl);
    }

    /* Field Getter API */
    @Operation(summary = "닉네임 가져오기")
    @GetMapping("/nickname")
    public Map<String, String> getNickname(HttpSession httpSession) {
        Map<String, String> map = new HashMap<>();
        User loginUser = (User) httpSession.getAttribute(SessionUtils.LOGIN_USER);
        map.put("nickname", loginUser.getNickname());

        return map;
    }

    @Operation(summary = "블로그 정보(이름, url) 가져오기")
    @GetMapping("/blog-info")
    public BlogResponse getBlogInfo(HttpSession httpSession) {
        User loginUser = (User) httpSession.getAttribute(SessionUtils.LOGIN_USER);
        return new BlogResponse(loginUser.getBlogName(), loginUser.getBlogUrl());
    }

    /* API related to additional-info */
    @Operation(summary = "추가 정보 입력")
    @PostMapping("/additional-info")
    public void additionalInfo(
            HttpSession httpSession, @RequestBody AdditionalInfoRequest additionalInfoRequest) {
        User loginUser = (User) httpSession.getAttribute(SessionUtils.LOGIN_USER);
        userService.editAdditionalInfo(loginUser, additionalInfoRequest);
    }
}
