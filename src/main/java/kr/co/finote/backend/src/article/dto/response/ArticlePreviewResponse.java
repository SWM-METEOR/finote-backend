package kr.co.finote.backend.src.article.dto.response;

import java.util.ArrayList;
import java.util.List;
import kr.co.finote.backend.src.article.dto.request.ArticlePreviewRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticlePreviewResponse {
    private int page;
    private int size;

    private List<ArticlePreviewRequest> articleList = new ArrayList<>();

    public static ArticlePreviewResponse of(int page, int size, List<?> list) {
        List<ArticlePreviewRequest> articleList = ArticlePreviewRequest.of(list);
        return new ArticlePreviewResponse(page, size, articleList);
    }
}
