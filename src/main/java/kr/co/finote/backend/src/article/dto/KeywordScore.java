package kr.co.finote.backend.src.article.dto;

import kr.co.finote.backend.src.article.domain.Keyword;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class KeywordScore {

    private Keyword keyword;
    private Double score;

    public static KeywordScore createKeywordScore(Keyword keyword, Double score) {
        return new KeywordScore(keyword, score);
    }
}
