package kr.co.finote.backend.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import kr.co.finote.backend.global.annotation.validator.PasswordValidator;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface ValidPassword {

    String message() default
            "비밀번호는 대소문자, 숫자, 특수문자(~, !, @, #, $, %, &, *, -)를 포함하여 8자~20자 사이로 입력해주세요.";

    Class[] groups() default {};

    Class[] payload() default {};
}
