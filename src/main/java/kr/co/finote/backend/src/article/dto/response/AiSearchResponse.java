package kr.co.finote.backend.src.article.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AiSearchResponse {

    private String type;
    private String content;
}
