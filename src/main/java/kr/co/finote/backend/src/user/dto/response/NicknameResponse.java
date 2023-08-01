package kr.co.finote.backend.src.user.dto.response;

import kr.co.finote.backend.src.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NicknameResponse {

    private String nickname;

    public static NicknameResponse of(User user) {
        return new NicknameResponse(user.getNickname());
    }
}
