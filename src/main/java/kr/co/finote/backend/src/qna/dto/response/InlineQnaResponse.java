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
public class InlineQnaResponse {

    private List<QuestionPreviewResponse> questionList;

    public static InlineQnaResponse createInlineQnaResponse(
            List<QuestionPreviewResponse> qusetionList) {
        return InlineQnaResponse.builder().questionList(qusetionList).build();
    }
}
