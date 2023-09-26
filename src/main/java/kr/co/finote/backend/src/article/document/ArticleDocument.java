package kr.co.finote.backend.src.article.document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.Id;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.dto.request.ArticleRequest;
import kr.co.finote.backend.src.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "article")
@Getter
@AllArgsConstructor
@Builder
public class ArticleDocument {

    @Id private String id;

    private Long articleId;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String title;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String body;

    private int totalLike;
    private int totalReply;
    private String thumbnail;
    private String createdDate;
    private String authorNickname;
    private String profileImageUrl;

    public static ArticleDocument createDocument(Long articleId, ArticleRequest request, User user) {
        String thumbnail1 = request.getThumbnail();
        if (thumbnail1 == null || thumbnail1.equals("")) {
            thumbnail1 =
                    "https://finote-image-bucket.s3.ap-northeast-2.amazonaws.com/finote_logo.png"; // deafult
            // 로고
        }
        return ArticleDocument.builder()
                .articleId(articleId)
                .title(request.getTitle().trim())
                .body(request.getBody())
                .thumbnail(thumbnail1)
                .totalLike(0)
                .totalReply(0)
                .authorNickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .createdDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .build();
    }

    public void editDocument(Article article) {
        this.title = article.getTitle().trim();
        this.body = article.getBody();
        this.thumbnail = article.getThumbnail();
    }

    public void editTotalLike(int totalLike) {
        this.totalLike = totalLike;
        this.totalLike = Math.max(0, this.totalLike);
    }

    public void editTotalReply(int totalReply) {
        this.totalReply = totalReply;
        this.totalReply = Math.max(0, this.totalReply);
    }

    public void editByUser(User user) {
        this.authorNickname = user.getNickname();
        this.profileImageUrl = user.getProfileImageUrl();
    }
}
