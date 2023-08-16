package kr.co.finote.backend.src.user.dto.request;

import java.util.ArrayList;
import java.util.List;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.dto.request.ArticlePreviewRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserArticlesRequest {
    private int page;
    private int size;

    private List<ArticlePreviewRequest> articleList = new ArrayList<>();

    public static UserArticlesRequest of(int page, int size, List<Article> articles) {
        List<ArticlePreviewRequest> articleList = ArticlePreviewRequest.getList(articles);
        return new UserArticlesRequest(page, size, articleList);
    }
}
