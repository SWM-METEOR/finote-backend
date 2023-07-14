package kr.co.finote.backend.src.user.api;

import static org.springframework.http.MediaType.*;

import java.util.HashMap;
import java.util.Map;
import kr.co.finote.backend.src.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
