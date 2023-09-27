package kr.co.finote.backend.src.qna.dto.response;

import java.time.format.DateTimeFormatter;
import kr.co.finote.backend.src.qna.document.QuestionDocument;
import kr.co.finote.backend.src.qna.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionPreviewResponse {

    private Long id;

    private String title;
    private String authorNickname;
    private String profileImageUrl;
    private String createdDate;

    private int totalAnswer;

    public static QuestionPreviewResponse of(Question question) {
        return QuestionPreviewResponse.builder()
                .id(question.getId())
                .title(question.getTitle())
                .authorNickname(question.getUser().getNickname())
                .profileImageUrl(question.getUser().getProfileImageUrl())
                .createdDate(question.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .totalAnswer(question.getTotalAnswer())
                .build();
    }

    public static QuestionPreviewResponse of(QuestionDocument document) {
        return QuestionPreviewResponse.builder()
                .id(document.getQuestionId())
                .title(document.getTitle())
                .authorNickname(document.getAuthorNickname())
                .profileImageUrl(document.getProfileImageUrl())
                .createdDate(document.getCreatedDate())
                .totalAnswer(document.getTotalAnswer())
                .build();
    }
}
