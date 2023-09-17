package kr.co.finote.backend.src.article.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Optional;
import kr.co.finote.backend.global.exception.InvalidInputException;
import kr.co.finote.backend.global.exception.NotFoundException;
import kr.co.finote.backend.src.article.document.ArticleDocument;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.dto.request.ArticleRequest;
import kr.co.finote.backend.src.article.dto.response.ArticleResponse;
import kr.co.finote.backend.src.article.dto.response.PostArticleResponse;
import kr.co.finote.backend.src.article.repository.ArticleEsRepository;
import kr.co.finote.backend.src.article.repository.ArticleRepository;
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

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks private ArticleService articleService;
    @Mock private ArticleRepository articleRepository;
    @Mock private ArticleEsRepository articleEsRepository;
    @Mock private KeywordService keywordService;
    @Mock private ArticleKeywordService articleKeywordService;
    @Mock private ElasticService elasticService;
    @Mock private UserService userService;

    private static final String TITLE = "title";
    private static final String BODY = "body";
    private static final String THUMBNAIL = "thumbnail";
    private static final String NICKNAME = "nickname";

    @Test
    @DisplayName("게시글 작성 성공")
    void saveSuccess() throws JsonProcessingException {
        // given
        ArticleRequest articleRequest = new ArticleRequest(TITLE, BODY, THUMBNAIL);
        User user = new User();

        when(articleRepository.findByUserAndTitleAndIsDeleted(user, TITLE, false))
                .thenReturn(Optional.empty());
        when(articleRepository.save(ArgumentMatchers.any(Article.class)))
                .thenReturn(new Article(1L, user, TITLE, BODY, 0, 0, 0, ""));
        when(articleEsRepository.save(ArgumentMatchers.any()))
                .thenReturn(new ArticleDocument("id", 1L, TITLE, BODY, 0, 0, "author", "2023-01-01", ""));

        // when
        PostArticleResponse postArticleResponse = articleService.save(articleRequest, user);

        // then
        Assertions.assertThat(postArticleResponse.getArticleId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("게시글 작성 실패 - 이미 존재하는 제목")
    void saveFail() {
        // given
        ArticleRequest articleRequest = new ArticleRequest(TITLE, BODY, THUMBNAIL);
        User user = new User();

        when(articleRepository.findByUserAndTitleAndIsDeleted(user, TITLE, false))
                .thenReturn(Optional.of(new Article()));

        // when
        assertThrows(InvalidInputException.class, () -> articleService.save(articleRequest, user));

        // then
    }

    @Test
    @DisplayName("findById Success")
    void findByIdSuccess() {
        // given
        Article article = new Article(1L, new User(), TITLE, BODY, 0, 0, 0, "");
        when(articleRepository.findByIdAndIsDeleted(1L, false)).thenReturn(Optional.of(article));

        // when
        ArticleResponse articleResponse = articleService.findById(1L);

        // then
        Assertions.assertThat(articleResponse.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("findById Fail - 존재하지 않는 게시글")
    void findByIdFail() {
        // given
        when(articleRepository.findByIdAndIsDeleted(1L, false)).thenReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> articleService.findById(1L));

        // then
    }

    @Test
    @DisplayName("findByNicknameTitle Success")
    void findByNicknameAndTitleSuccess() {
        // given
        User user = new User();
        Article article = new Article(1L, new User(), TITLE, BODY, 0, 0, 0, "");
        when(articleRepository.findByUserAndTitleAndIsDeleted(user, TITLE, false))
                .thenReturn(Optional.of(article));
        when(userService.findByNickname(NICKNAME)).thenReturn(user);

        // when
        Article findArticle = articleService.findByNicknameAndTitle(NICKNAME, TITLE);

        // then
        Assertions.assertThat(findArticle.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("findByNicknameTitle Fail")
    void findByNicknameAndTitleFail() {
        // given
        User user = new User();
        when(articleRepository.findByUserAndTitleAndIsDeleted(user, TITLE, false))
                .thenReturn(Optional.empty());
        when(userService.findByNickname(NICKNAME)).thenReturn(user);

        // when
        assertThrows(
                NotFoundException.class, () -> articleService.findByNicknameAndTitle(NICKNAME, TITLE));

        // then
    }
}
