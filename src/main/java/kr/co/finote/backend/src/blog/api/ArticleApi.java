package kr.co.finote.backend.src.blog.api;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import kr.co.finote.backend.src.blog.domain.Article;
import kr.co.finote.backend.src.blog.dto.request.ArticleRequest;
import kr.co.finote.backend.src.blog.dto.response.ArticleResponse;
import kr.co.finote.backend.src.blog.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleApi {

    private final ArticleService articleService;

    @PostMapping
    public Map<String, String> postArticles(@RequestBody @Valid ArticleRequest articleRequest) {
        Map<String, String> map = new HashMap<>();
        map.put("articleId", articleService.save(articleRequest));
        return map;
    }

    @GetMapping("/{articleId}")
    public ArticleResponse getArticle(@PathVariable String articleId) {
        Article article = articleService.findById(articleId);
        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .body(article.getTitle())
                //                .authorId(article.getUser().getId())   // TODO user 연결 후 주석 해제
                //                .authorNickname(article.getUser().getNickName())
                //                .profileImageUrl(article.getUser().getProfileImageUrl())
                .createDate(article.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();
    }
}
