package kr.co.finote.backend.global.exception;

import kr.co.finote.backend.global.code.ResponseCode;
import lombok.Getter;

@Getter
public class InvalidInputException extends CustomException {
    public InvalidInputException(ResponseCode responseCode) {
        super(responseCode);
    }
}
