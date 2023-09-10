package kr.co.finote.backend.src.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ValidationNicknameResponse {

    private boolean isDuplicated;

    public static ValidationNicknameResponse createValidationNicknameRResponse(boolean isDuplicated) {
        return ValidationNicknameResponse.builder().isDuplicated(isDuplicated).build();
    }
}
