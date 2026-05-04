package br.com.db.petcontrol.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
@Schema(description = "Resposta paginada contendo os elementos e metadados da página")
public record PageResponseDTO<T>(
    @Schema(description = "Conteúdo da página") List<T> content,
    @Schema(description = "Total de páginas disponíveis", example = "5") int totalPages,
    @Schema(description = "Total de elementos disponíveis", example = "100") long totalElements,
    @Schema(description = "Tamanho da página (quantidade de elementos por página)", example = "6")
        int size,
    @Schema(description = "Número da página atual", example = "0") int number) {}
