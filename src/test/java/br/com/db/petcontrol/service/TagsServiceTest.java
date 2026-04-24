package br.com.db.petcontrol.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.db.petcontrol.dto.response.TagsResponseDTO;
import br.com.db.petcontrol.mapper.TagsMapper;
import br.com.db.petcontrol.mocks.TagsFixture;
import br.com.db.petcontrol.model.Tags;
import br.com.db.petcontrol.repository.TagsRepository;
import br.com.db.petcontrol.service.impl.TagsServiceImpl;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TagsServiceTest {

  @Mock private TagsRepository repository;

  private TagsServiceImpl service;

  private TagsMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = Mappers.getMapper(TagsMapper.class);
    service = new TagsServiceImpl(repository, mapper);
  }

  @Nested
  class GetAllTagsTests {

    @Test
    void shouldReturnAllTagsOrdered() {
      List<Tags> tags =
          List.of(
              TagsFixture.builder().name("adaptado a apartamento").build(),
              TagsFixture.builder().name("amigável").build(),
              TagsFixture.builder().name("castrado").build(),
              TagsFixture.builder().name("independente").build(),
              TagsFixture.builder().name("necessita de cuidados especiais").build(),
              TagsFixture.builder().name("sociável com outros animais").build(),
              TagsFixture.builder().name("treinado").build(),
              TagsFixture.builder().name("vacinado").build(),
              TagsFixture.builder().name("vermifugado").build());

      when(repository.findAllByOrderByNameAsc()).thenReturn(tags);

      List<TagsResponseDTO> result = service.getAllTags();

      assertThat(result)
          .hasSize(9)
          .extracting(TagsResponseDTO::name)
          .containsExactly(
              "adaptado a apartamento",
              "amigável",
              "castrado",
              "independente",
              "necessita de cuidados especiais",
              "sociável com outros animais",
              "treinado",
              "vacinado",
              "vermifugado");

      verify(repository).findAllByOrderByNameAsc();
    }

    @Test
    void shouldReturnTagsWithCorrectIds() {
      UUID id1 = UUID.randomUUID();
      UUID id2 = UUID.randomUUID();

      when(repository.findAllByOrderByNameAsc())
          .thenReturn(
              List.of(
                  TagsFixture.builder().id(id1).name("adaptado a apartamento").build(),
                  TagsFixture.builder().id(id2).name("amigável").build()));

      List<TagsResponseDTO> result = service.getAllTags();

      assertThat(result).hasSize(2).extracting(TagsResponseDTO::id).containsExactly(id1, id2);
    }

    @Test
    void shouldReturnEmptyList() {
      when(repository.findAllByOrderByNameAsc()).thenReturn(List.of());

      assertThat(service.getAllTags()).isEmpty();

      verify(repository).findAllByOrderByNameAsc();
    }

    @Test
    void shouldMapEntityFieldsCorrectly() {
      UUID id = UUID.randomUUID();
      String nome = "amigável";

      when(repository.findAllByOrderByNameAsc())
          .thenReturn(List.of(TagsFixture.builder().id(id).name(nome).build()));

      TagsResponseDTO dto = service.getAllTags().getFirst();

      assertThat(dto.id()).isEqualTo(id);
      assertThat(dto.name()).isEqualTo(nome);
    }
  }
}
