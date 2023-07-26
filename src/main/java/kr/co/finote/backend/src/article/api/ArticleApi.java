package kr.co.finote.backend.src.article.api;

import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.dto.request.ArticleRequest;
import kr.co.finote.backend.src.article.dto.response.ArticleResponse;
import kr.co.finote.backend.src.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleApi {

    private final ArticleService articleService;

    @Operation(summary = "블로그 글 작성")
    @PostMapping
    public Map<String, Long> postArticles(
            @RequestBody @Valid ArticleRequest articleRequest, HttpSession httpSession) {
        // TODO 유저 로그인 후 session 검증
        //        User loginUser = (User) httpSession.getAttribute(SessionUtils.LOGIN_USER);
        Map<String, Long> map = new HashMap<>();
        //        map.put("articleId", articleService.save(articleRequest, loginUser));
        map.put("articleId", articleService.save(articleRequest));
        return map;
    }

    @Operation(summary = "블로그 글 조회")
    @GetMapping("/{articleId}")
    public ArticleResponse getArticle(@PathVariable Long articleId, HttpSession httpSession) {
        Article article = articleService.findById(articleId);
        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .body(article.getTitle())
                .authorId("author")
                .authorNickname("nickname")
                .profileImageUrl("")
                .createDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();
    }
}
