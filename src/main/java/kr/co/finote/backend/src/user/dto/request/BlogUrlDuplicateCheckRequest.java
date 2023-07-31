package kr.co.finote.backend.src.user.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BlogUrlDuplicateCheckRequest {

    @NotBlank(message = "블로그 주소를 입력해주세요.")
    private String blogUrl;
}
