package kr.co.finote.backend.src.qna.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class QuestionPreviewListResponse {

    private int page;
    private int size;

    private List<QuestionPreviewResponse> questionList;

    public static QuestionPreviewListResponse createdQuestionPreviewListResponse(
            int page, int size, List<QuestionPreviewResponse> questionList) {
        return QuestionPreviewListResponse.builder()
                .page(page)
                .size(size)
                .questionList(questionList)
                .build();
    }
}
