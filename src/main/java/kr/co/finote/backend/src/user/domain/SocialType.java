package kr.co.finote.backend.src.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SocialType {
    GOOGLE("Google");

    private final String value;
}
