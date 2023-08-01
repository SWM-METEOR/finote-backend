package kr.co.finote.backend.src.user.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AdditionalInfoRequest {

    @NotBlank(message = "프로필 이미지 url을 입력해주세요.")
    private String profileImage;

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    @NotBlank(message = "블로그 이름을 입력해주세요.")
    private String blogName;

    @NotBlank(message = "블로그 url을 입력해주세요.")
    private String blogUrl;
}
