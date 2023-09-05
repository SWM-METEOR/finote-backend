package kr.co.finote.backend.src.common.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUploadRequest {

    private String fileName;
}
