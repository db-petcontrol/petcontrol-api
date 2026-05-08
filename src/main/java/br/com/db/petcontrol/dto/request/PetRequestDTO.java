package br.com.db.petcontrol.dto.request;

import br.com.db.petcontrol.enums.PetStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record PetRequestDTO(
    @Pattern(regexp = "^[\\p{L}\\s]+$", message = "The name must contain only letters and spaces")
        @NotBlank(message = "Pet name must not be blank")
        @Size(max = 64, message = "Pet name must be at most 64 characters long")
        @Schema(description = "Nome do pet", example = "Lily")
        String name,
    @NotNull(message = "Specie ID must not be null")
        @Schema(description = "ID da espécie", example = "dd3af030-9823-40ff-bc6e-4de9eae55894")
        UUID specieId,
    @NotNull(message = "Pet status must not be null")
        @Schema(description = "Status do pet", example = "AVAILABLE")
        PetStatus status,
    @Schema(
            description = "IDs das tags do pet",
            example =
                "[\"6231f3b7-e6c3-4e93-8890-4fc7dbc06101\", \"171402a5-71fd-42db-a680-df25187ac0c0\"]")
        List<UUID> tagsIds) {}
