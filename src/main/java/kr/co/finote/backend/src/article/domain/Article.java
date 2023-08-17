package kr.co.finote.backend.src.article.domain;

import javax.persistence.*;
import kr.co.finote.backend.global.entity.BaseEntity;
import kr.co.finote.backend.src.article.dto.request.ArticleRequest;
import kr.co.finote.backend.src.user.domain.User;
import lombok.*;

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

    private String title;

    private String body;

    private int totalLike;
    private int reply;

    public static Article createArticle(ArticleRequest articleRequest, User user) {
        return Article.builder()
                .title(articleRequest.getTitle())
                .body(articleRequest.getBody())
                .user(user)
                .build();
    }
}
