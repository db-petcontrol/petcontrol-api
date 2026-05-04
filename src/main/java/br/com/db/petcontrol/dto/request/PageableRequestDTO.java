package br.com.db.petcontrol.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Parâmetros de paginação")
public record PageableRequestDTO(
    @Schema(description = "Número da página (começa em 0)", example = "0") Integer page,
    @Schema(description = "Tamanho da página", example = "6") Integer size) {
  public PageableRequestDTO {
    if (page == null) {
      page = 0;
    }

    if (size == null) {
      size = 6;
    }
  }
}
