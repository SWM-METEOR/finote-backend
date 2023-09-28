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
    EMAIL_SENDING_FAILED(BAD_REQUEST, "400_EMAIL_SENDING_FAILED", "이메일 전송에 실패했습니다."),
    EMAIL_ALREADY_EXIST(BAD_REQUEST, "400_EMAIL_ALREADY_EXIST", "해당 이메일로 가입된 계정이 존재합니다."),
    EMAIL_NOT_VERIFIED(BAD_REQUEST, "400_NOT_VERIFIED_EMAIL", "인증되지 않은 이메일입니다."),
    EMAIL_CODE_NOT_MATCH(BAD_REQUEST, "400_EMAIL_CODE_NOT_MATCH", "인증 코드가 일치하지 않습니다."),

    /** category error */
    CATEGORY_ALREADY_EXIST(BAD_REQUEST, "400_CATEGORY_ALREADY_EXIST", "같은 이름의 카테고리가 이미 존재합니다."),
    CATEGORY_NOT_FOUND(NOT_FOUND, "404_CATEGORY_NOT_FOUND", "해당 카테고리를 찾을 수 없습니다."),
    CATEGORY_NOT_WRITER(BAD_REQUEST, "400_CATEGORY_NOT_WRITER", "카테고리 수정 및 삭제 권한이 없습니다."),
    CATEGORY_NOT_EMPTY(BAD_REQUEST, "400_CATEGORY_NOT_EMPTY", "카테고리에 게시글이 존재합니다."),

    /** article error */
    ARTICLE_NOT_FOUND(NOT_FOUND, "404_ARTICLE_NOT_FOUND", "해당 게시글을 찾을 수 없습니다."),
    ARTICLE_ALREADY_EXIST(BAD_REQUEST, "400_ARTICLE_ALREADY_EXIST", "같은 제목의 게시글이 이미 존재합니다."),
    ARTICLE_NOT_WRITER(BAD_REQUEST, "400_ARTICLE_NOT_WRITER", "게시글 수정 및 삭제 권한이 없습니다"),

    /** reply error */
    REPLY_NOT_FOUND(NOT_FOUND, "404_REPLY_NOT_FOUND", "해당 댓글을 찾을 수 없습니다."),
    REPLY_NOT_WRITER(BAD_REQUEST, "400_REPLY_NOT_WRITER", "댓글 수정 및 삭제 권한이 없습니다."),

    /** authentication/authorization error */
    UNAUTHENTICATED(UNAUTHORIZED, "401_UNAUTHORIZED", "인증되지 않은 사용자입니다."),
    INVALID_ACCESS_TOKEN(UNAUTHORIZED, "401_INVALID_ACCESS_TOKEN", "액세스 토큰이 유효하지 않습니다."),
    INVALID_REFRESH_TOKEN(UNAUTHORIZED, "401_INVALID_REFRESH_TOKEN", "리프레시 토큰이 유효하지 않습니다."),

    /** follow error */
    ALREADY_FOLLOWING(BAD_REQUEST, "400_ARLEADY_FOLLWING", "이미 팔로잉 한 유저입니다."),
    NOT_FOLLOWING(BAD_REQUEST, "400_NOT_FOLLOWING", "팔로우하지 않은 유저입니다."),
    SELF_FOLLOWING(BAD_REQUEST, "400_SELF_FOLLOWING", "자기 자신을 팔로우/언팔로우 할 수 없습니다."),

    /** question error */
    QUESTION_ALREADY_EXIST(BAD_REQUEST, "400_QUESTION_ALREADY_EXIST", "같은 제목의 질문 글을 이미 등록했습니다."),
    QUESTION_NOT_FOUND(NOT_FOUND, "404_QUESTION_NOT_FOUND", "해당 질문 글을 찾을 수 없습니다."),
    QUESTION_NOT_WRITER(BAD_REQUEST, "404_QUESTION_NOT_WRITER", "질문 글 수정/삭제 권한이 없습니다."),

    /** Conncetion error */
    ES_NOT_CONNECT(INTERNAL_SERVER_ERROR, "500_ES_NOT_CONNECT", "Es서버와 연결 되지 않았습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
