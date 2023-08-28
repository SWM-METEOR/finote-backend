package kr.co.finote.backend.src.article.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DragArticleRequest {

    @NotBlank(message = "드래그한 텍스트가 없습니다.")
    private String dragText;
}
