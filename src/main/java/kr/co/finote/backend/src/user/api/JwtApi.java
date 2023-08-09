package kr.co.finote.backend.src.user.api;

import kr.co.finote.backend.global.jwt.JwtTokenDto;
import kr.co.finote.backend.src.user.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class JwtApi {

    private final JwtService jwtService;

    @PostMapping("/jwt/re-issue")
    public JwtTokenDto reIssue(@RequestBody JwtTokenDto tokenDto) {
        return jwtService.reIssue(tokenDto);
    }
}
