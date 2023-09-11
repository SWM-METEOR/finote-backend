package kr.co.finote.backend.src.article.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArticleTotalLikeResponse {

    private int totalLike;

    public static ArticleTotalLikeResponse createArticleTotalLikeResponse(int totalLike) {
        return ArticleTotalLikeResponse.builder()
                .totalLike(totalLike)
                .build();
    }
}
