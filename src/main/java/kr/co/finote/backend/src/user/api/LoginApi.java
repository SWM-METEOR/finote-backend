package kr.co.finote.backend.src.user.api;

import io.swagger.v3.oas.annotations.Operation;
import javax.servlet.http.HttpServletRequest;
import kr.co.finote.backend.global.authentication.oauth.google.dto.request.GoogleAccessTokenRequest;
import kr.co.finote.backend.global.authentication.oauth.google.dto.response.GoogleLoginResponse;
import kr.co.finote.backend.global.authentication.oauth.google.dto.response.GoogleOauthUserInfoResponse;
import kr.co.finote.backend.src.user.dto.response.GoogleLoginCodeResponse;
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

    private final LoginService loginService;
    private final SessionService sessionService;

    @Operation(summary = "구글 로그인 처리", description = "프론트에서 발급받은 Code를 전달해주어야 함.")
    @GetMapping("/auth/google")
    public GoogleLoginResponse auth(@RequestParam String code, HttpServletRequest servletRequest)
            throws Exception {
        GoogleAccessTokenRequest googleAccessTokenRequest = loginService.getGoogleAccessToken(code);
        GoogleOauthUserInfoResponse googleOauthUserInfoResponse =
                loginService.getGoogleUserInfo(googleAccessTokenRequest);

        SaveUserResponse saveUserResponse = loginService.saveUser(googleOauthUserInfoResponse);

        // TODO : 이후 로그아웃 API를 통해 명시적 세션 만료 기능 추가해야함.
        sessionService.startSession(servletRequest, saveUserResponse.getUser());

        return GoogleLoginResponse.createGoogleLoginResponse(saveUserResponse.getIsNewUser());
    }

    @Operation(summary = "유저 로그인 상태 확인")
    @GetMapping("/check-login-status")
    public boolean checkLoginStatus(HttpServletRequest servletRequest) {
        return sessionService.checkLoginStatus(servletRequest);
    }

    @Operation(
            summary = "구글 로그인 콜백 처리",
            description =
                    "백엔드 로그인 테스트용. 요청 url [https://accounts.google.com/o/oauth2/v2/auth?client_id=96307152718-ku7cun76hfk7scuo8pohntmjpfu1eauq.apps.googleusercontent.com&redirect_uri=http://localhost:8080/users/callback&response_type=code&scope=profile email&access_type=offline]")
    @GetMapping("/callback")
    public GoogleLoginCodeResponse callback(@RequestParam String code) {
        log.info("code : {}", code);
        return GoogleLoginCodeResponse.createGoogleLoginCodeResponse(code);
    }
}
