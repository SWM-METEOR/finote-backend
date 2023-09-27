package kr.co.finote.backend.src.qna.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InlineQnaRequest {

    @NotBlank(message = "드래그한 내용이 없습니다.")
    private String dragText;
}
