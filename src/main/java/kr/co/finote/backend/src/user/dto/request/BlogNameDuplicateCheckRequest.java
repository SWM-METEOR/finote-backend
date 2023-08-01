package kr.co.finote.backend.src.user.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BlogNameDuplicateCheckRequest {

    @NotBlank(message = "블로그 이름을 입력해주세요.")
    private String blogName;
}
