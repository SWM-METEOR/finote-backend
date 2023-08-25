package kr.co.finote.backend.src.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class FollowResultResponse {
    private boolean success;

    public static FollowResultResponse of(boolean success) {
        return FollowResultResponse.builder().success(success).build();
    }
}
