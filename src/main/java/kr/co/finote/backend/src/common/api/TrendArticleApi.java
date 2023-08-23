package kr.co.finote.backend.src.common.api;

import kr.co.finote.backend.src.article.dto.response.ArticlePreviewListResponse;
import kr.co.finote.backend.src.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class TrendArticleApi {

    private final ArticleService articleService;

    @RequestMapping("/trend-articles")
    public ArticlePreviewListResponse trendArticles(
            @RequestParam int page, @RequestParam(required = false, defaultValue = "30") int size) {
        return articleService.trendArticles(page, size);
    }
}
