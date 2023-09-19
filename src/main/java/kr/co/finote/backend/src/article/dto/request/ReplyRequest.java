package kr.co.finote.backend.src.article.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReplyRequest {

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;
}
