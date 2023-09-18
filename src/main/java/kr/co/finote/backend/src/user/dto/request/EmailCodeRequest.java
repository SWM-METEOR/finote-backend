package kr.co.finote.backend.src.user.dto.request;

import javax.validation.constraints.NotNull;
import kr.co.finote.backend.global.annotation.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmailCodeRequest {

    @NotNull(message = "이메일이 입력되지 않았습니다.")
    @ValidEmail
    private String email;
}
