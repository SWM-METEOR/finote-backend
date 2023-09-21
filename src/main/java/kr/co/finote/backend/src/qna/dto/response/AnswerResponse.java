package kr.co.finote.backend.src.qna.dto.response;

import java.time.format.DateTimeFormatter;
import kr.co.finote.backend.src.qna.domain.Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerResponse {

    private Long id;
    private String profileImage;
    private String nickname;
    private String createdDate;

    private int totalLike;
    private int totalUnlike;

    public static AnswerResponse of(Answer answer) {
        return AnswerResponse.builder()
                .id(answer.getId())
                .profileImage(answer.getUser().getProfileImageUrl())
                .nickname(answer.getUser().getNickname())
                .createdDate(
                        answer.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .totalLike(answer.getTotalLike())
                .totalUnlike(answer.getTotalUnlike())
                .build();
    }
}
