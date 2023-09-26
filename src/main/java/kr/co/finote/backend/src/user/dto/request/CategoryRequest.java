package kr.co.finote.backend.src.user.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CategoryRequest {

    @NotBlank(message = "카테고리 이름을 입력해주세요.")
    private String name;
}
