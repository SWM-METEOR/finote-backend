package kr.co.finote.backend.src.qna.dto.response;

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
public class PostQuestionResponse {

    private Long id;
    private String nickname;
    private String title;

    public static PostQuestionResponse of(User user, Question question) {
        return PostQuestionResponse.builder()
                .id(question.getId())
                .nickname(user.getNickname())
                .title(question.getTitle())
                .build();
    }
}
