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
    private String thumbnail;
    private String createDate;
    private String authorId;
    private String authorNickname;
    private String profileImageUrl;

    public static ArticleResponse of(Article article) {
        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .body(article.getBody())
                .thumbnail(article.getThumbnail() == null ? "" : article.getThumbnail())
                .authorId(article.getUser().getId())
                .authorNickname(article.getUser().getNickname())
                .profileImageUrl(article.getUser().getProfileImageUrl())
                .createDate(article.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();
    }
}
