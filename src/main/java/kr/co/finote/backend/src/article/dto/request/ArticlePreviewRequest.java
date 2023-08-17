package kr.co.finote.backend.src.article.dto.request;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import kr.co.finote.backend.src.article.document.ArticleDocument;
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
    private int reply;
    private String authorNickname;
    private String date;

    public static ArticlePreviewRequest of(Article article) {
        return ArticlePreviewRequest.builder()
                .id(article.getId())
                .title(article.getTitle())
                .body(article.getBody())
                .totalLike(article.getTotalLike())
                .reply(article.getReply())
                .authorNickname(article.getUser().getNickname())
                .date(article.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .build();
    }

    public static ArticlePreviewRequest of(ArticleDocument document) {
        return ArticlePreviewRequest.builder()
                .id(document.getArticleId())
                .title(document.getTitle())
                .body(document.getBody())
                .totalLike(document.getTotalLike())
                .reply(document.getReply())
                .authorNickname(document.getAuthorNickName())
                .date(document.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .build();
    }

    public static List<ArticlePreviewRequest> FromArticle(List<Article> list) {
        List<ArticlePreviewRequest> requests = new ArrayList<>();
        for (Article article : list) {
            requests.add(ArticlePreviewRequest.of(article));
        }

        return requests;
    }

    public static List<ArticlePreviewRequest> FromArticleDocument(List<ArticleDocument> documents) {
        List<ArticlePreviewRequest> requests = new ArrayList<>();
        for (ArticleDocument document : documents) {
            requests.add(ArticlePreviewRequest.of(document));
        }
        return requests;
    }
}
