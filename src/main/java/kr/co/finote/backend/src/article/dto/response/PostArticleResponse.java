package kr.co.finote.backend.src.article.dto.response;

import kr.co.finote.backend.src.article.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostArticleResponse {

    private Long articleId;
    private String nickname;
    private String title;

    public static PostArticleResponse of(Article article) {
        return new PostArticleResponse(
                article.getId(), article.getUser().getNickname(), article.getTitle());
    }
}
