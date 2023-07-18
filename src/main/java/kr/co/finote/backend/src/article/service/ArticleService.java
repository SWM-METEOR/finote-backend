package kr.co.finote.backend.src.article.service;

import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.CustomException;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.dto.request.ArticleRequest;
import kr.co.finote.backend.src.article.repository.ArticleRepository;
import kr.co.finote.backend.src.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Long save(ArticleRequest articleRequest, User user) {
        Article article =
                Article.builder()
                        .title(articleRequest.getTitle())
                        .body(articleRequest.getBody())
                        .user(user)
                        .build();

        return articleRepository.save(article).getId();
    }

    public Article findById(Long articleId) {
        return articleRepository
                .findById(articleId)
                .orElseThrow(() -> new CustomException(ResponseCode.ARTICLE_NOT_FOUND));
    }
}
