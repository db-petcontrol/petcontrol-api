package br.com.db.petcontrol.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.db.petcontrol.dto.response.SpeciesResponseDTO;
import br.com.db.petcontrol.mapper.SpeciesMapper;
import br.com.db.petcontrol.mocks.SpeciesFixture;
import br.com.db.petcontrol.model.Species;
import br.com.db.petcontrol.repository.SpeciesRepository;
import br.com.db.petcontrol.service.impl.SpeciesServiceImpl;
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
class SpeciesServiceTest {

  @Mock private SpeciesRepository repository;

  private SpeciesServiceImpl service;

  private SpeciesMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = Mappers.getMapper(SpeciesMapper.class);
    service = new SpeciesServiceImpl(repository, mapper);
  }

  @Nested
  class GetAllSpeciesTests {

    @Test
    void shouldReturnAllSpeciesOrdered() {
      List<Species> species =
          List.of(
              SpeciesFixture.builder().name("Calopsita").build(),
              SpeciesFixture.builder().name("Cachorro").build(),
              SpeciesFixture.builder().name("Canário").build(),
              SpeciesFixture.builder().name("Coelho").build(),
              SpeciesFixture.builder().name("Gato").build(),
              SpeciesFixture.builder().name("Hamster").build(),
              SpeciesFixture.builder().name("Pássaro").build(),
              SpeciesFixture.builder().name("Peixe").build(),
              SpeciesFixture.builder().name("Porquinho-da-índia").build());

      when(repository.findAllByOrderByNameAsc()).thenReturn(species);

      List<SpeciesResponseDTO> result = service.getAllSpecies();

      assertThat(result)
          .hasSize(9)
          .extracting(SpeciesResponseDTO::name)
          .containsExactly(
              "Calopsita",
              "Cachorro",
              "Canário",
              "Coelho",
              "Gato",
              "Hamster",
              "Pássaro",
              "Peixe",
              "Porquinho-da-índia");

      verify(repository).findAllByOrderByNameAsc();
    }

    @Test
    void shouldReturnSpeciesWithCorrectIds() {
      UUID id1 = UUID.randomUUID();
      UUID id2 = UUID.randomUUID();

      when(repository.findAllByOrderByNameAsc())
          .thenReturn(
              List.of(
                  SpeciesFixture.builder().id(id1).name("Cachorro").build(),
                  SpeciesFixture.builder().id(id2).name("Gato").build()));

      List<SpeciesResponseDTO> result = service.getAllSpecies();

      assertThat(result).hasSize(2).extracting(SpeciesResponseDTO::id).containsExactly(id1, id2);
    }

    @Test
    void shouldReturnEmptyList() {
      when(repository.findAllByOrderByNameAsc()).thenReturn(List.of());

      assertThat(service.getAllSpecies()).isEmpty();

      verify(repository).findAllByOrderByNameAsc();
    }

    @Test
    void shouldMapEntityFieldsCorrectly() {
      UUID id = UUID.randomUUID();
      String nome = "Réptil";

      when(repository.findAllByOrderByNameAsc())
          .thenReturn(List.of(SpeciesFixture.builder().id(id).name(nome).build()));

      SpeciesResponseDTO dto = service.getAllSpecies().getFirst();

      assertThat(dto.id()).isEqualTo(id);
      assertThat(dto.name()).isEqualTo(nome);
    }
  }
}
