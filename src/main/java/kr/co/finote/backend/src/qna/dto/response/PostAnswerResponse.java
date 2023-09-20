package kr.co.finote.backend.src.qna.dto.response;

import java.time.format.DateTimeFormatter;
import kr.co.finote.backend.src.qna.domain.Answer;
import kr.co.finote.backend.src.user.domain.User;
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
    private String profileImage;
    private String nickname;
    private String createdTime;

    public static PostAnswerResponse of(User writer, Answer answer) {
        return PostAnswerResponse.builder()
                .id(answer.getId())
                .profileImage(writer.getProfileImageUrl())
                .nickname(writer.getNickname())
                .createdTime(
                        answer.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
