package kr.co.finote.backend.src.user.dto.request;

import lombok.Getter;

@Getter
public class EmailCodeValidationRequest {

    private String email;
    private String code;
}
