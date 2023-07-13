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

    INVALID_INPUT_VALUE(BAD_REQUEST, "400_BAD_REQUEST", "입력값이 올바르지 않습니다.");

    /** custom error code */
    private final HttpStatus status;

    private final String code;
    private final String message;
}
