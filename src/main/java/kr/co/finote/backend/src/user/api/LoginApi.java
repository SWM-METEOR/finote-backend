package kr.co.finote.backend.src.user.api;

import io.swagger.v3.oas.annotations.Operation;
import kr.co.finote.backend.global.authentication.oauth.google.dto.request.GoogleAccessToken;
import kr.co.finote.backend.global.authentication.oauth.google.dto.response.GoogleLoginResponse;
import kr.co.finote.backend.global.authentication.oauth.google.dto.response.GoogleUserInfo;
import kr.co.finote.backend.global.jwt.JwtToken;
import kr.co.finote.backend.src.user.dto.response.GoogleLoginCodeResponse;
import kr.co.finote.backend.src.user.service.LoginService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoginApi {

    LoginService loginService;

    @Operation(summary = "구글 로그인 처리", description = "프론트에서 발급받은 Code를 전달해주어야 함.")
    @GetMapping("/auth/google")
    public GoogleLoginResponse auth(@RequestParam String code) throws Exception {
        GoogleAccessToken googleAccessToken = loginService.getGoogleAccessToken(code);
        GoogleUserInfo googleUserInfo = loginService.getGoogleUserInfo(googleAccessToken);
        return loginService.addOrUpdateUser(googleUserInfo);
    }

    @Operation(
            summary = "구글 로그인 콜백 처리",
            description =
                    "백엔드 로그인 테스트용. 요청 url [https://accounts.google.com/o/oauth2/v2/auth?client_id=96307152718-ku7cun76hfk7scuo8pohntmjpfu1eauq.apps.googleusercontent.com&redirect_uri=http://localhost:8080/users/callback&response_type=code&scope=profile email&access_type=offline], "
                            + "[https://accounts.google.com/o/oauth2/v2/auth?client_id=96307152718-ku7cun76hfk7scuo8pohntmjpfu1eauq.apps.googleusercontent.com&redirect_uri=https://dev-api.finote.co.kr/users/callback&response_type=code&scope=profile email&access_type=offline]")
    @GetMapping("/callback")
    public GoogleLoginCodeResponse callback(@RequestParam String code) {
        return GoogleLoginCodeResponse.createGoogleLoginCodeResponse(code);
    }

    @Operation(summary = "로그아웃", description = "토큰 만료기간과 상관 없이 액세스, 리프레시 토큰 모두 초기화")
    @PostMapping("/logout")
    public void logout(@RequestBody JwtToken jwtToken) {
        loginService.logout(jwtToken);
    }
}
