package kr.co.finote.backend.src.common.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FollowingsCountResponse {

    private int count;

    public static FollowingsCountResponse createFollowingsResponse(int count) {
        return FollowingsCountResponse.builder().count(count).build();
    }
}
