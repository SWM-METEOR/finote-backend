package kr.co.finote.backend.src.article.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RelatedArticleResponse {

    private String keyword;
    private List<ArticlePreviewResponse> articleList;

    public static RelatedArticleResponse createRelatedArticleResponse(
            String keyword, List<ArticlePreviewResponse> articleList) {
        return new RelatedArticleResponse(keyword, articleList);
    }
}
