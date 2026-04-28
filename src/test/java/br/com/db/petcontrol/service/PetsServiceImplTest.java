package br.com.db.petcontrol.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.db.petcontrol.dto.request.PetRequestDTO;
import br.com.db.petcontrol.exception.NotFoundException;
import br.com.db.petcontrol.mapper.PetsMapper;
import br.com.db.petcontrol.mocks.PetsFixture;
import br.com.db.petcontrol.mocks.SpeciesFixture;
import br.com.db.petcontrol.mocks.TagsFixture;
import br.com.db.petcontrol.model.Pets;
import br.com.db.petcontrol.model.Species;
import br.com.db.petcontrol.model.Tags;
import br.com.db.petcontrol.model.enums.PetStatus;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PetsServiceImplTest {

  @Mock private PetsRepository petRepository;
  @Mock private SpeciesRepository speciesRepository;
  @Mock private TagsRepository tagRepository;
  @Mock private PetsMapper petsMapper;

  private PetsServiceImpl service;

  @BeforeEach
  void setUp() {
    service = new PetsServiceImpl(petRepository, speciesRepository, tagRepository, petsMapper);
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
      when(petsMapper.toEntity(any(PetRequestDTO.class), any(Species.class), anyList()))
          .thenReturn(expectedPet);
      when(petRepository.saveAndFlush(any(Pets.class))).thenReturn(expectedPet);

      Pets result = service.create(dto);

      assertThat(result).isNotNull();
      assertThat(result.getName()).isEqualTo(expectedPet.getName());
      assertThat(result.getSpecies()).isEqualTo(species);
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
      when(petsMapper.toEntity(any(PetRequestDTO.class), any(Species.class), anyList()))
          .thenReturn(expectedPet);
      when(petRepository.saveAndFlush(any(Pets.class))).thenReturn(expectedPet);

      Pets result = service.create(dto);

      assertThat(result.getId()).isEqualTo(petId);
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
      verify(petsMapper, never()).toEntity(any(), any(), anyList());
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
      when(petsMapper.toEntity(dto, species, tags)).thenReturn(expectedPet);
      when(petRepository.saveAndFlush(expectedPet)).thenReturn(expectedPet);

      Pets result = service.create(dto);

      assertThat(result.getTags()).hasSize(2).containsExactlyInAnyOrder(tag1, tag2);
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
      when(petsMapper.toEntity(any(PetRequestDTO.class), any(Species.class), anyList()))
          .thenReturn(expectedPet);
      when(petRepository.saveAndFlush(any(Pets.class))).thenReturn(expectedPet);

      Pets result = service.create(dto);

      assertThat(result.getTags()).isEmpty();
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
      when(petsMapper.toEntity(any(PetRequestDTO.class), any(Species.class), anyList()))
          .thenReturn(expectedPet);
      when(petRepository.saveAndFlush(any(Pets.class))).thenReturn(expectedPet);

      Pets result = service.create(dto);

      assertThat(result.getStatus()).isEqualTo(PetStatus.AVAILABLE);
    }

    @Test
    void shouldCallAllRepositoriesWithCorrectArguments() {
      UUID speciesId = UUID.randomUUID();
      Tags tag = TagsFixture.builder().build();
      List<UUID> tagIds = List.of(tag.getId());
      List<Tags> tags = List.of(tag);

      Species species = SpeciesFixture.builder().id(speciesId).build();
      PetRequestDTO dto =
          PetsFixture.requestDtoBuilder().specieId(speciesId).tagsIds(tagIds).build();
      Pets expectedPet = PetsFixture.builder().build();

      when(speciesRepository.findById(speciesId)).thenReturn(Optional.of(species));
      when(tagRepository.findAllById(tagIds)).thenReturn(tags);
      when(petsMapper.toEntity(dto, species, tags)).thenReturn(expectedPet);
      when(petRepository.saveAndFlush(expectedPet)).thenReturn(expectedPet);

      service.create(dto);

      verify(speciesRepository).findById(speciesId);
      verify(tagRepository).findAllById(tagIds);
      verify(petsMapper).toEntity(dto, species, tags);
      verify(petRepository).saveAndFlush(expectedPet);
    }

    @Test
    void shouldReturnExactPetReturnedByRepository() {
      UUID speciesId = UUID.randomUUID();
      UUID persistedId = UUID.randomUUID();
      Species species = SpeciesFixture.builder().id(speciesId).build();
      PetRequestDTO dto = PetsFixture.requestDtoBuilder().specieId(speciesId).build();
      Pets mappedPet = PetsFixture.builder().build();
      Pets persistedPet = PetsFixture.builder().id(persistedId).species(species).build();

      when(speciesRepository.findById(speciesId)).thenReturn(Optional.of(species));
      when(tagRepository.findAllById(anyList())).thenReturn(List.of());
      when(petsMapper.toEntity(any(PetRequestDTO.class), any(Species.class), anyList()))
          .thenReturn(mappedPet);
      when(petRepository.saveAndFlush(any(Pets.class))).thenReturn(persistedPet);

      Pets result = service.create(dto);

      assertThat(result.getId()).isEqualTo(persistedId);
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
}
