package kr.co.finote.backend.global.advice;

import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.NotFoundException;
import kr.co.finote.backend.global.exception.UnAuthorizedException;
import kr.co.finote.backend.global.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExHandler {

    // @Valid 실패 시 발생하는 예외 공통 처리 로직
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        log.error(e.getMessage());

        ErrorResponse errorResponse = ErrorResponse.hasDetailError(ResponseCode.INVALID_INPUT_VALUE, e);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        log.error(e.getMessage());

        ErrorResponse errorResponse = ErrorResponse.noDetailError(e.getResponseCode());
        return new ResponseEntity<>(errorResponse, e.getResponseCode().getStatus());
    }

    @ExceptionHandler(UnAuthorizedException.class)
    protected ResponseEntity<ErrorResponse> handleUnAuthorizedException(UnAuthorizedException e) {
        log.error(e.getMessage());

        ErrorResponse errorResponse = ErrorResponse.noDetailError(e.getResponseCode());
        return new ResponseEntity<>(errorResponse, e.getResponseCode().getStatus());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {

        log.error(e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.noDetailError(ResponseCode.INTERNAL_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
