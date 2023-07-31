package kr.co.finote.backend.src.user.dto.response;

import kr.co.finote.backend.src.user.domain.User;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SaveUserResponse {

    private User user;
    private Boolean isNewUser;

    public static SaveUserResponse newUser(User user) {
        return SaveUserResponse.builder().user(user).isNewUser(true).build();
    }

    public static SaveUserResponse oldUser(User user) {
        return SaveUserResponse.builder().user(user).isNewUser(false).build();
    }
}
