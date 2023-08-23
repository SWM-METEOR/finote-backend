package kr.co.finote.backend.src.article.utils;

import java.util.ArrayList;
import java.util.List;
import kr.co.finote.backend.global.utils.StringUtils;
import kr.co.finote.backend.src.article.document.ArticleDocument;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.dto.response.ArticlePreviewResponse;

public class ArticlePreviewUtils {

    public static List<ArticlePreviewResponse> ToArticlesPreivewResponses(List<?> list) {

        List<ArticlePreviewResponse> articlePreviewResponseList = new ArrayList<>();
        for (Object object : list) {
            if (object instanceof Article) {
                Article article = (Article) object;
                String previewBody = StringUtils.markdownToPreviewText(article.getBody());
                articlePreviewResponseList.add(ArticlePreviewResponse.of(article, previewBody));
            } else if (object instanceof ArticleDocument) {
                ArticleDocument document = (ArticleDocument) object;
                String previewBody = StringUtils.markdownToPreviewText(document.getBody());
                articlePreviewResponseList.add(ArticlePreviewResponse.of(document, previewBody));
            }
        }

        return articlePreviewResponseList;
    }
}
