package kr.co.finote.backend.src.article.domain;

import javax.persistence.*;
import kr.co.finote.backend.global.entity.BaseEntity;
import kr.co.finote.backend.src.article.dto.request.ArticleRequest;
import kr.co.finote.backend.src.user.domain.Category;
import kr.co.finote.backend.src.user.domain.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@NoArgsConstructor
@SuppressWarnings("PMD.UnusedAssignment")
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private String title = "";

    @Column(columnDefinition = "TEXT", nullable = false)
    private String body = "";

    @ColumnDefault("0")
    private int totalLike;

    @ColumnDefault("0")
    private int totalReply;

    @ColumnDefault("0")
    private int totalView;

    @Column(nullable = false, length = 500)
    private String thumbnail = "";

    public Article(
            Long id,
            User user,
            Category category,
            String title,
            String body,
            int totalLike,
            int totalReply,
            int totalView,
            String thumbnail) {
        super();
        this.id = id;
        this.user = user;
        this.category = category;
        this.title = title;
        this.body = body;
        this.totalLike = totalLike;
        this.totalReply = totalReply;
        this.totalView = totalView;
        this.thumbnail = thumbnail;
    }

    public static Article createArticle(ArticleRequest articleRequest, User user) {
        String thumbnail1 = articleRequest.getThumbnail();
        if (thumbnail1 == null || thumbnail1.equals("")) {
            thumbnail1 =
                    "https://finote-image-bucket.s3.ap-northeast-2.amazonaws.com/finote.png"; // deafult
            // 로고
        }
        return Article.builder()
                .title(articleRequest.getTitle().trim())
                .body(articleRequest.getBody())
                .thumbnail(thumbnail1)
                .user(user)
                .build();
    }

    public void editTotalLike(int num) {
        this.totalLike += num;
        this.totalLike = Math.max(0, this.totalLike);
    }

    public void editArticle(ArticleRequest articleRequest) {
        this.title = articleRequest.getTitle().trim();
        this.body = articleRequest.getBody();
        this.thumbnail = articleRequest.getThumbnail();
    }

    public void deleteArticle() {
        this.isDeleted = true;
    }

    public void editTotalView() {
        this.totalView++;
    }

    public void editTotalReply(int num) {
        this.totalReply += num;
        this.totalReply = Math.max(0, totalReply);
    }
}
