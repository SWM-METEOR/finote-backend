package kr.co.finote.backend.src.article.service;

import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.NotFoundException;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.dto.request.ArticleRequest;
import kr.co.finote.backend.src.article.repository.ArticleRepository;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public Long save(ArticleRequest articleRequest, User loginUser) {
        User user =
                userRepository
                        .findByEmailAndIsDeleted("hyunyelim25@gmail.com", false)
                        .orElseThrow(() -> new NotFoundException(ResponseCode.USER_NOT_FOUND));
        Article article = Article.createArticle(articleRequest, user); // TODO 로그인 후 login user로 변경
        return articleRepository.save(article).getId();
    }

    public Article findById(Long articleId) {
        return articleRepository
                .findByIdAndIsDeleted(articleId, false)
                .orElseThrow(() -> new NotFoundException(ResponseCode.ARTICLE_NOT_FOUND));
    }
}
