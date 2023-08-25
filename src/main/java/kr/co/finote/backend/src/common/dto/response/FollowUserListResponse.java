package kr.co.finote.backend.src.common.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FollowUserListResponse {

    List<FollowUserResponse> lists;

    public static FollowUserListResponse of(List<FollowUserResponse> lists) {
        return FollowUserListResponse.builder().lists(lists).build();
    }
}
