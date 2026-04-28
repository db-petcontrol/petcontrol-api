package br.com.db.petcontrol.mocks;

import br.com.db.petcontrol.dto.request.PetRequestDTO;
import br.com.db.petcontrol.dto.response.PetResponseDTO;
import br.com.db.petcontrol.model.Pets;
import br.com.db.petcontrol.model.enums.PetStatus;
import java.util.List;
import java.util.UUID;

public final class PetsFixture {

  private static final String DEFAULT_NAME = "Lily";
  private static final PetStatus DEFAULT_STATUS = PetStatus.AVAILABLE;

  private PetsFixture() {}

  public static Pets.PetsBuilder builder() {
    return Pets.builder()
        .id(UUID.randomUUID())
        .name(DEFAULT_NAME)
        .status(DEFAULT_STATUS)
        .species(SpeciesFixture.builder().build())
        .tags(List.of());
  }

  public static PetRequestDTO.PetRequestDTOBuilder requestDtoBuilder() {
    return PetRequestDTO.builder()
        .name(DEFAULT_NAME)
        .specieId(UUID.randomUUID())
        .status(DEFAULT_STATUS)
        .tagsIds(List.of());
  }

  public static PetResponseDTO.PetResponseDTOBuilder responseDtoBuilder() {
    return PetResponseDTO.builder()
        .id(UUID.randomUUID())
        .name(DEFAULT_NAME)
        .status(DEFAULT_STATUS)
        .species("Cachorro")
        .tags(List.of());
  }
}
