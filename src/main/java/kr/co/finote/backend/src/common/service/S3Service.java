package kr.co.finote.backend.src.common.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import java.net.URL;
import java.util.Date;
import java.util.UUID;
import kr.co.finote.backend.src.common.dto.request.FileUploadRequest;
import kr.co.finote.backend.src.common.dto.response.PresignedUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${S3_BUCKET_NAME}")
    private String bucketName;

    public static final Long EXPIRATION_TIME = 1000L * 60 * 60; // 1 hour

    public PresignedUrlResponse getPresignedUrl(FileUploadRequest request) {
        String fileName = UUID.randomUUID().toString() + "_" + request.getFileName();
        String objectKey = "images/" + fileName;

        Date expiration = new Date();
        long expirationTimeMillis = expiration.getTime();
        expirationTimeMillis += EXPIRATION_TIME;
        expiration.setTime(expirationTimeMillis);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, objectKey)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(expiration);

        // 업로드 이후 query parameter를 모두 제거하고 pre-signed url에 GET요청을 보낼 때 정상적으로 동작하도록 하기 위해 추가
        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL, CannedAccessControlList.PublicRead.toString());

        URL presignedUrl = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return PresignedUrlResponse.createdPresignedUrlResponse(presignedUrl.toString());
    }
}
