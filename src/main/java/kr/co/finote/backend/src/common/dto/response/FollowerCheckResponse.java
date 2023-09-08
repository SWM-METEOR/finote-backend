package kr.co.finote.backend.src.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class FollowerCheckResponse {

    boolean isFollowed;

    public static FollowerCheckResponse createFollowerCheckResponse(boolean isFollowed) {
        return FollowerCheckResponse.builder().isFollowed(isFollowed).build();
    }
}
