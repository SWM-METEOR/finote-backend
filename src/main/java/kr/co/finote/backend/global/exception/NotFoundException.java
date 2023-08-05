package kr.co.finote.backend.global.exception;

import kr.co.finote.backend.global.code.ResponseCode;
import lombok.Getter;

@Getter
public class NotFoundException extends CustomException {
    public NotFoundException(ResponseCode responseCode) {
        super(responseCode);
    }
}
