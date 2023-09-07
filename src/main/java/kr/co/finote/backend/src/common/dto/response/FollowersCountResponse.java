package kr.co.finote.backend.src.common.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FollowersCountResponse {
    private int count;

    public static FollowersCountResponse createFollowersResponse(int count) {
        return FollowersCountResponse.builder().count(count).build();
    }
}
