package br.com.db.petcontrol.exception;

import br.com.db.petcontrol.dto.response.ErrorResponseDTO;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponseDTO> handleNotFoundException(NotFoundException ex) {

    ErrorResponseDTO response = new ErrorResponseDTO(ex.getMessages());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {

    List<String> errors =
        ex.getBindingResult().getAllErrors().stream()
            .map(error -> error.getDefaultMessage())
            .toList();

    ErrorResponseDTO response = new ErrorResponseDTO(errors);
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponseDTO> handleTypeMismatch(
      MethodArgumentTypeMismatchException ex) {
    String message =
        String.format("Valor inválido '%s' para o parâmetro '%s'", ex.getValue(), ex.getName());
    ErrorResponseDTO error = new ErrorResponseDTO(List.of(message));
    return ResponseEntity.badRequest().body(error);
  }
}
