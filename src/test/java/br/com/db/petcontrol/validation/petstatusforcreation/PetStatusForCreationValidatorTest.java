package br.com.db.petcontrol.validation.petstatusforcreation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.db.petcontrol.dto.request.PetRequestDTO;
import br.com.db.petcontrol.mocks.PetsFixture;
import br.com.db.petcontrol.model.enums.PetStatus;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PetStatusForCreationValidatorTest {

  @Mock private ConstraintValidatorContext context;
  @Mock private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;
  @Mock private ValidPetStatusForCreation annotation;

  private PetStatusForCreationValidator validator;

  @BeforeEach
  void setUp() {
    validator = new PetStatusForCreationValidator();
    when(annotation.message()).thenReturn("Pet can only be created with status AVAILABLE");
    validator.initialize(annotation);
  }

  @Nested
  class ValidatePetStatusTests {

    @Test
    void shouldReturnTrueWhenStatusIsAvailable() {
      PetRequestDTO dto = PetsFixture.requestDtoBuilder().status(PetStatus.AVAILABLE).build();

      boolean isValid = validator.isValid(dto, context);

      assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnFalseWhenStatusIsInAnalysis() {
      PetRequestDTO dto = PetsFixture.requestDtoBuilder().status(PetStatus.IN_ANALYSIS).build();
      when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);

      boolean isValid = validator.isValid(dto, context);

      assertThat(isValid).isFalse();
    }

    @Test
    void shouldReturnFalseWhenStatusIsAdopted() {
      PetRequestDTO dto = PetsFixture.requestDtoBuilder().status(PetStatus.ADOPTED).build();
      when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);

      boolean isValid = validator.isValid(dto, context);

      assertThat(isValid).isFalse();
    }

    @Test
    void shouldReturnTrueWhenDtoIsNull() {
      boolean isValid = validator.isValid(null, context);

      assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnTrueWhenStatusIsNull() {
      PetRequestDTO dto = PetsFixture.requestDtoBuilder().status(null).build();

      boolean isValid = validator.isValid(dto, context);

      assertThat(isValid).isTrue();
    }

    @Test
    void shouldBuildConstraintViolationWhenStatusIsNotAvailable() {
      PetRequestDTO dto = PetsFixture.requestDtoBuilder().status(PetStatus.IN_ANALYSIS).build();
      when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);

      boolean isValid = validator.isValid(dto, context);

      assertThat(isValid).isFalse();
      verify(context).disableDefaultConstraintViolation();
      verify(context)
          .buildConstraintViolationWithTemplate(
              "Pet can only be created with status AVAILABLE, but got: IN_ANALYSIS");
      verify(violationBuilder).addConstraintViolation();
    }

    @Test
    void shouldReturnDetailedMessageWithActualStatus() {
      PetRequestDTO dto = PetsFixture.requestDtoBuilder().status(PetStatus.ADOPTED).build();
      when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);

      validator.isValid(dto, context);

      verify(context)
          .buildConstraintViolationWithTemplate(
              "Pet can only be created with status AVAILABLE, but got: ADOPTED");
      verify(violationBuilder).addConstraintViolation();
    }
  }
}
