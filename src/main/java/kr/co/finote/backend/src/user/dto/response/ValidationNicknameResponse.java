package kr.co.finote.backend.src.user.dto.response;

import lombok.Builder;

@Builder
public class ValidationNicknameResponse {

    private boolean isDuplicated;

    public static ValidationNicknameResponse createValidationNicknameRResponse(boolean isDuplicated) {
        return ValidationNicknameResponse.builder().isDuplicated(isDuplicated).build();
    }
}
