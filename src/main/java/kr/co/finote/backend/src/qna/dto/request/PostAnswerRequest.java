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
public class PostAnswerRequest {

    @NotBlank(message = "답변 내용을 입력해주세요")
    private String content;
}
