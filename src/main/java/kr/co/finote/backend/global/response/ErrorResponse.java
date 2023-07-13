package kr.co.finote.backend.global.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
        private String value;
        private String reason;
    }
}
