package kr.co.finote.backend.src.article.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedRequest {

    private String authorId;
    private Long articleId;

    public static FeedRequest createFeedRequest(String authorId, Long articleId) {
        return FeedRequest.builder().authorId(authorId).articleId(articleId).build();
    }
}
