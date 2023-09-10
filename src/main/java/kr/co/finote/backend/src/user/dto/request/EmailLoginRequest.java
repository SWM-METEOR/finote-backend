package kr.co.finote.backend.src.user.dto.request;

import lombok.Getter;

@Getter
public class EmailLoginRequest {

    private String email;
    private String password;
}
