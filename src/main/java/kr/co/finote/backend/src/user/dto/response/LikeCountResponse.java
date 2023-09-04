package kr.co.finote.backend.src.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeCountResponse {

    private int likeCount;

    public static LikeCountResponse createLikeCountResponse(int likeCount) {
        return new LikeCountResponse(likeCount);
    }
}
