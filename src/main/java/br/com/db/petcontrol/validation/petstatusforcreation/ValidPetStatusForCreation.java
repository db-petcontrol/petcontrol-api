package br.com.db.petcontrol.validation.petstatusforcreation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PetStatusForCreationValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPetStatusForCreation {
  String message() default "Pet can only be created with status AVAILABLE";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
