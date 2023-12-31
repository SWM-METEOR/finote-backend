package kr.co.finote.backend.src.article.service;

import java.util.Optional;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.domain.ArticleLike;
import kr.co.finote.backend.src.article.dto.cache.ArticleLikeCache;
import kr.co.finote.backend.src.article.repository.ArticleLikeRepository;
import kr.co.finote.backend.src.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleLikeCacheService {

    private final ArticleLikeRepository articleLikeRepository;

    @Cacheable(
            key = "#user.id + '-' + #article.id",
            value = "ArticleLikeLog",
            cacheManager = "articleLikeManager")
    @Transactional
    public ArticleLikeCache findLikeLog(User user, Article article) {
        log.info("not cached");
        Optional<ArticleLike> articleLike = articleLikeRepository.findByUserAndArticle(user, article);
        return articleLike.map(ArticleLikeCache::of).orElse(null);
    }
}
