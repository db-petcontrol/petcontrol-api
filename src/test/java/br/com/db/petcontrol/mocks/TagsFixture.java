package br.com.db.petcontrol.mocks;

import br.com.db.petcontrol.dto.response.TagsResponseDTO;
import br.com.db.petcontrol.model.Tags;
import java.util.UUID;

public final class TagsFixture {

  private static final String DEFAULT_NAME = "amigável";

  private TagsFixture() {}

  public static Tags.TagsBuilder builder() {
    return Tags.builder().id(UUID.randomUUID()).name(DEFAULT_NAME);
  }

  public static TagsResponseDTO.TagsResponseDTOBuilder dtoBuilder() {
    return TagsResponseDTO.builder().id(UUID.randomUUID()).name(DEFAULT_NAME);
  }
}
