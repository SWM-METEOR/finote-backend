package kr.co.finote.backend.src.user.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AdditionalInfoRequest {

    // TODO : finote의 기본 프사 S3링크를 @Default로 설정하기
    private String profileImageUrl;

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    @NotBlank(message = "블로그 이름을 입력해주세요.")
    private String blogName;
}
