package kr.co.finote.backend.global.exception;

import kr.co.finote.backend.global.code.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UnAuthorizedException extends RuntimeException {

    private final ResponseCode responseCode;
}
