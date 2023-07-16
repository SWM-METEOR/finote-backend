package kr.co.finote.backend.src.user.api;

import static kr.co.finote.backend.global.code.ResponseCode.UNAUTHENTICATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.HashMap;
import java.util.Map;
import kr.co.finote.backend.global.exception.CustomException;
import kr.co.finote.backend.src.user.domain.User;
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

    @PostMapping(value = "/validation/nickname", consumes = APPLICATION_JSON_VALUE)
    public Map<String, Boolean> validateNickname(@RequestBody Map<String, String> requestData) {
        String nickname = requestData.get("nickname");

        boolean isDuplicated = userService.duplicateNickname(nickname);

        Map<String, Boolean> map = new HashMap<>();
        map.put("isDuplicated", isDuplicated);
        return map;
    }

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
}
