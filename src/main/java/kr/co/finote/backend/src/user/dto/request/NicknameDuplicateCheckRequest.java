package kr.co.finote.backend.src.user.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;

@Getter
public class NicknameDuplicateCheckRequest {

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(max = 10, message = "닉네임은 10자 이하로 입력해주세요.")
    private String nickname;
}
