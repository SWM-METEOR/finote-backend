package kr.co.finote.backend.src.qna.dto.response;

import java.time.format.DateTimeFormatter;
import kr.co.finote.backend.src.qna.domain.Question;
import kr.co.finote.backend.src.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponse {

    private Long id;

    private int totalAnswer;

    private String title;
    private String body;
    private String authorNickname;
    private String profileImageUrl;
    private String createdDate;

    public static QuestionResponse of(User user, Question question) {
        return QuestionResponse.builder()
                .id(question.getId())
                .totalAnswer(question.getTotalAnswer())
                .title(question.getTitle())
                .authorNickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .createdDate(question.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();
    }
}
