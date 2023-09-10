package kr.co.finote.backend.src.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ValidationBlogNameResponse {

    private boolean isDuplicated;

    public static ValidationBlogNameResponse createValidationBlogNameResponse(boolean isDuplicated) {
        return ValidationBlogNameResponse.builder().isDuplicated(isDuplicated).build();
    }
}
