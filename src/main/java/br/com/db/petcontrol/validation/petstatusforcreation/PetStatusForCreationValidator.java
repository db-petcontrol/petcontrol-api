package br.com.db.petcontrol.validation.petstatusforcreation;

import br.com.db.petcontrol.dto.request.PetRequestDTO;
import br.com.db.petcontrol.model.enums.PetStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PetStatusForCreationValidator
    implements ConstraintValidator<ValidPetStatusForCreation, PetRequestDTO> {
  private String message;

  @Override
  public void initialize(ValidPetStatusForCreation annotation) {
    this.message = annotation.message();
  }

  @Override
  public boolean isValid(PetRequestDTO dto, ConstraintValidatorContext context) {
    if (dto == null || dto.status() == null) {
      return true;
    }

    boolean isValid = dto.status() == PetStatus.AVAILABLE;

    if (!isValid) {
      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate(this.message + ", but got: " + dto.status())
          .addConstraintViolation();
    }

    return isValid;
  }
}
