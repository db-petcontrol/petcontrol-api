package br.com.db.petcontrol.service;

import static br.com.db.petcontrol.mocks.PageFixture.DEFAULT_PAGE;
import static br.com.db.petcontrol.mocks.PageFixture.DEFAULT_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.db.petcontrol.dto.request.PetRequestDTO;
import br.com.db.petcontrol.dto.response.PageResponseDTO;
import br.com.db.petcontrol.dto.response.PetResponseDTO;
import br.com.db.petcontrol.enums.PetStatus;
import br.com.db.petcontrol.exception.NotFoundException;
import br.com.db.petcontrol.mapper.PageMapper;
import br.com.db.petcontrol.mapper.PetsMapper;
import br.com.db.petcontrol.mocks.PageFixture;
import br.com.db.petcontrol.mocks.PetsFixture;
import br.com.db.petcontrol.mocks.SpeciesFixture;
import br.com.db.petcontrol.mocks.TagsFixture;
import br.com.db.petcontrol.model.Pets;
import br.com.db.petcontrol.model.Species;
import br.com.db.petcontrol.model.Tags;
import br.com.db.petcontrol.repository.PetsRepository;
import br.com.db.petcontrol.repository.SpeciesRepository;
import br.com.db.petcontrol.repository.TagsRepository;
import br.com.db.petcontrol.service.impl.PetsServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PetsServiceImplTest {

  @Mock private PetsRepository petRepository;
  @Mock private SpeciesRepository speciesRepository;
  @Mock private TagsRepository tagRepository;

  private PetsMapper petsMapper;
  private PageMapper pageMapper;

  private PetsServiceImpl service;

  @BeforeEach
  void setUp() {
    petsMapper = Mappers.getMapper(PetsMapper.class);
    pageMapper = Mappers.getMapper(PageMapper.class);

    service =
        new PetsServiceImpl(
            petRepository, speciesRepository, tagRepository, petsMapper, pageMapper);
  }

  @Nested
  class CreatePetTests {

    @Test
    void shouldCreatePetSuccessfully() {
      UUID speciesId = UUID.randomUUID();
      Species species = SpeciesFixture.builder().id(speciesId).name("Cachorro").build();
      PetRequestDTO dto = PetsFixture.requestDtoBuilder().specieId(speciesId).build();
      Pets expectedPet = PetsFixture.builder().species(species).build();

      when(speciesRepository.findById(speciesId)).thenReturn(Optional.of(species));
      when(tagRepository.findAllById(anyList())).thenReturn(List.of());
      when(petRepository.saveAndFlush(any(Pets.class))).thenReturn(expectedPet);

      PetResponseDTO result = service.create(dto);

      assertThat(result).isNotNull();
      assertThat(result.name()).isEqualTo(expectedPet.getName());
      assertThat(result.species()).isEqualTo("Cachorro");
    }

    @Test
    void shouldReturnPetWithCorrectId() {
      UUID petId = UUID.randomUUID();
      UUID speciesId = UUID.randomUUID();
      Species species = SpeciesFixture.builder().id(speciesId).build();
      PetRequestDTO dto = PetsFixture.requestDtoBuilder().specieId(speciesId).build();
      Pets expectedPet = PetsFixture.builder().id(petId).species(species).build();

      when(speciesRepository.findById(speciesId)).thenReturn(Optional.of(species));
      when(tagRepository.findAllById(anyList())).thenReturn(List.of());
      when(petRepository.saveAndFlush(any(Pets.class))).thenReturn(expectedPet);

      PetResponseDTO result = service.create(dto);

      assertThat(result.id()).isEqualTo(petId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenSpeciesDoesNotExist() {
      UUID nonExistentSpeciesId = UUID.randomUUID();
      PetRequestDTO dto = PetsFixture.requestDtoBuilder().specieId(nonExistentSpeciesId).build();

      when(speciesRepository.findById(nonExistentSpeciesId)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> service.create(dto))
          .isInstanceOf(NotFoundException.class)
          .hasMessageContaining("Species not found: " + nonExistentSpeciesId);

      verify(petRepository, never()).saveAndFlush(any());
    }

    @Test
    void shouldCreatePetWithTags() {
      UUID speciesId = UUID.randomUUID();
      Tags tag1 = TagsFixture.builder().build();
      Tags tag2 = TagsFixture.builder().build();
      List<UUID> tagIds = List.of(tag1.getId(), tag2.getId());
      List<Tags> tags = List.of(tag1, tag2);

      Species species = SpeciesFixture.builder().id(speciesId).build();
      PetRequestDTO dto =
          PetsFixture.requestDtoBuilder().specieId(speciesId).tagsIds(tagIds).build();
      Pets expectedPet = PetsFixture.builder().species(species).tags(tags).build();

      when(speciesRepository.findById(speciesId)).thenReturn(Optional.of(species));
      when(tagRepository.findAllById(tagIds)).thenReturn(tags);
      when(petRepository.saveAndFlush(any(Pets.class))).thenReturn(expectedPet);

      PetResponseDTO result = service.create(dto);

      assertThat(result.tags()).hasSize(2);
    }

    @Test
    void shouldCreatePetWithEmptyTagsList() {
      UUID speciesId = UUID.randomUUID();
      Species species = SpeciesFixture.builder().id(speciesId).build();
      PetRequestDTO dto =
          PetsFixture.requestDtoBuilder().specieId(speciesId).tagsIds(List.of()).build();
      Pets expectedPet = PetsFixture.builder().species(species).tags(List.of()).build();

      when(speciesRepository.findById(speciesId)).thenReturn(Optional.of(species));
      when(tagRepository.findAllById(anyList())).thenReturn(List.of());
      when(petRepository.saveAndFlush(any(Pets.class))).thenReturn(expectedPet);

      PetResponseDTO result = service.create(dto);

      assertThat(result.tags()).isEmpty();
    }

    @Test
    void shouldCreatePetWithCorrectStatus() {
      UUID speciesId = UUID.randomUUID();
      Species species = SpeciesFixture.builder().id(speciesId).build();
      PetRequestDTO dto =
          PetsFixture.requestDtoBuilder().specieId(speciesId).status(PetStatus.AVAILABLE).build();
      Pets expectedPet = PetsFixture.builder().species(species).status(PetStatus.AVAILABLE).build();

      when(speciesRepository.findById(speciesId)).thenReturn(Optional.of(species));
      when(tagRepository.findAllById(anyList())).thenReturn(List.of());
      when(petRepository.saveAndFlush(any(Pets.class))).thenReturn(expectedPet);

      PetResponseDTO result = service.create(dto);

      assertThat(result.status()).isEqualTo(PetStatus.AVAILABLE);
    }

    @Test
    void shouldReturnExactPetReturnedByRepository() {
      UUID speciesId = UUID.randomUUID();
      UUID persistedId = UUID.randomUUID();
      Species species = SpeciesFixture.builder().id(speciesId).build();
      PetRequestDTO dto = PetsFixture.requestDtoBuilder().specieId(speciesId).build();
      Pets persistedPet = PetsFixture.builder().id(persistedId).species(species).build();

      when(speciesRepository.findById(speciesId)).thenReturn(Optional.of(species));
      when(tagRepository.findAllById(anyList())).thenReturn(List.of());
      when(petRepository.saveAndFlush(any(Pets.class))).thenReturn(persistedPet);

      PetResponseDTO result = service.create(dto);

      assertThat(result.id()).isEqualTo(persistedId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenSomeTagsDoNotExist() {
      UUID speciesId = UUID.randomUUID();
      UUID tagId1 = UUID.randomUUID();
      UUID tagId2 = UUID.randomUUID();
      Tags tag1 = TagsFixture.builder().id(tagId1).build();

      Species species = SpeciesFixture.builder().id(speciesId).build();
      PetRequestDTO dto =
          PetsFixture.requestDtoBuilder()
              .specieId(speciesId)
              .tagsIds(List.of(tagId1, tagId2))
              .build();

      when(speciesRepository.findById(speciesId)).thenReturn(Optional.of(species));
      when(tagRepository.findAllById(List.of(tagId1, tagId2))).thenReturn(List.of(tag1));

      assertThatThrownBy(() -> service.create(dto))
          .isInstanceOf(NotFoundException.class)
          .hasMessageContaining("Tags not found");

      verify(petRepository, never()).saveAndFlush(any());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenAllTagsDoNotExist() {
      UUID speciesId = UUID.randomUUID();
      UUID tagId1 = UUID.randomUUID();
      UUID tagId2 = UUID.randomUUID();

      Species species = SpeciesFixture.builder().id(speciesId).build();
      PetRequestDTO dto =
          PetsFixture.requestDtoBuilder()
              .specieId(speciesId)
              .tagsIds(List.of(tagId1, tagId2))
              .build();

      when(speciesRepository.findById(speciesId)).thenReturn(Optional.of(species));
      when(tagRepository.findAllById(List.of(tagId1, tagId2))).thenReturn(List.of());

      assertThatThrownBy(() -> service.create(dto))
          .isInstanceOf(NotFoundException.class)
          .hasMessageContaining("Tags not found");

      verify(petRepository, never()).saveAndFlush(any());
    }
  }

  @Nested
  class FindPetsTests {
    @Test
    void shouldReturnPaginatedListOfPets() {
      List<Pets> pets =
          List.of(PetsFixture.builder().build(), PetsFixture.builder().name("Lily").build());

      when(petRepository.findAll(any(Pageable.class))).thenReturn(PageFixture.getPageMock(pets));

      PageResponseDTO<PetResponseDTO> response =
          service.findAll(PageFixture.pageableRequestDtoBuilder().build());

      List<UUID> petIds = pets.stream().map(Pets::getId).toList();
      List<UUID> responseIds = response.content().stream().map(PetResponseDTO::id).toList();

      assertThat(responseIds).containsExactlyInAnyOrderElementsOf(petIds);
      assertEquals(DEFAULT_SIZE, response.size());
      assertEquals(DEFAULT_PAGE, response.number());
    }

    @Test
    void shouldReturnEmptyPageWhenNoPetsFound() {
      when(petRepository.findAll(any(Pageable.class)))
          .thenReturn(PageFixture.getPageMock(List.of()));

      PageResponseDTO<PetResponseDTO> response =
          service.findAll(PageFixture.pageableRequestDtoBuilder().build());

      assertThat(response.content()).isEmpty();
      assertEquals(DEFAULT_SIZE, response.size());
      assertEquals(DEFAULT_PAGE, response.number());
    }
  }

  @Nested
  class FindPetTests {
    @Test
    void shouldReturnPetByIdSuccessfully() {
      Pets pet = PetsFixture.builder().build();

      when(petRepository.findById(pet.getId())).thenReturn(Optional.of(pet));

      PetResponseDTO result = service.find(pet.getId());

      assertThat(result.id()).isEqualTo(pet.getId());
      assertThat(result.name()).isEqualTo(pet.getName());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenPetDoesNotExist() {
      UUID petId = UUID.randomUUID();

      when(petRepository.findById(petId)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> service.find(petId))
          .isInstanceOf(NotFoundException.class)
          .hasMessageContaining("Pet not found");
    }
  }

  @Nested
  class UpdatePetTests {
    @Test
    void shouldUpdatePetSuccessfully() {
      UUID petId = UUID.randomUUID();
      UUID speciesId = UUID.randomUUID();
      Tags tag = TagsFixture.builder().build();
      List<Tags> tags = List.of(tag);

      Pets existingPet = PetsFixture.builder().id(petId).build();
      Species species = SpeciesFixture.builder().id(speciesId).name("Cachorro").build();

      PetRequestDTO updateRequest =
          PetsFixture.requestDtoBuilder()
              .name("Rex Atualizado")
              .specieId(speciesId)
              .status(PetStatus.ADOPTED)
              .tagsIds(List.of(tag.getId()))
              .build();

      Pets updatedPet =
          PetsFixture.builder()
              .id(petId)
              .name("Rex Atualizado")
              .species(species)
              .status(PetStatus.ADOPTED)
              .tags(tags)
              .build();

      when(petRepository.findById(petId)).thenReturn(Optional.of(existingPet));
      when(speciesRepository.findById(speciesId)).thenReturn(Optional.of(species));
      when(tagRepository.findAllById(List.of(tag.getId()))).thenReturn(tags);
      when(petRepository.saveAndFlush(any(Pets.class))).thenReturn(updatedPet);

      PetResponseDTO response = service.update(petId, updateRequest);

      assertThat(response.id()).isEqualTo(petId);
      assertThat(response.name()).isEqualTo(updateRequest.name());
      assertThat(response.status()).isEqualTo(updateRequest.status());
      assertThat(response.species()).isEqualTo(species.getName());
      assertThat(response.tags()).containsExactly(tag.getName());

      verify(petRepository, times(1)).saveAndFlush(any(Pets.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenPetDoesNotExist() {
      UUID petId = UUID.randomUUID();
      PetRequestDTO updateRequest = PetsFixture.requestDtoBuilder().build();

      when(petRepository.findById(petId)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> service.update(petId, updateRequest))
          .isInstanceOf(NotFoundException.class)
          .hasMessageContaining("Pet not found");

      verify(petRepository, never()).saveAndFlush(any());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenSpeciesDoesNotExist() {
      UUID petId = UUID.randomUUID();
      UUID speciesId = UUID.randomUUID();

      Pets existingPet = PetsFixture.builder().id(petId).build();
      PetRequestDTO updateRequest = PetsFixture.requestDtoBuilder().specieId(speciesId).build();

      when(petRepository.findById(petId)).thenReturn(Optional.of(existingPet));
      when(speciesRepository.findById(speciesId)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> service.update(petId, updateRequest))
          .isInstanceOf(NotFoundException.class)
          .hasMessageContaining("Species not found");

      verify(petRepository, never()).saveAndFlush(any());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTagDoesNotExist() {
      UUID petId = UUID.randomUUID();
      UUID speciesId = UUID.randomUUID();
      UUID tagId = UUID.randomUUID();

      Pets existingPet = PetsFixture.builder().id(petId).build();
      Species species = SpeciesFixture.builder().id(speciesId).build();
      PetRequestDTO updateRequest =
          PetsFixture.requestDtoBuilder().specieId(speciesId).tagsIds(List.of(tagId)).build();

      when(petRepository.findById(petId)).thenReturn(Optional.of(existingPet));
      when(speciesRepository.findById(speciesId)).thenReturn(Optional.of(species));
      when(tagRepository.findAllById(List.of(tagId))).thenReturn(List.of());

      assertThatThrownBy(() -> service.update(petId, updateRequest))
          .isInstanceOf(NotFoundException.class)
          .hasMessageContaining("Tags not found");

      verify(petRepository, never()).saveAndFlush(any());
    }
  }
}
