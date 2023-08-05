package kr.co.finote.backend.global.exception;

import kr.co.finote.backend.global.code.ResponseCode;
import lombok.Getter;

@Getter
public class UnAuthorizedException extends CustomException {
    public UnAuthorizedException(ResponseCode responseCode) {
        super(responseCode);
    }
}
