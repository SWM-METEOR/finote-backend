package kr.co.finote.backend.src.blog.service;

import kr.co.finote.backend.src.blog.domain.Article;
import kr.co.finote.backend.src.blog.dto.request.ArticleRequest;
import kr.co.finote.backend.src.blog.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Long save(ArticleRequest articleRequest) {
        Article article =
                Article.builder()
                        .title(articleRequest.getTitle())
                        .body(articleRequest.getBody())
                        .user(null) // TODO user 정보 연결
                        .build();

        return articleRepository.save(article).getId();
    }
}
