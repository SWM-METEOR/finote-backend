package kr.co.finote.backend.src.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ValidationEmailResponse {

    private boolean isDuplicated;

    public static ValidationEmailResponse createValidationEmailResponse(boolean isDuplicated) {
        return ValidationEmailResponse.builder().isDuplicated(isDuplicated).build();
    }
}
