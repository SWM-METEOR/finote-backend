package kr.co.finote.backend.src.common.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PresignedUrlResponse {

    String preSignedUrl;

    public static PresignedUrlResponse createdPresignedUrlResponse(String preSignedUrl) {
        return PresignedUrlResponse.builder().preSignedUrl(preSignedUrl).build();
    }
}
