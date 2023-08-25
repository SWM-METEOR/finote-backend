package kr.co.finote.backend.src.common.dto.response;

import kr.co.finote.backend.src.user.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FollowUserResponse {

    String nickName;
    String profileImageUrl;
    String blogName;

    public static FollowUserResponse of(User user) {
        return FollowUserResponse.builder()
                .nickName(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .blogName(user.getBlogName())
                .build();
    }
}
