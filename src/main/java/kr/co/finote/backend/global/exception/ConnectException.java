package kr.co.finote.backend.global.exception;

import kr.co.finote.backend.global.code.ResponseCode;
import lombok.Getter;

@Getter
public class ConnectException extends CustomException {

    public ConnectException(ResponseCode responseCode) {
        super(responseCode);
    }
}
