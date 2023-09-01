package kr.co.finote.backend.src.article.dto.response;

import java.time.format.DateTimeFormatter;
import kr.co.finote.backend.src.article.domain.Article;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArticleResponse {

    private Long id;
    private String title;
    private String body;
    private String createDate;
    private String authorId;
    private String authorNickname;
    private String profileImageUrl;
    private Boolean isLiked;

    public static ArticleResponse of(Article article, boolean isLiked) {
        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .body(article.getBody())
                .authorId(article.getUser().getId())
                .authorNickname(article.getUser().getNickname())
                .profileImageUrl(article.getUser().getProfileImageUrl())
                .createDate(article.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .isLiked(isLiked)
                .build();
    }
}
