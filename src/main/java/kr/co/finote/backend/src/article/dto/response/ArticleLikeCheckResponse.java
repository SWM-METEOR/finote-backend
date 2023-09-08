package kr.co.finote.backend.src.article.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ArticleLikeCheckResponse {
    private boolean isLiked;

    public static ArticleLikeCheckResponse createArticleLikeCheckResponse(boolean isLiked) {
        return ArticleLikeCheckResponse.builder().isLiked(isLiked).build();
    }
}
