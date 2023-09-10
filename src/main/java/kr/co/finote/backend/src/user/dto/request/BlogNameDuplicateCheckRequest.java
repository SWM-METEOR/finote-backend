package kr.co.finote.backend.src.user.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;

@Getter
public class BlogNameDuplicateCheckRequest {

    @NotBlank(message = "블로그 이름을 입력해주세요.")
    @Size(max = 20, message = "블로그 이름은 20자 이하로 입력해주세요.")
    private String blogName;
}
