package kr.co.finote.backend.global.advice;

import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.CustomException;
import kr.co.finote.backend.global.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(e.getResponseCode().getCode());
        errorResponse.setStatus(e.getResponseCode().getStatus().value());
        errorResponse.setMessage(e.getResponseCode().getMessage());
        return new ResponseEntity<>(errorResponse, e.getResponseCode().getStatus());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(ResponseCode.INTERNAL_ERROR.getCode());
        errorResponse.setStatus(ResponseCode.INTERNAL_ERROR.getStatus().value());
        errorResponse.setMessage(ResponseCode.INTERNAL_ERROR.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
