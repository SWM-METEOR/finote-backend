package kr.co.finote.backend.src.article.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.InvalidInputException;
import kr.co.finote.backend.global.exception.NotFoundException;
import kr.co.finote.backend.src.article.document.ArticleDocument;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.domain.ArticleLike;
import kr.co.finote.backend.src.article.dto.cache.ArticleLikeCache;
import kr.co.finote.backend.src.article.dto.request.ArticleRequest;
import kr.co.finote.backend.src.article.dto.response.*;
import kr.co.finote.backend.src.article.repository.ArticleEsRepository;
import kr.co.finote.backend.src.article.repository.ArticleRepository;
import kr.co.finote.backend.src.user.domain.Role;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings({"PMD.AvoidDuplicateLiterals", "PMD.JUnitTestContainsTooManyAsserts"})
class ArticleServiceTest {

    @InjectMocks private ArticleService articleService;
    @Mock private ArticleRepository articleRepository;
    @Mock private ArticleEsRepository articleEsRepository;
    @Mock private KeywordService keywordService;
    @Mock private ArticleKeywordService articleKeywordService;
    @Mock private ElasticService elasticService;
    @Mock private UserService userService;
    @Mock private ArticleLikeService articleLikeService;
    @Mock private ArticleViewCacheService articleViewCacheService;
    @Mock private ArticleLikeCacheService articleLikeCacheService;

