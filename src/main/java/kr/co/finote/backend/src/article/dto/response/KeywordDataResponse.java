package kr.co.finote.backend.src.article.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KeywordDataResponse {

    @JsonProperty("keyword")
    private String keyword;

    @JsonProperty("score")
    private double score;
}
