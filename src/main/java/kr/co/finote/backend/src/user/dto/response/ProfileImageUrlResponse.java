package kr.co.finote.backend.src.user.dto.response;

import kr.co.finote.backend.src.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileImageUrlResponse {
    private String profileImageUrl;

    public static ProfileImageUrlResponse of(User loginUser) {
        String profileImageUrl = loginUser.getProfileImageUrl();
        if (profileImageUrl == null || profileImageUrl.equals("")) {
            return new ProfileImageUrlResponse(
                    "https://finote-image-bucket.s3.ap-northeast-2.amazonaws.com/profile_image.png");
        }
        return new ProfileImageUrlResponse(loginUser.getProfileImageUrl());
    }
}
