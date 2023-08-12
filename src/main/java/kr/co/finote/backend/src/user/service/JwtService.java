package kr.co.finote.backend.src.user.service;

import java.util.Optional;
import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.UnAuthorizedException;
import kr.co.finote.backend.global.jwt.JwtToken;
import kr.co.finote.backend.global.jwt.JwtTokenProvider;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Transactional
    public JwtToken reIssue(JwtToken jwtToken) {
        String refreshToken = jwtToken.getRefreshToken();

        User user =
                userRepository
                        .findByRefreshTokenAndIsDeleted(refreshToken, false)
                        .orElseThrow(() -> new UnAuthorizedException(ResponseCode.INVALID_REFRESH_TOKEN));

        if (jwtTokenProvider.validateTokenExpiration(refreshToken)) {
            String token = jwtTokenProvider.createToken(user.getEmail());
            String newRefreshToken = jwtTokenProvider.createRefreshToken();

            user.updateRefreshToken(newRefreshToken);

            return new JwtToken(token, newRefreshToken);
        }

        user.updateRefreshToken(null);
        throw new UnAuthorizedException(ResponseCode.INVALID_REFRESH_TOKEN);
    }

    public boolean hasRefreshToken(String token) {
        String email = jwtTokenProvider.getMemberEmail(token);

        Optional<User> findUser = userRepository.findByEmailAndIsDeleted(email, false);
        return findUser.filter(user -> user.getRefreshToken() != null).isPresent();
    }
}
