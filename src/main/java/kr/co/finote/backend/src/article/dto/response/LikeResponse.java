package kr.co.finote.backend.src.article.dto.response;

import kr.co.finote.backend.src.article.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class LikeResponse {

    private Long articleId;
    private int totalLike;

    public static LikeResponse of(Article article) {
        return LikeResponse.builder()
                .articleId(article.getId())
                .totalLike(article.getTotalLike())
                .build();
    }
}
