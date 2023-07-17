package kr.co.finote.backend.src.article.api;

import io.swagger.v3.oas.annotations.Operation;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.dto.request.ArticleRequest;
import kr.co.finote.backend.src.article.dto.response.ArticleResponse;
import kr.co.finote.backend.src.article.service.ArticleService;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleApi {

    private final ArticleService articleService;

    @Operation(summary = "블로그 글 작성")
    @PostMapping
    public Map<String, String> postArticles(@RequestBody @Valid ArticleRequest articleRequest) {
        Map<String, String> map = new HashMap<>();
        map.put("articleId", articleService.save(articleRequest));
        return map;
    }

    @Operation(summary = "블로그 글 조회")
    @GetMapping("/{articleId}")
    public ArticleResponse getArticle(@PathVariable String articleId, HttpSession httpSession) {
        User loginUser = (User) httpSession.getAttribute(SessionUtils.LOGIN_USER);
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
