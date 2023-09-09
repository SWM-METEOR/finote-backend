package kr.co.finote.backend.global.annotation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import kr.co.finote.backend.global.annotation.ValidPassword;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return value.matches(
                "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%&*-])[A-Za-z\\d~!@#$%^&*()+|=]{8,20}$");
    }
}
