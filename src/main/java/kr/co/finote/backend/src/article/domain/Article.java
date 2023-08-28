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
    private int reply;

    @ColumnDefault("")
    @Column(nullable = false)
    private String thumbnail;

    public static Article createArticle(ArticleRequest articleRequest, User user) {
        return Article.builder()
                .title(articleRequest.getTitle())
                .body(articleRequest.getBody())
                .user(user)
                .build();
    }
}
