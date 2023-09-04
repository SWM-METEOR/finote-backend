package kr.co.finote.backend.src.article.service;

import java.util.Optional;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.domain.ArticleLike;
import kr.co.finote.backend.src.article.repository.ArticleLikeRepository;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.dto.response.LikeCountResponse;
import lombok.RequiredArgsConstructor;
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
}
