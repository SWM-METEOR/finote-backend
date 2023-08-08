package kr.co.finote.backend.global.jwt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtTokenProviderTest {

    @Autowired JwtTokenProvider jwtTokenProvider;

    String AccessToken;

    @DisplayName("액세스 토큰 발급 테스트")
    @Test
    void createToken() {
        String token = jwtTokenProvider.createToken("sangwonj98@gmail.com");
        AccessToken = token;
        System.out.println("token = " + token);
    }

    @DisplayName("리프레시 토큰 발급 테스트")
    @Test
    void createRefreshToken() {
        String refreshToken = jwtTokenProvider.createRefreshToken();
        System.out.println("refreshToken = " + refreshToken);
    }

    @DisplayName("토큰을 제대로 인식하는지 테스트")
    @Test
    void resolveTokenTest() {
        String token = jwtTokenProvider.createToken("sangwonj98@gmail.com");
        String memberEmail = jwtTokenProvider.getMemberEmail(token);
        Assertions.assertEquals("sangwonj98@gmail.com", memberEmail);
    }
}
