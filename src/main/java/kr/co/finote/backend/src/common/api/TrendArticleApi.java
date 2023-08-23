package kr.co.finote.backend.src.common.api;

import io.swagger.v3.oas.annotations.Operation;
import kr.co.finote.backend.src.article.dto.response.ArticlePreviewListResponse;
import kr.co.finote.backend.src.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class TrendArticleApi {

    private final ArticleService articleService;

    @Operation(summary = "트렌딩 아티클", description = "글 30개씩 무한 스크롤에 대응하여 결과 제공")
    @GetMapping("/trend-articles")
    public ArticlePreviewListResponse trendArticles(
            @RequestParam int page, @RequestParam(required = false, defaultValue = "30") int size) {
        return articleService.trendArticles(page, size);
    }
}
