package kr.co.finote.backend.src.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailCodeValidationRequest {

    private String email;
    private String code;
}
