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

    public static UserArticlesResponse of(int page, int size, List<Article> articles) {
        List<ArticlePreviewRequest> articleList = ArticlePreviewRequest.FromArticle(articles);
        return new UserArticlesResponse(page, size, articleList);
    }

    public static UserArticlesResponse of(List<ArticleDocument> documents) {
        List<ArticlePreviewRequest> articleList = ArticlePreviewRequest.FromArticleDocument(documents);
        // TODO : 무한 스크롤 구현시 page, size 값 변수로 변경
        return new UserArticlesResponse(1, 10, articleList);
    }
}
