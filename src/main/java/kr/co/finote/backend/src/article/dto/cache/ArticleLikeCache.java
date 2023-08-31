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

    private String user_id;
    private Long article_id;
    private Boolean isDeleted;

    public static ArticleLikeCache of(ArticleLike articleLike) {
        return ArticleLikeCache.builder()
                .user_id(articleLike.getUser().getId())
                .article_id(articleLike.getArticle().getId())
                .isDeleted(articleLike.getIsDeleted())
                .build();
    }
}