    @Test
    @DisplayName("save Success")
    void save() throws JsonProcessingException {
        // given
        ArticleRequest articleRequest = new ArticleRequest("title", "body", "thumbnail");
        User user = new User();

        when(articleRepository.findByUserAndTitleAndIsDeleted(user, "title", false))
                .thenReturn(Optional.empty());
        when(articleRepository.save(ArgumentMatchers.any(Article.class)))
                .thenReturn(new Article(1L, user, "title", "body", 0, 0, 0, ""));
        when(articleEsRepository.save(ArgumentMatchers.any()))
                .thenReturn(
                        new ArticleDocument("id", 1L, "title", "body", 0, 0, "author", "2023-01-01", ""));

        // when
        PostArticleResponse postArticleResponse = articleService.save(articleRequest, user);

        // then
        assertThat(postArticleResponse.getArticleId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("save Fail - 이미 존재하는 제목")
    void saveFail() {
        // given
        ArticleRequest articleRequest = new ArticleRequest("title", "body", "thumbnail");
        User user = new User();

        when(articleRepository.findByUserAndTitleAndIsDeleted(user, "title", false))
                .thenReturn(Optional.of(new Article()));

        // when
        InvalidInputException invalidInputException =
                assertThrows(InvalidInputException.class, () -> articleService.save(articleRequest, user));

        // then
        Assertions.assertThat(invalidInputException.getResponseCode())
                .isEqualTo(ResponseCode.ARTICLE_ALREADY_EXIST);
    }

    @Test
    @DisplayName("findById Success")
    void findById() {
        // given
        Article article = new Article(1L, new User(), "title", "body", 0, 0, 0, "");
        when(articleRepository.findByIdAndIsDeleted(1L, false)).thenReturn(Optional.of(article));

        // when
        ArticleResponse articleResponse = articleService.findById(1L);

        // then
        assertThat(articleResponse.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("findById Fail - 존재하지 않는 게시글")
    void findByIdFail() {
        // given
        when(articleRepository.findByIdAndIsDeleted(1L, false)).thenReturn(Optional.empty());

        // when
        NotFoundException notFoundException =
                assertThrows(NotFoundException.class, () -> articleService.findById(1L));

        // then
        assertThat(notFoundException.getResponseCode()).isEqualTo(ResponseCode.ARTICLE_NOT_FOUND);
    }

    @Test
    @DisplayName("findByNicknameTitle Success")
    void findByNicknameAndTitleSuccess() {
        // given
        User user = new User();
        Article article = new Article(1L, new User(), "title", "body", 0, 0, 0, "");
        when(articleRepository.findByUserAndTitleAndIsDeleted(user, "title", false))
                .thenReturn(Optional.of(article));
        when(userService.findByNickname("nickname")).thenReturn(user);

        // when
        Article findArticle = articleService.findByNicknameAndTitle("nickname", "title");

        // then
        assertThat(findArticle.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("findByNicknameTitle Fail - 존재하지 않는 게시글")
    void findByNicknameAndTitleFail() {
        // given
        User user = new User();
        when(articleRepository.findByUserAndTitleAndIsDeleted(user, "title", false))
                .thenReturn(Optional.empty());
        when(userService.findByNickname("nickname")).thenReturn(user);

        // when
        NotFoundException notFoundException =
                assertThrows(
                        NotFoundException.class,
                        () -> articleService.findByNicknameAndTitle("nickname", "title"));

        // then
        assertThat(notFoundException.getResponseCode()).isEqualTo(ResponseCode.ARTICLE_NOT_FOUND);
    }

    @Test
    @DisplayName("lookUpByNicknameAndTitle Success")
    void lookupByNicknameAndTitle() {
        // given
        User user = new User();
        Article article = new Article(1L, user, "title", "body", 0, 0, 0, "");
        when(userService.findByNickname("nickname")).thenReturn(user);
        when(articleRepository.findByUserAndTitleAndIsDeleted(user, "title", false))
                .thenReturn(Optional.of(article));
        HttpServletRequest request = mock(HttpServletRequest.class);

        // 모킹된 HttpServletRequest 객체를 사용하여 원하는 동작을 정의
        when(request.getHeader("X-FORWARDED-FOR")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("localhost");

        when(articleViewCacheService.hasViewCache("localhost-1")).thenReturn(true);

        // when
        ArticleResponse articleResponse =
                articleService.lookupByNicknameAndTitle("nickname", "title", request);

        // then
        assertThat(articleResponse.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("lookUpByNicknameAndTitle Fail - 존재하지 않는 게시글")
    void lookupByNicknameAndTitleFail() {
        // given
        User user = new User();
        when(userService.findByNickname("nickname")).thenReturn(user);
        when(articleRepository.findByUserAndTitleAndIsDeleted(user, "title", false))
                .thenReturn(Optional.empty());
        HttpServletRequest request = mock(HttpServletRequest.class);

        // when
        NotFoundException notFoundException =
                assertThrows(
                        NotFoundException.class,
                        () -> articleService.lookupByNicknameAndTitle("nickname", "title", request));

        // then
        assertThat(notFoundException.getResponseCode()).isEqualTo(ResponseCode.ARTICLE_NOT_FOUND);
    }

    @Test
    @DisplayName("updateViewOrNot - 조회수 업데이트 O")
    void updateView() {
        // given
        Article article = new Article(1L, new User(), "title", "body", 0, 0, 0, "");
        HttpServletRequest request = mock(HttpServletRequest.class);

        // 모킹된 HttpServletRequest 객체를 사용하여 원하는 동작을 정의
        when(request.getHeader("X-FORWARDED-FOR")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("localhost");
        when(articleViewCacheService.hasViewCache("localhost-1")).thenReturn(false);

        // when
        articleService.updateViewOrNot(request, article);

        // then
        verify(articleViewCacheService, times(1)).cacheView("localhost-1");
    }

    @Test
    @DisplayName("updateViewOrNot - 조회수 업데이트 X")
    void notUpdateView() {
        // given
        Article article = new Article(1L, new User(), "title", "body", 0, 0, 0, "");
        HttpServletRequest request = mock(HttpServletRequest.class);

        // 모킹된 HttpServletRequest 객체를 사용하여 원하는 동작을 정의
        when(request.getHeader("X-FORWARDED-FOR")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("localhost");
        when(articleViewCacheService.hasViewCache("localhost-1")).thenReturn(true);

        // when
        articleService.updateViewOrNot(request, article);

        // then
        verify(articleViewCacheService, times(0)).cacheView("localhost-1");
    }

    @Test
    @DisplayName("trendArticles")
    void trendArticles() {
        // given
        int pageNum = 1;
        int size = 10;
        List<Article> articles = new ArrayList<>();
        articles.add(new Article(1L, new User(), "title", "body", 0, 0, 0, ""));
        articles.add(new Article(2L, new User(), "title", "body", 0, 0, 0, ""));
        articles.add(new Article(3L, new User(), "title", "body", 0, 0, 0, ""));
        articles.add(new Article(4L, new User(), "title", "body", 0, 0, 0, ""));
        articles.add(new Article(5L, new User(), "title", "body", 0, 0, 0, ""));
        articles.add(new Article(6L, new User(), "title", "body", 0, 0, 0, ""));
        articles.add(new Article(7L, new User(), "title", "body", 0, 0, 0, ""));
        articles.add(new Article(8L, new User(), "title", "body", 0, 0, 0, ""));
        articles.add(new Article(9L, new User(), "title", "body", 0, 0, 0, ""));
        articles.add(new Article(10L, new User(), "title", "body", 0, 0, 0, ""));

        when(articleRepository.findAllByIsDeleted(eq(false), any(Pageable.class)))
                .thenReturn(new PageImpl<>(articles));

        // when
        ArticlePreviewListResponse response = articleService.trendArticles(pageNum, size);

        // then
        assertThat(response.getPage()).isEqualTo(1);
        assertThat(response.getSize()).isEqualTo(10);
        assertThat(response.getArticleList().size()).isEqualTo(10);
        for (int i = 0; i < 10; i++) {
            assertThat(response.getArticleList().get(i).getId()).isEqualTo(i + 1);
        }
    }

    @Test
    @DisplayName("articlesAll")
    void articlesAll() {
        // given
        int pageNum = 1;
        int size = 10;
        User user = new User();
        List<Article> articles = new ArrayList<>();
        articles.add(new Article(1L, new User(), "title", "body", 0, 0, 0, ""));
        articles.add(new Article(2L, new User(), "title", "body", 0, 0, 0, ""));
        articles.add(new Article(3L, new User(), "title", "body", 0, 0, 0, ""));
        articles.add(new Article(4L, new User(), "title", "body", 0, 0, 0, ""));
        articles.add(new Article(5L, new User(), "title", "body", 0, 0, 0, ""));
        articles.add(new Article(6L, new User(), "title", "body", 0, 0, 0, ""));
        articles.add(new Article(7L, new User(), "title", "body", 0, 0, 0, ""));
        articles.add(new Article(8L, new User(), "title", "body", 0, 0, 0, ""));
        articles.add(new Article(9L, new User(), "title", "body", 0, 0, 0, ""));
        articles.add(new Article(10L, new User(), "title", "body", 0, 0, 0, ""));

        when(userService.findByNickname("nickname")).thenReturn(user);
        when(articleRepository.findByUserAndIsDeleted(any(User.class), eq(false), any(Pageable.class)))
                .thenReturn(new PageImpl<>(articles));

        // when
        ArticlePreviewListResponse response = articleService.articlesAll("nickname", pageNum, size);

        // then
        assertThat(response.getPage()).isEqualTo(1);
        assertThat(response.getSize()).isEqualTo(10);
        assertThat(response.getArticleList().size()).isEqualTo(10);
        for (int i = 0; i < 10; i++) {
            assertThat(response.getArticleList().get(i).getId()).isEqualTo(i + 1);
        }
    }

    @Test
    @DisplayName("postLikeByNicknameAndTitle - 캐시 O, 좋아요")
    void postLikeByNicknameAndTitleWithCacheLike() {
        // given
        User user = new User();
        Article article = new Article(1L, user, "title", "body", 0, 0, 0, "");
        ArticleLikeCache articleLikeCache =
                new ArticleLikeCache("user", 1L, true); // 기존에 이미 좋아요했다가 취소한 내역 있음
        ArticleLike articleLike = new ArticleLike(1L, user, article);
        when(articleLikeCacheService.findLikeLog(user, article)).thenReturn(articleLikeCache);
        when(articleLikeService.findByUser(user, article)).thenReturn(Optional.of(articleLike));

        // when
        LikeResponse likeResponse = articleService.postLikeByNicknameAndTitle(user, article);

        // then
        assertThat(likeResponse.getArticleId()).isEqualTo(1L);
        assertThat(likeResponse.getTotalLike()).isEqualTo(1);
        assertThat(article.getTotalLike()).isEqualTo(1);
        assertThat(articleLike.getIsDeleted()).isFalse();
    }

    @Test
    @DisplayName("postLikeByNicknameAndTitle - 캐시 O, 좋아요 취소")
    void postLikeByNicknameAndTitleWithCacheUnLike() {
        User user = new User();
        Article article = new Article(1L, user, "title", "body", 1, 0, 0, "");
        ArticleLikeCache articleLikeCache =
                new ArticleLikeCache("user", 1L, false); // 기존에 이미 좋아요 한 내역 있음
        ArticleLike articleLike = new ArticleLike(1L, user, article);
        when(articleLikeCacheService.findLikeLog(user, article)).thenReturn(articleLikeCache);
        when(articleLikeService.findByUser(user, article)).thenReturn(Optional.of(articleLike));

        // when
        LikeResponse likeResponse = articleService.postLikeByNicknameAndTitle(user, article);

        // then
        assertThat(likeResponse.getArticleId()).isEqualTo(1L);
        assertThat(likeResponse.getTotalLike()).isEqualTo(0);
        assertThat(article.getTotalLike()).isEqualTo(0);
        assertThat(articleLike.getIsDeleted()).isTrue();
    }

    @Test
    @DisplayName("postLikeByNicknameAndTitle - 캐시 X, 좋아요")
    void postLikeByNicknameAndTitle() {
        // given
        User user = new User();
        Article article = new Article(1L, user, "title", "body", 0, 0, 0, "");
        ArticleLike articleLike = new ArticleLike(1L, user, article);
        when(articleLikeCacheService.findLikeLog(user, article)).thenReturn(null);
        when(articleLikeService.findByUser(user, article)).thenReturn(Optional.of(articleLike));

        // when
        LikeResponse likeResponse = articleService.postLikeByNicknameAndTitle(user, article);

        // then
        assertThat(likeResponse.getArticleId()).isEqualTo(1L);
        assertThat(likeResponse.getTotalLike()).isEqualTo(1);
        assertThat(article.getTotalLike()).isEqualTo(1);
        verify(articleLikeService, times(1)).save(user, article);
    }

    @Test
    @DisplayName("editArticle Success")
    void editArticleSuccess() {
        // given
        User writerUser =
                new User(
                        "1",
                        "name",
                        "password",
                        "email",
                        null,
                        null,
                        Role.USER,
                        LocalDateTime.now(),
                        "nickname",
                        null,
                        "blogName",
                        "refreshToken");
        User loginUser =
                new User(
                        "1",
                        "name",
                        "password",
                        "email",
                        null,
                        null,
                        Role.USER,
                        LocalDateTime.now(),
                        "nickname",
                        null,
                        "blogName",
                        "refreshToken");
        Article article = new Article(1L, writerUser, "title", "body", 0, 0, 0, "");
        ArticleRequest articleRequest = new ArticleRequest("new title", "new body", "new thumbnail");
        when(articleRepository.findByIdAndIsDeleted(1L, false)).thenReturn(Optional.of(article));

        // when
        PostArticleResponse postArticleResponse =
                articleService.editArticle(loginUser, 1L, articleRequest);

        // then
        assertThat(article.getTitle()).isEqualTo("new title");
        assertThat(article.getBody()).isEqualTo("new body");
        assertThat(article.getThumbnail()).isEqualTo("new thumbnail");
        assertThat(postArticleResponse.getArticleId()).isEqualTo(1L);
        assertThat(postArticleResponse.getTitle()).isEqualTo("new title");
        assertThat(postArticleResponse.getNickname()).isEqualTo("nickname");
    }

    @Test
    @DisplayName("editArticle Fail - 존재하지 않는 게시글")
    void editArticleFailNotFound() {
        // given
        User loginUser =
                new User(
                        "1",
                        "name",
                        "password",
                        "email",
                        null,
                        null,
                        Role.USER,
                        LocalDateTime.now(),
                        "nickname",
                        null,
                        "blogName",
                        "refreshToken");
        ArticleRequest articleRequest = new ArticleRequest("new title", "new body", "new thumbnail");
        when(articleRepository.findByIdAndIsDeleted(1L, false)).thenReturn(Optional.empty());

        // when
        NotFoundException notFoundException =
                assertThrows(
                        NotFoundException.class,
                        () -> articleService.editArticle(loginUser, 1L, articleRequest));

        // then
        assertThat(notFoundException.getResponseCode()).isEqualTo(ResponseCode.ARTICLE_NOT_FOUND);
    }

    @Test
    @DisplayName("editArticle Fail - 권한이 없는 유저")
    void editArticleFailNotWriter() {
        // given
        User writerUser =
                new User(
                        "1",
                        "name",
                        "password",
                        "writerEmail",
                        null,
                        null,
                        Role.USER,
                        LocalDateTime.now(),
                        "nickname",
                        null,
                        "blogName",
                        "refreshToken");
        User loginUser =
                new User(
                        "1",
                        "name",
                        "password",
                        "email",
                        null,
                        null,
                        Role.USER,
                        LocalDateTime.now(),
                        "nickname",
                        null,
                        "blogName",
                        "refreshToken");
        Article article = new Article(1L, writerUser, "title", "body", 0, 0, 0, "");
        ArticleRequest articleRequest = new ArticleRequest("new title", "new body", "new thumbnail");
        when(articleRepository.findByIdAndIsDeleted(1L, false)).thenReturn(Optional.of(article));

        // when
        InvalidInputException invalidInputException =
                assertThrows(
                        InvalidInputException.class,
                        () -> articleService.editArticle(loginUser, 1L, articleRequest));

        // then
        assertThat(invalidInputException.getResponseCode()).isEqualTo(ResponseCode.ARTICLE_NOT_WRITER);
    }

    @Test
    @DisplayName("deleteArticle Success")
    void deleteArticleSuccess() {
        // given
        User writerUser =
                new User(
                        "1",
                        "name",
                        "password",
                        "email",
                        null,
                        null,
                        Role.USER,
                        LocalDateTime.now(),
                        "nickname",
                        null,
                        "blogName",
                        "refreshToken");
        User loginUser =
                new User(
                        "1",
                        "name",
                        "password",
                        "email",
                        null,
                        null,
                        Role.USER,
                        LocalDateTime.now(),
                        "nickname",
                        null,
                        "blogName",
                        "refreshToken");
        Article article = new Article(1L, writerUser, "title", "body", 0, 0, 0, "");
        when(articleRepository.findByIdAndIsDeleted(1L, false)).thenReturn(Optional.of(article));

        // when
        articleService.deleteArticle(loginUser, 1L);

        // then
        assertThat(article.getIsDeleted()).isTrue();
        verify(articleLikeService, times(1)).deleteAllByArticle(article);
        verify(articleKeywordService, times(1)).deleteArticleKeyword(article);
    }

    @Test
    @DisplayName("deleteArticle Fail - 존재하지 않는 게시글")
    void deleteArticleFailNotFound() {
        // given
        User loginUser =
                new User(
                        "1",
                        "name",
                        "password",
                        "email",
                        null,
                        null,
                        Role.USER,
                        LocalDateTime.now(),
                        "nickname",
                        null,
                        "blogName",
                        "refreshToken");
        when(articleRepository.findByIdAndIsDeleted(1L, false)).thenReturn(Optional.empty());

        // when
        NotFoundException notFoundException =
                assertThrows(NotFoundException.class, () -> articleService.deleteArticle(loginUser, 1L));

        // then
        assertThat(notFoundException.getResponseCode()).isEqualTo(ResponseCode.ARTICLE_NOT_FOUND);
    }

    @Test
    @DisplayName("deleteArticle Fail - 권한이 없는 유저")
    void deleteArticleFailNotWriter() {
        // given
        User writerUser =
                new User(
                        "1",
                        "name",
                        "password",
                        "writerEmail",
                        null,
                        null,
                        Role.USER,
                        LocalDateTime.now(),
                        "nickname",
                        null,
                        "blogName",
                        "refreshToken");
        User loginUser =
                new User(
                        "1",
                        "name",
                        "password",
                        "email",
                        null,
                        null,
                        Role.USER,
                        LocalDateTime.now(),
                        "nickname",
                        null,
                        "blogName",
                        "refreshToken");
        Article article = new Article(1L, writerUser, "title", "body", 0, 0, 0, "");
        when(articleRepository.findByIdAndIsDeleted(1L, false)).thenReturn(Optional.of(article));

        // when
        InvalidInputException invalidInputException =
                assertThrows(
                        InvalidInputException.class, () -> articleService.deleteArticle(loginUser, 1L));

        // then
        assertThat(invalidInputException.getResponseCode()).isEqualTo(ResponseCode.ARTICLE_NOT_WRITER);
    }

    @Test
    @DisplayName("checkLike - 비로그인 유저")
    void checkLikeUnLoginUser() {
        // given
        User loginUser = null;

        // when
        ArticleLikeCheckResponse articleLikeCheckResponse =
                articleService.checkLike(loginUser, "author", "title");

        // then
        assertThat(articleLikeCheckResponse.isLiked()).isFalse();
    }

    @Test
    @DisplayName("checkLike - 좋아요 한 유저")
    void checkLikeLoginUserLiked() {
        // given
        User writer =
                new User(
                        "1",
                        "name",
                        "password",
                        "email",
                        null,
                        null,
                        Role.USER,
                        LocalDateTime.now(),
                        "writer",
                        null,
                        "blogName",
                        "refreshToken");
        User reader =
                new User(
                        "2",
                        "name",
                        "password",
                        "email",
                        null,
                        null,
                        Role.USER,
                        LocalDateTime.now(),
                        "reader",
                        null,
                        "blogName",
                        "refreshToken");
        Article article = new Article(1L, writer, "title", "body", 0, 0, 0, "");
        ArticleLikeCache articleLikeCache = new ArticleLikeCache("2", 1L, false);
        when(userService.findByNickname("nickname")).thenReturn(writer);
        when(articleRepository.findByUserAndTitleAndIsDeleted(writer, "title", false))
                .thenReturn(Optional.of(article));
        when(articleLikeCacheService.findLikeLog(reader, article)).thenReturn(articleLikeCache);

        // when
        ArticleLikeCheckResponse articleLikeCheckResponse =
                articleService.checkLike(reader, "nickname", "title");

        // then
        assertThat(articleLikeCheckResponse.isLiked()).isTrue();
    }

    @Test
    @DisplayName("checkLike - 좋아요 안한 유저")
    void checkLikeLoginUserUnLiked() {
        // given
        User writer =
                new User(
                        "1",
                        "name",
                        "password",
                        "email",
                        null,
                        null,
                        Role.USER,
                        LocalDateTime.now(),
                        "writer",
                        null,
                        "blogName",
                        "refreshToken");
        User reader =
                new User(
                        "2",
                        "name",
                        "password",
                        "email",
                        null,
                        null,
                        Role.USER,
                        LocalDateTime.now(),
                        "reader",
                        null,
                        "blogName",
                        "refreshToken");
        Article article = new Article(1L, writer, "title", "body", 0, 0, 0, "");
        ArticleLikeCache articleLikeCache = new ArticleLikeCache("2", 1L, true);
        when(userService.findByNickname("nickname")).thenReturn(writer);
        when(articleRepository.findByUserAndTitleAndIsDeleted(writer, "title", false))
                .thenReturn(Optional.of(article));
        when(articleLikeCacheService.findLikeLog(reader, article)).thenReturn(articleLikeCache);
        //        when(articleLikeCacheService.findLikeLog(reader, article)).thenReturn(null);

        // when
        ArticleLikeCheckResponse articleLikeCheckResponse =
                articleService.checkLike(reader, "nickname", "title");

        // then
        assertThat(articleLikeCheckResponse.isLiked()).isFalse();
    }

    @Test
    @DisplayName("totalLike")
    void totalLike() {
        // given
        User user = new User();
        Article article = new Article(1L, new User(), "title", "body", 3, 0, 0, "");
        when(userService.findByNickname("nickname")).thenReturn(user);
        when(articleRepository.findByUserAndTitleAndIsDeleted(user, "title", false))
                .thenReturn(Optional.of(article));

        // when
        ArticleTotalLikeResponse articleTotalLikeResponse =
                articleService.totalLike("nickname", "title");

        // then
        assertThat(articleTotalLikeResponse.getTotalLike()).isEqualTo(3);
    }
}
