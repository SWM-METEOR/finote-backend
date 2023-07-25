package kr.co.finote.backend.global.advice;

import java.util.ArrayList;
import java.util.List;
import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.CustomException;
import kr.co.finote.backend.global.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(ResponseCode.INVALID_INPUT_VALUE.getCode());
        errorResponse.setStatus(ResponseCode.INVALID_INPUT_VALUE.getStatus().value());
        errorResponse.setMessage(ResponseCode.INVALID_INPUT_VALUE.getMessage());
        List<FieldError> fieldErrors = e.getFieldErrors();
        List<ErrorResponse.DetailError> detailErrorList = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            detailErrorList.add(
                    new ErrorResponse.DetailError(
                            fieldError.getField(),
                            fieldError.getRejectedValue() == null ? "" : fieldError.getRejectedValue().toString(),
                            fieldError.getDefaultMessage()));
        }
        errorResponse.setDetail(detailErrorList);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(e.getResponseCode().getCode());
        errorResponse.setStatus(e.getResponseCode().getStatus().value());
        errorResponse.setMessage(e.getResponseCode().getMessage());
        return new ResponseEntity<>(errorResponse, e.getResponseCode().getStatus());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(ResponseCode.INTERNAL_ERROR.getCode());
        errorResponse.setStatus(ResponseCode.INTERNAL_ERROR.getStatus().value());
        errorResponse.setMessage(ResponseCode.INTERNAL_ERROR.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
