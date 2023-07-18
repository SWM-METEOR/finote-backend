package kr.co.finote.backend.src.user.api;

import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kr.co.finote.backend.global.authentication.oauth.google.GoogleOauth;
import kr.co.finote.backend.global.authentication.oauth.google.dto.request.GoogleAccessTokenRequest;
import kr.co.finote.backend.global.authentication.oauth.google.dto.response.GoogleLoginResponse;
import kr.co.finote.backend.global.authentication.oauth.google.dto.response.GoogleOauthUserInfoResponse;
import kr.co.finote.backend.src.user.dto.response.SaveUserResponse;
import kr.co.finote.backend.src.user.service.LoginService;
import kr.co.finote.backend.src.user.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class LoginApi {

    private final GoogleOauth googleOauth;
    private final LoginService loginService;
    private final SessionService sessionService;

    // TODO : Front에서 Access Token을 위한 코드 발급 완성 시 삭제
    @Operation(summary = "구글 로그인", description = "브라우저에서 직접 테스트 해야함")
    @GetMapping("/login-google")
    public void loginGoogle(HttpServletResponse response) {
        log.info("/login-google");

        try {
            response.sendRedirect(googleOauth.getOauthRedirectURL());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "구글 로그인 redirect url", description = "브라우저에서 직접 테스트 해야함")
    @GetMapping("/auth/google/")
    public GoogleLoginResponse auth(@RequestParam String code, HttpServletRequest request) {
        log.info("Code from Google social Login API {}", code);

        GoogleAccessTokenRequest googleAccessToken = loginService.getGoogleAccessToken(code);
        GoogleOauthUserInfoResponse GoogleOauthUserInfo =
                loginService.getGoogleUserInfo(googleAccessToken);

        SaveUserResponse saveUserResponse = loginService.saveUser(GoogleOauthUserInfo);
        // TODO : 이후 로그아웃 API를 통해 명시적 세션 만료 기능 추가해야함.
        sessionService.startSession(request, saveUserResponse.getUser());

        return GoogleLoginResponse.builder()
                .accessToken(googleAccessToken.getAccessToken())
                .refreshToken(googleAccessToken.getRefreshToken())
                .newUser(saveUserResponse.getNewUser())
                .build();
    }
}
