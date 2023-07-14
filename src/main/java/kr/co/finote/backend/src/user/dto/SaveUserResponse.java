package kr.co.finote.backend.src.user.dto;

import kr.co.finote.backend.src.user.domain.User;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveUserResponse {

    private User user;
    private Boolean newUser;
}
