package kr.co.finote.backend.src.user.dto.response;

import kr.co.finote.backend.src.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileImageUrlResponse {
    private String profileImageUrl;

    public static ProfileImageUrlResponse of(User loginUser) {
        return new ProfileImageUrlResponse(loginUser.getProfileImageUrl());
    }
}
