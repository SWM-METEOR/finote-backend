package kr.co.finote.backend.src.common.dto.response;

import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedResponse {

    private Long id;

    private String profileImageUrl;
    private String title;
    private String nickname;
    private String date;

    private int like;
    private int reply;

    public static FeedResponse of(Article article, User user) {
        return FeedResponse.builder()
                .id(article.getId())
                .profileImageUrl(user.getProfileImageUrl())
                .title(article.getTitle())
                .nickname(user.getNickname())
                .date(article.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .like(article.getTotalLike())
                .reply(article.getTotalReply())
                .build();
    }
}
