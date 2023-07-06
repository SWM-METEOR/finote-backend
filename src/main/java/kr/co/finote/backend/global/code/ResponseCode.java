package kr.co.finote.backend.global.code;

import static org.springframework.http.HttpStatus.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ResponseCode {
    /** success */
    SUCCESS(OK, "200_OK", "요청에 성공하였습니다."),

    /** error */
    INTERNAL_ERROR(INTERNAL_SERVER_ERROR, "500_INTERNAL_SERVER_ERROR", "서버 내부에 오류가 발생했습니다."),

    /** custom error code */
    TEST_ERROR(BAD_REQUEST, "400_BAD_REQUEST", "잘못된 요청입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
