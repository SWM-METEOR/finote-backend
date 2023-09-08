package kr.co.finote.backend.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import kr.co.finote.backend.global.annotation.validator.EmailValidator;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
public @interface ValidEmail {

    String message() default "@을 포함한 올바른 이메일 형식을 입력해주세요.";

    Class[] groups() default {};

    Class[] payload() default {};
}
