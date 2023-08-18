package kr.co.finote.backend.src.article.api;

import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import kr.co.finote.backend.global.annotation.Login;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.dto.request.ArticleRequest;
import kr.co.finote.backend.src.article.dto.request.dragArticleRequest;
import kr.co.finote.backend.src.article.dto.response.ArticleResponse;
import kr.co.finote.backend.src.article.dto.response.PostArticleResponse;
import kr.co.finote.backend.src.article.service.ArticleService;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.dto.response.UserArticlesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleApi {

    private final ArticleService articleService;

    @Operation(summary = "블로그 글 작성")
    @PostMapping
    public PostArticleResponse postArticles(
            @Login User loginUser, @RequestBody @Valid ArticleRequest request) {

        Long articleId = articleService.save(request, loginUser);
        articleService.saveDocument(articleId, request, loginUser);
        return PostArticleResponse.createPostArticleResponse(articleId);
    }

    @Operation(summary = "블로그 글 조회")
    @GetMapping("/{articleId}")
    public ArticleResponse getArticle(@PathVariable Long articleId) {
        Article article = articleService.findById(articleId);
        return ArticleResponse.of(article);
    }

    @Operation(summary = "스마트 드래그 - 관련 아티클 기능")
    @PostMapping("/drag-related")
    // TODO : 테스트 후 무한 스크롤 대응 방법 고민 (ElasticSearchRepository에서)
    public UserArticlesResponse dragRelatedArticle(
            @RequestBody dragArticleRequest request,
            @RequestParam int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return articleService.getDragRelatedArticle(page, size, request);
    }
}
