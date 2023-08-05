package kr.co.finote.backend.global.response;

import java.util.List;
import java.util.stream.Collectors;
import kr.co.finote.backend.global.code.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private int status;
    private String code;
    private String message;
    private List<DetailError> detail;

    @Setter
    @Getter
    @AllArgsConstructor
    public static class DetailError {
        private String field;
        private String rejectedValue;
        private String reason;
    }

    public static ErrorResponse hasDetailError(
            ResponseCode code, MethodArgumentNotValidException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(code.getCode());
        errorResponse.setStatus(code.getStatus().value());
        errorResponse.setMessage(code.getMessage());

        List<ErrorResponse.DetailError> list =
                exception.getFieldErrors().stream()
                        .map(
                                fieldError ->
                                        new ErrorResponse.DetailError(
                                                fieldError.getField(),
                                                fieldError.getRejectedValue() == null
                                                        ? ""
                                                        : fieldError.getRejectedValue().toString(),
                                                fieldError.getDefaultMessage()))
                        .collect(Collectors.toList());

        errorResponse.setDetail(list);

        return errorResponse;
    }

    public static ErrorResponse noDetailError(ResponseCode code) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(code.getCode());
        errorResponse.setStatus(code.getStatus().value());
        errorResponse.setMessage(code.getMessage());

        return errorResponse;
    }
}
