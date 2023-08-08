package kr.co.finote.backend.src.user.service;

import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.CustomException;
import kr.co.finote.backend.global.jwt.JwtTokenDto;
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
    public JwtTokenDto reIssue(JwtTokenDto request) {
        String refreshToken = request.getRefreshToken();

        User user = userRepository.findByRefreshTokenAndIsDeleted(refreshToken, false)
                .orElseThrow(() -> new CustomException(ResponseCode.NO_REFRESH_TOKEN));

        if (jwtTokenProvider.validateTokenExpiration(refreshToken)) {
            String token = jwtTokenProvider.createToken(user.getEmail());
            return new JwtTokenDto(token, refreshToken);
        }

        String newRefreshToken = jwtTokenProvider.createRefreshToken();
        user.setRefreshToken(newRefreshToken);
        String token = jwtTokenProvider.createToken(user.getEmail());

        return new JwtTokenDto(token, newRefreshToken);
    }
}
