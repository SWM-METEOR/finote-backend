package kr.co.finote.backend.src.article.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AiSearchType {
    MEANING("의미", "의미: \n"),
    EXPLANATION("추가 설명", "추가 설명: \n"),
    CODE("코드 설명", "코드 설명: \n");

    private final String type;
    private final String RESPONSE_PREFIX;
}
