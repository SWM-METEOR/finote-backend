package kr.co.finote.backend.src.user.api;

import static kr.co.finote.backend.global.code.ResponseCode.UNAUTHENTICATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.HashMap;
import java.util.Map;
import kr.co.finote.backend.global.exception.CustomException;
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
    @PostMapping(value = "/validation/nickname", consumes = APPLICATION_JSON_VALUE)
    public Map<String, Boolean> validateNickname(@RequestBody Map<String, String> requestData) {
        String nickname = requestData.get("nickname");

        boolean isDuplicated = userService.duplicateNickname(nickname);

        Map<String, Boolean> map = new HashMap<>();
        map.put("isDuplicated", isDuplicated);
        return map;
    }

    @PostMapping(value = "/validation/blog-name", consumes = APPLICATION_JSON_VALUE)
    public Map<String, Boolean> validateBlogName(@RequestBody Map<String, String> requestData) {
        String blogName = requestData.get("blogName");

        boolean isDuplicated = userService.duplicateBlogName(blogName);

        Map<String, Boolean> map = new HashMap<>();
        map.put("isDuplicated", isDuplicated);
        return map;
    }

    @PostMapping(value = "/validation/blog-url", consumes = APPLICATION_JSON_VALUE)
    public Map<String, Boolean> validateBlogUrl(@RequestBody Map<String, String> requestData) {
        String blogUrl = requestData.get("blogUrl");

        boolean isDuplicated = userService.duplicateBlogUrl(blogUrl);

        Map<String, Boolean> map = new HashMap<>();
        map.put("isDuplicated", isDuplicated);
        return map;
    }

    /* Field Getter API */
    @GetMapping("/nickname")
    public Map<String, String> getNickname(
            @SessionAttribute(name = SessionUtils.LOGIN_USER, required = false) User loginUser) {

        if (loginUser == null) {
            throw new CustomException(UNAUTHENTICATED);
        }

        Map<String, String> map = new HashMap<>();
        map.put("nickname", loginUser.getNickname());

        return map;
    }

    @GetMapping("/blog-info")
    public BlogResponse getBlogInfo(
            @SessionAttribute(name = SessionUtils.LOGIN_USER, required = false) User loginUser) {

        if (loginUser == null) {
            throw new CustomException(UNAUTHENTICATED);
        }

        return new BlogResponse(loginUser.getBlogName(), loginUser.getBlogUrl());
    }

    /* API related to additional-info */
    @PostMapping("/additional-info")
    public void additionalInfo(
            @SessionAttribute(name = SessionUtils.LOGIN_USER, required = false) User loginUser,
            @RequestBody AdditionalInfoRequest additionalInfoRequest) {

        if (loginUser == null) {
            throw new CustomException(UNAUTHENTICATED);
        }

        userService.editAdditionalInfo(loginUser, additionalInfoRequest);
    }
}
