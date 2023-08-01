package kr.co.finote.backend.src.article.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AiSearchResponse {

    private String content;

    public static AiSearchResponse createAiSearchResponse(String content) {
        return new AiSearchResponse(content);
    }
}
