package kr.co.finote.backend.src.qna.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerListResponse {

    private List<AnswerResponse> answerResponseList;

    public static AnswerListResponse of(List<AnswerResponse> answerResponseList) {
        return AnswerListResponse.builder().answerResponseList(answerResponseList).build();
    }
}
