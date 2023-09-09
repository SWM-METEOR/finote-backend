package kr.co.finote.backend.src.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EmailCodeValidationResponse {

    private boolean isValid;

    public static EmailCodeValidationResponse createEmailValidationResponse(boolean isValid) {
        return EmailCodeValidationResponse.builder().isValid(isValid).build();
    }
}
