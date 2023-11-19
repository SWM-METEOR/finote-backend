package kr.co.finote.backend.src.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedListResponse {

    private List<FeedResponse> feeds;

    public static FeedListResponse of(List<FeedResponse> feeds) {
        return FeedListResponse.builder()
                .feeds(feeds)
                .build();
    }
}
