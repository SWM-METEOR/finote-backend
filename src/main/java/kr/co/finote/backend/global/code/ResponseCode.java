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

    INVALID_INPUT_VALUE(BAD_REQUEST, "400_INVALID_INPUT_VALUE", "입력값이 올바르지 않습니다."),

    /** Users error */
    USER_NOT_FOUND(NOT_FOUND, "404_USER_NOT_FOUND", "존재하지 않는 유저입니다."),
    DUPLICATE_NICKNAME(BAD_REQUEST, "400_DUPLICATE_NICKNAME", "중복된 닉네임입니다."),
    NICKNAME_TOO_LONG(BAD_REQUEST, "400_NICKNAME_TOO_LONG", "닉네임은 100자 이하로 입력해주세요."),
    DUPLICATE_BLOG_NAME(BAD_REQUEST, "400_DUPLICATE_BLOG_NAME", "중복된 블로그명입니다."),
    BLOG_NAME_TOO_LONG(BAD_REQUEST, "400_BLOG_NAME_TOO_LONG", "블로그명은 100자 이하로 입력해주세요."),
    DUPLICATE_BLOG_URL(BAD_REQUEST, "400_DUPLICATE_BLOG_URL", "중복된 블로그 url 입니다."),
    BLOG_URL_TOO_LONG(BAD_REQUEST, "400_BLOG_URL_TOO_LONG", "블로그 url은 100자 이하로 입력해주세요."),

    /** article error */
    ARTICLE_NOT_FOUND(NOT_FOUND, "404_ARTICLE_NOT_FOUND", "해당 게시글을 찾을 수 없습니다."),

    /** authentication/authorization error */
    UNAUTHENTICATED(UNAUTHORIZED, "401_UNAUTHORIZED", "인증되지 않은 사용자입니다."),
    INVALID_ACCESS_TOKEN(UNAUTHORIZED, "401_INVALID_ACCESS_TOKEN", "액세스 토큰이 유효하지 않습니다."),
    INVALID_REFRESH_TOKEN(UNAUTHORIZED, "401_INVALID_REFRESH_TOKEN", "리프레시 토큰이 유효하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
