package kr.co.finote.backend.src.user.api;

import io.swagger.v3.oas.annotations.Operation;
import kr.co.finote.backend.global.jwt.JwtToken;
import kr.co.finote.backend.src.user.service.JwtService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtApi {

    JwtService jwtService;

    @Operation(summary = "토큰 재발급", description = "해당 요청에서는 액세스 토큰, 리프레시 토큰이 언제나 재발급 처리")
    @PostMapping("/jwt/re-issue")
    public JwtToken reIssue(@RequestBody JwtToken jwtToken) {
        return jwtService.reIssue(jwtToken);
    }
}
