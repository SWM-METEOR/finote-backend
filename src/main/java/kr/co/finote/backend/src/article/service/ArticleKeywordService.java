package kr.co.finote.backend.src.article.service;

import java.util.ArrayList;
import java.util.List;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.domain.ArticleKeyword;
import kr.co.finote.backend.src.article.dto.KeywordScore;
import kr.co.finote.backend.src.article.repository.ArticleKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleKeywordService {

    public static final int KEYWORD_MAX_COUNT = 3;
    private final ArticleKeywordRepository articleKeywordRepository;

    public void saveArticleKeywordList(Article article, List<KeywordScore> keywordScoreList) {
        List<ArticleKeyword> articleKeywordList = new ArrayList<>();
        for (KeywordScore keywordScore : keywordScoreList) {
            ArticleKeyword articleKeyword =
                    ArticleKeyword.createArticleKeyword(
                            article, keywordScore.getKeyword(), keywordScore.getScore());
            articleKeywordList.add(articleKeyword);
        }
        articleKeywordRepository.saveAll(articleKeywordList);
    }

    public List<ArticleKeyword> findTop3ArticleKeywordList(Article article) {
        List<ArticleKeyword> articleKeywordList =
                articleKeywordRepository.findAllByArticleAndIsDeleted(article, false);
        if (articleKeywordList.size() > KEYWORD_MAX_COUNT) return articleKeywordList.subList(0, 3);
        else return articleKeywordList;
    }
}
