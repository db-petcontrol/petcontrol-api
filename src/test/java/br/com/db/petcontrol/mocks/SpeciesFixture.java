package br.com.db.petcontrol.mocks;

import br.com.db.petcontrol.dto.response.SpeciesResponseDTO;
import br.com.db.petcontrol.model.Species;
import java.util.UUID;

public final class SpeciesFixture {

  private static final String DEFAULT_NAME = "Cachorro";

  private SpeciesFixture() {}

  public static Species.SpeciesBuilder builder() {
    return Species.builder().id(UUID.randomUUID()).name(DEFAULT_NAME);
  }

  public static SpeciesResponseDTO.SpeciesResponseDTOBuilder dtoBuilder() {
    return SpeciesResponseDTO.builder().id(UUID.randomUUID()).name(DEFAULT_NAME);
  }
}
