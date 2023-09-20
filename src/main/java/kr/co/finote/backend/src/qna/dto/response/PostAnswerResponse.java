package kr.co.finote.backend.src.qna.dto.response;

import kr.co.finote.backend.src.qna.domain.Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostAnswerResponse {

    private Long id;

    public static PostAnswerResponse of(Answer answer) {
        return PostAnswerResponse.builder().id(answer.getId()).build();
    }
}
