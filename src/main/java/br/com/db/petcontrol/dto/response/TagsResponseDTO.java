package br.com.db.petcontrol.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Builder;

@Builder
@Schema(description = "Resposta com informações da tag")
public record TagsResponseDTO(
    @Schema(
            description = "Identificador único da tag",
            example = "d6acb364-305e-4883-964f-0e7aee360684")
        UUID id,
    @Schema(description = "Nome da tag", example = "Vacinado") String name) {}
