package kr.co.finote.backend.src.article.document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.Id;
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
    private int reply;

    private String authorNickName;
    private String createdDate;

    private String thumbnail;

    public static ArticleDocument createDocument(Long articleId, ArticleRequest request, User user) {
        return ArticleDocument.builder()
                .articleId(articleId)
                .title(request.getTitle())
                .body(request.getBody())
                .totalLike(0)
                .reply(0)
                .authorNickName(user.getNickname())
                .createdDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .build();
    }
}
