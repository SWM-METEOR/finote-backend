package kr.co.finote.backend.src.article.dto.request;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import kr.co.finote.backend.src.article.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ArticlePreviewRequest {
    private Long id;
    private String title;
    private String body;
    private int totalLike;
    private int comment;
    private String authorNickname;
    private String date;

    public static ArticlePreviewRequest of(Article article) {
        return ArticlePreviewRequest.builder()
                .id(article.getId())
                .title(article.getTitle())
                .body(article.getBody())
                .totalLike(article.getTotalLike())
                .comment(article.getReply())
                .authorNickname(article.getUser().getNickname())
                .date(article.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .build();
    }

    public static List<ArticlePreviewRequest> getList(List<Article> list) {
        List<ArticlePreviewRequest> requests = new ArrayList<>();
        for (Article article : list) {
            requests.add(ArticlePreviewRequest.of(article));
        }

        return requests;
    }
}
