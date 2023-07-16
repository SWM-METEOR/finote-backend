package kr.co.finote.backend.src.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalInfoRequest {

    private String profileImage;
    private String nickname;
    private String blogName;
    private String blogUrl;
}
