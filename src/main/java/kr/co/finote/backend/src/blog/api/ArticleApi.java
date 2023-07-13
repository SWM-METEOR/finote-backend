package kr.co.finote.backend.src.blog.api;

import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import kr.co.finote.backend.src.blog.dto.request.ArticleRequest;
import kr.co.finote.backend.src.blog.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleApi {

    private final ArticleService articleService;

    @PostMapping
    public Map<String, Long> postArticles(@RequestBody @Valid ArticleRequest articleRequest) {
        Map<String, Long> map = new HashMap<>();
        map.put("articleId", articleService.save(articleRequest));
        return map;
    }
}
