package kr.co.finote.backend.src.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmailCodeValidationRequest {

    private String email;
    private String code;
}
