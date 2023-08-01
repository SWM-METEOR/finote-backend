package kr.co.finote.backend.src.user.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class NicknameDuplicateCheckRequest {

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;
}
