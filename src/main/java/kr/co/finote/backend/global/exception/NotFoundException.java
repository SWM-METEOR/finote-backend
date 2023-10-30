package kr.co.finote.backend.global.exception;

import kr.co.finote.backend.global.code.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotFoundException extends RuntimeException {

    private final ResponseCode responseCode;
}
