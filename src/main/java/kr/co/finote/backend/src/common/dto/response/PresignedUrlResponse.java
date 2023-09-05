package kr.co.finote.backend.src.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PresignedUrlResponse {

    private String preSignedUrl;

    public static PresignedUrlResponse createdPresignedUrlResponse(String preSignedUrl) {
        return PresignedUrlResponse.builder().preSignedUrl(preSignedUrl).build();
    }
}
