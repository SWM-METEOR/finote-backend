package kr.co.finote.backend.src.article.domain;

import javax.persistence.*;
import kr.co.finote.backend.global.entity.BaseEntity;
import kr.co.finote.backend.src.article.dto.request.ArticleRequest;
import kr.co.finote.backend.src.user.domain.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    @ColumnDefault("")
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    @ColumnDefault("")
    private String body;

    @ColumnDefault("0")
    private int totalLike;

    @ColumnDefault("0")
    private int totalReply;

    @ColumnDefault("")
    @Column(nullable = false)
    private String thumbnail;

    public static Article createArticle(ArticleRequest articleRequest, User user) {
        String thumbnail1 = articleRequest.getThumbnail();
        if (thumbnail1 == null || thumbnail1.equals("")) {
            thumbnail1 =
                    "https://finote-image-bucket.s3.ap-northeast-2.amazonaws.com/finote_logo.png"; // deafult
            // 로고
        }
        return Article.builder()
                .title(articleRequest.getTitle().trim())
                .body(articleRequest.getBody())
                .thumbnail(thumbnail1)
                .user(user)
                .build();
    }

    public void updateLikeCount(int num) {
        this.totalLike += num;
        totalLike = Math.max(0, totalLike);
    }

    public void editArticle(ArticleRequest articleRequest) {
        this.title = articleRequest.getTitle().trim();
        this.body = articleRequest.getBody();
    }

    public void deleteArticle() {
        this.isDeleted = true;
    }
}
