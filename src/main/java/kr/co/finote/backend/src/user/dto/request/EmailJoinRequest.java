package kr.co.finote.backend.src.user.dto.request;

import javax.validation.constraints.NotBlank;
import kr.co.finote.backend.global.annotation.ValidEmail;
import kr.co.finote.backend.global.annotation.ValidPassword;
import lombok.Getter;

@Getter
public class EmailJoinRequest {

    @NotBlank(message = "이메일이 입력되지 않았습니다.")
    @ValidEmail
    private String email;

    @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
    @ValidPassword
    private String password;
}
