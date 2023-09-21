package kr.co.finote.backend.src.article.dto.response;

import java.time.format.DateTimeFormatter;
import kr.co.finote.backend.src.article.domain.Reply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReplyResponse {

    private Long id;
    private String nickname;
    private String profileImageUrl;
    private String content;
    private String createdDate;
    private Boolean isMine;

    public static ReplyResponse of(Reply reply) {
        return ReplyResponse.builder()
                .id(reply.getId())
                .nickname(reply.getUser().getNickname())
                .profileImageUrl(reply.getUser().getProfileImageUrl())
                .content(reply.getContent())
                .createdDate(
                        reply.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .isMine(reply.isMine())
                .build();
    }
}
