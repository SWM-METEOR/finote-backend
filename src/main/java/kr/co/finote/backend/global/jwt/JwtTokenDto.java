package kr.co.finote.backend.global.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenDto {

    private String accessToken;
    private String refreshToken;
}
