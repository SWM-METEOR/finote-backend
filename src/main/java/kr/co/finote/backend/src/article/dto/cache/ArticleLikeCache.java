package kr.co.finote.backend.src.article.dto.cache;

import kr.co.finote.backend.src.article.domain.ArticleLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ArticleLikeCache {

    private String userId;
    private Long articleId;
    private Boolean isDeleted;

    public static ArticleLikeCache of(ArticleLike articleLike) {
        return ArticleLikeCache.builder()
                .userId(articleLike.getUser().getId())
                .articleId(articleLike.getArticle().getId())
                .isDeleted(articleLike.getIsDeleted())
                .build();
    }
}
