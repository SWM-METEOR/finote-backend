package kr.co.finote.backend.src.article.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import kr.co.finote.backend.global.annotation.Login;
import kr.co.finote.backend.global.annotation.LoginOptional;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.dto.request.AiSearchRequest;
import kr.co.finote.backend.src.article.dto.request.ArticleRequest;
import kr.co.finote.backend.src.article.dto.request.DragArticleRequest;
import kr.co.finote.backend.src.article.dto.response.*;
import kr.co.finote.backend.src.article.service.AiSearchService;
import kr.co.finote.backend.src.article.service.ArticleService;
import kr.co.finote.backend.src.user.domain.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Article API", description = "블로그 글 API")
public class ArticleApi {

    ArticleService articleService;
    AiSearchService aiSearchService;
    static final String ARTICLE_ID_PATH_VARIABLE = "article-id";

    @Operation(summary = "블로그 글 작성")
    @PostMapping("/write")
    public PostArticleResponse postArticles(
            @Login User loginUser, @RequestBody @Valid ArticleRequest request)
            throws JsonProcessingException {
        return articleService.save(request, loginUser);
    }

    @Operation(summary = "블로그 id로 글 조회")
    @GetMapping("/{articleId}")
    public ArticleResponse getArticle(@LoginOptional User user, @PathVariable Long articleId) {
        return articleService.lookupById(user, articleId);
    }

    @Operation(summary = "블로그 작성자 닉네임, 글 제목으로 조회")
    @GetMapping("/{nickname}/{title}")
    public ArticleResponse getArticleByNicknameAndTitle(
            @PathVariable String nickname, @PathVariable String title) {
        return articleService.lookupByNicknameAndTitle(nickname, title);
    }

    @Operation(summary = "블로그 글 수정")
    @PostMapping("/edit/{article-id}")
    public PostArticleResponse editArticle(
            @Login User loginUser,
            @PathVariable(ARTICLE_ID_PATH_VARIABLE) Long articleId,
            @RequestBody @Valid ArticleRequest request) {
        return articleService.editArticle(loginUser, articleId, request);
    }

    @Operation(summary = "블로그 글 삭제")
    @PostMapping("/delete/{article-id}")
    public void deleteArticle(
            @Login User loginUser, @PathVariable(ARTICLE_ID_PATH_VARIABLE) Long articleId) {
        articleService.deleteArticle(loginUser, articleId);
    }

    @Operation(summary = "스마트 드래그 - AI 검색")
    @PostMapping("/ai-search")
    public AiSearchResponse AiSearch(@RequestBody @Valid AiSearchRequest request) {
        return aiSearchService.getResponse(request);
    }

    @Operation(summary = "스마트 드래그 - 관련 아티클 기능")
    @PostMapping("/drag-related")
    public ArticlePreviewListResponse dragRelatedArticle(
            @RequestBody DragArticleRequest request,
            @RequestParam int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return articleService.getDragRelatedArticle(page, size, request);
    }

    @Operation(summary = "글 전체와 연관된 키워드별 추천글")
    @GetMapping("/related/{nickname}/{title}")
    public List<RelatedArticleResponse> getRelatedArticle(
            @PathVariable String nickname, @PathVariable String title) {
        return articleService.getRelatedArticle(nickname, title);
    }

    // TODO 블로그 글 삭제 시 article_keyword 데이터도 같이 삭제(soft delete)

    @Operation(summary = "유저의 작성 글 모두 가져오기", description = "무한 스크롤 10개씩 요청 받아 대응")
    @GetMapping("/{nickname}/all")
    public ArticlePreviewListResponse articlesAll(
            @PathVariable String nickname,
            @RequestParam int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return articleService.articlesAll(nickname, page, size);
    }

    /* 글 좋아요 관련 API */
    @Operation(summary = "닉네임/제목 기반 유저의 블로그 글 좋아요", description = "이미 좋아요 되어있다면 좋아요 수 증가하지 않음")
    @PostMapping("/like/{nickname}/{title}")
    public LikeResponse postLikeByNicknameAndTitle(
            @Login User loginUser, @PathVariable String nickname, @PathVariable String title) {
        Article article = articleService.findByNicknameAndTitle(nickname, title);
        return articleService.postLikeByNicknameAndTitle(loginUser, article);
    }

    @Operation(summary = "독자의 글에 대한 좋아요 여부 확인")
    @GetMapping("/check-like/{nickname}/{title}")
    public ArticleLikeCheckResponse checkLike(
            @LoginOptional User user, @PathVariable String nickname, @PathVariable String title) {
        return articleService.checkLike(user, nickname, title);
    }

    @Operation(summary = "글의 총 좋아요 수")
    @GetMapping("/total-like/{nickname}/{title}")
    public ArticleTotalLikeResponse totalLike(
            @PathVariable String nickname, @PathVariable String title) {
        return articleService.totalLike(nickname, title);
    }
}
