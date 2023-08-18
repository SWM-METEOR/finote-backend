package kr.co.finote.backend.src.user.dto.response;

import java.util.ArrayList;
import java.util.List;
import kr.co.finote.backend.src.article.document.ArticleDocument;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.dto.request.ArticlePreviewRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserArticlesResponse {
    private int page;
    private int size;

    private List<ArticlePreviewRequest> articleList = new ArrayList<>();

    public static UserArticlesResponse fromArticles(int page, int size, List<Article> articles) {
        List<ArticlePreviewRequest> articleList = ArticlePreviewRequest.FromArticle(articles);
        return new UserArticlesResponse(page, size, articleList);
    }

    public static UserArticlesResponse fromDocuments(
            int page, int size, List<ArticleDocument> documents) {
        List<ArticlePreviewRequest> articleList = ArticlePreviewRequest.FromArticleDocument(documents);
        return new UserArticlesResponse(page, size, articleList);
    }
}
