package kr.co.finote.backend.src.user.service;

import java.util.Optional;
import kr.co.finote.backend.global.exception.UnAuthorizedException;
import kr.co.finote.backend.global.jwt.JwtToken;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.repository.UserRepository;
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
    @Autowired private UserRepository userRepository;

    @Test
    @DisplayName("회원가입하지 않은 유저는 로그아웃 시 예외가 발생한다")
    void logoutFail() {
        // given
        JwtToken jwtToken = new JwtToken("atk", "rtk");
        // when

        // then
        Assertions.assertThrows(UnAuthorizedException.class, () -> loginService.logout(jwtToken));
    }

    @Test
    @DisplayName("회원가입한 유저는 로그아웃을 수행할 수 있다.")
    void logoutSuccess() {
        String email = "meteor123@gmail.com";
        String refreshToken = "meteor";
        // given
        User user =
                User.builder().email(email).profileImageUrl("s3").refreshToken(refreshToken).build();
        userRepository.save(user);

        JwtToken jwtToken = new JwtToken("atk", refreshToken);

        // when
        loginService.logout(jwtToken);

        // then
        Optional<User> findUser = userRepository.findByEmailAndIsDeleted(email, false);
        Assertions.assertTrue(findUser.isPresent() && findUser.get().getRefreshToken() == null);
    }
}
