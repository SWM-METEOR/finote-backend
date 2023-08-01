package kr.co.finote.backend.src.article.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostArticleResponse {

    private Long articleId;

    public static PostArticleResponse createPostArticleResponse(Long articleId) {
        return new PostArticleResponse(articleId);
    }
}
