package br.com.db.petcontrol.dto.request;

import br.com.db.petcontrol.enums.PetStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record PetRequestDTO(
    @NotBlank @Schema(description = "Nome do pet", example = "Lily") String name,
    @NotNull
        @Schema(description = "ID da espécie", example = "dd3af030-9823-40ff-bc6e-4de9eae55894")
        UUID specieId,
    @NotNull @Schema(description = "Status do pet", example = "AVAILABLE") PetStatus status,
    @Schema(
            description = "IDs das tags do pet",
            example =
                "[\"6231f3b7-e6c3-4e93-8890-4fc7dbc06101\", \"171402a5-71fd-42db-a680-df25187ac0c0\"]")
        List<UUID> tagsIds) {}
