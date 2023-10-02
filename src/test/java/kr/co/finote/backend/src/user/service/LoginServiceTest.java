package kr.co.finote.backend.src.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.finote.backend.global.exception.UnAuthorizedException;
import kr.co.finote.backend.global.jwt.JwtToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class LoginServiceTest {

    @Autowired private LoginService loginService;

    @Test
    @DisplayName("회원가입하지 않은 유저는 로그아웃 시 예외가 발생한다")
    void logoutTest() throws JsonProcessingException {
        // given
        JwtToken jwtToken = new JwtToken("atk", "rtk");

        // when

        // then
        Assertions.assertThrows(UnAuthorizedException.class, () -> loginService.logout(jwtToken));
    }
}
