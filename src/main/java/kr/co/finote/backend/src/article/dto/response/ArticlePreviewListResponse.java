package kr.co.finote.backend.src.article.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticlePreviewListResponse {
    private int page;
    private int size;

    private List<ArticlePreviewResponse> articleList;

    public static ArticlePreviewListResponse of(
            int page, int size, List<ArticlePreviewResponse> articleList) {
        return new ArticlePreviewListResponse(page, size, articleList);
    }
}
