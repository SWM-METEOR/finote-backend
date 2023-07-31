package kr.co.finote.backend.src.article.dto.response;

import java.time.LocalDateTime;
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

    public static ArticleResponse of(Article article) {
        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .body(article.getBody())
                .authorId("author")
                .authorNickname("유리")
                .profileImageUrl("")
                .createDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();
    }
}
