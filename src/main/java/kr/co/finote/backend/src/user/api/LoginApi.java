package kr.co.finote.backend.src.user.api;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import kr.co.finote.backend.global.authentication.oauth.google.GoogleAccessTokenDto;
import kr.co.finote.backend.global.authentication.oauth.google.GoogleLoginResponseDto;
import kr.co.finote.backend.global.authentication.oauth.google.GoogleOauth;
import kr.co.finote.backend.global.authentication.oauth.google.GoogleOauthUserInfoDto;
import kr.co.finote.backend.src.user.service.LoginService;
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

    // TODO : Front에서 Access Token을 위한 코드 발급 완성 시 삭제
    @GetMapping("/login-google")
    public void loginGoogle(HttpServletResponse response) {
        log.info("/login-google");

        try {
            response.sendRedirect(googleOauth.getOauthRedirectURL());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/auth/google/")
    public GoogleLoginResponseDto auth(@RequestParam String code) {
        log.info("Code from Google social Login API {}", code);

        GoogleAccessTokenDto googleAccessToken = loginService.getGoogleAccessToken(code);
        GoogleOauthUserInfoDto GoogleOauthUserInfo = loginService.getGoogleUserInfo(googleAccessToken);

        // TODO : userinfo를 가지고 repository 접근해서 최초 로그인과 이미 존재하는 회원 구분
        Boolean newUser = loginService.saveUser(GoogleOauthUserInfo);

        // TODO : ResponseDTO 반환
        GoogleLoginResponseDto response =
                GoogleLoginResponseDto.builder()
                        .accessToken(googleAccessToken.getAccessToken())
                        .refreshToken(googleAccessToken.getRefreshToken())
                        .newUser(newUser)
                        .build();

        return response;
    }
}
