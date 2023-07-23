package kr.co.finote.backend.src.article.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiSearchRequest {

    private Boolean code;

    @NotEmpty(message = "GPT 질문 내용이 있어야합니다.")
    @Size(max = 1000)
    private String prompt;
}
