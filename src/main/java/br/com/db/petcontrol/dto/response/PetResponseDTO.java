package br.com.db.petcontrol.dto.response;

import br.com.db.petcontrol.model.enums.PetStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record PetResponseDTO(
    @Schema(
            description = "Identificador único do pet",
            example = "d6acb364-305e-4883-964f-0e7aee360684")
        UUID id,
    @Schema(description = "Nome do pet", example = "Lily") String name,
    @Schema(description = "Status do pet", example = "AVAILABLE") PetStatus status,
    @Schema(description = "Espécie do pet", example = "Gato") String species,
    @Schema(description = "Tags do pet", example = "[\"castrado\", \"vacinado\"]")
        List<String> tags) {}
