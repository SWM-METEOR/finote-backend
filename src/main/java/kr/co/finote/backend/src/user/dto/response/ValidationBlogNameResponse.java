package kr.co.finote.backend.src.user.dto.response;

import lombok.Builder;

@Builder
public class ValidationBlogNameResponse {

    private boolean isDuplicated;

    public static ValidationBlogNameResponse createValidationBlogNameRResponse(boolean isDuplicated) {
        return ValidationBlogNameResponse.builder().isDuplicated(isDuplicated).build();
    }
}
