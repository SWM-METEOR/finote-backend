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

    /** global error */
    INTERNAL_ERROR(INTERNAL_SERVER_ERROR, "500_INTERNAL_SERVER_ERROR", "서버 내부에 오류가 발생했습니다."),

    INVALID_INPUT_VALUE(BAD_REQUEST, "400_BAD_REQUEST", "입력값이 올바르지 않습니다."),

    /** Users error */
    DUPLICATE_NICKNAME(BAD_REQUEST, "400_BAD_REQUEST", "중복된 닉네임입니다."),

    /** UsersBlog error */
    BLOG_NOT_FOUND(NOT_FOUND, "404_BLOG_NOT_FOUND", "유저의 블로그를 찾을 수 없습니다."),
    DUPLICATE_BLOG_NAME(BAD_REQUEST, "400_BAD_REQUEST", "중복된 블로그명입니다."),
    DUPLICATE_BLOG_URL(BAD_REQUEST, "400_BAD_REQUEST", "중복된 블로그 url 입니다."),

    /** article error */
    ARTICLE_NOT_FOUND(NOT_FOUND, "404_ARTICLE_NOT_FOUND", "해당 게시글을 찾을 수 없습니다."),

    /** error occurs when unauthenticated user requests for resource */
    UNAUTHENTICATED(UNAUTHORIZED, "401_UNAUTHORIZED", "인증되지 않은 사용자입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
