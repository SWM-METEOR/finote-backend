package kr.co.finote.backend.src.article.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kr.co.finote.backend.global.utils.StringUtils;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.domain.ArticleLike;
import kr.co.finote.backend.src.article.dto.response.ArticlePreviewListResponse;
import kr.co.finote.backend.src.article.dto.response.ArticlePreviewResponse;
import kr.co.finote.backend.src.article.repository.ArticleLikeRepository;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.dto.response.LikeCountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleLikeService {

    private final ArticleLikeRepository articleLikeRepository;

    public Optional<ArticleLike> findByUser(User user, Article article) {
        return articleLikeRepository.findByUserAndArticle(user, article);
    }

    public void save(User user, Article article) {
        articleLikeRepository.save(ArticleLike.createArticleLike(user, article));
    }

    public void deleteAllByArticle(Article article) {
        articleLikeRepository.deleteAllByArticle(article);
    }

    public LikeCountResponse getLikeCount(User loginUser) {
        return LikeCountResponse.createLikeCountResponse(
                articleLikeRepository.countByUserAndIsDeleted(loginUser, false));
    }

    public ArticlePreviewListResponse getLikeArticles(User loginUser, int page, int size) {
        int pageNum = page - 1;
        Pageable pageable = PageRequest.of(pageNum, size, Sort.by("createdDate").descending());

        List<ArticlePreviewResponse> articlePreviewResponseList = new ArrayList<>();
        Page<ArticleLike> articleLikePage =
                articleLikeRepository.findAllByUserAndIsDeleted(loginUser, false, pageable);
        articleLikePage.getContent().stream()
                .map(ArticleLike::getArticle)
                .forEach(
                        article -> {
                            String previewBody = StringUtils.markdownToPreviewText(article.getBody());
                            articlePreviewResponseList.add(ArticlePreviewResponse.of(article, previewBody));
                        });

        return ArticlePreviewListResponse.of(page, size, articlePreviewResponseList);
    }
}
