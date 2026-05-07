package br.com.db.petcontrol.integration;

import static br.com.db.petcontrol.mocks.PageFixture.DEFAULT_PAGE;
import static br.com.db.petcontrol.mocks.PageFixture.DEFAULT_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.db.petcontrol.dto.request.PetRequestDTO;
import br.com.db.petcontrol.dto.response.PageResponseDTO;
import br.com.db.petcontrol.dto.response.PetResponseDTO;
import br.com.db.petcontrol.enums.PetStatus;
import br.com.db.petcontrol.mocks.PetsFixture;
import br.com.db.petcontrol.mocks.TagsFixture;
import br.com.db.petcontrol.model.Pets;
import br.com.db.petcontrol.model.Species;
import br.com.db.petcontrol.model.Tags;
import br.com.db.petcontrol.repository.PetsRepository;
import br.com.db.petcontrol.repository.SpeciesRepository;
import br.com.db.petcontrol.repository.TagsRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class PetsIntegrationTest {

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private PetsRepository petsRepository;
  @Autowired private SpeciesRepository speciesRepository;
  @Autowired private TagsRepository tagsRepository;

  private static final String PETS_URL = "/pets";
  private static final String PET_ID_URL = PETS_URL + "/{id}";

  private Species savedSpecies;

  @BeforeEach
  void setUp() {
    petsRepository.deleteAll();
    tagsRepository.deleteAll();
    speciesRepository.deleteAll();
    savedSpecies = speciesRepository.save(new Species(null, "Cachorro"));
  }

  @AfterEach
  void tearDown() {
    petsRepository.deleteAll();
    tagsRepository.deleteAll();
    speciesRepository.deleteAll();
  }

  @Nested
  class CreatePetTests {

    @Test
    void shouldCreatePetAndReturnCreatedStatus() {
      PetRequestDTO request =
          PetRequestDTO.builder()
              .name("Rex")
              .specieId(savedSpecies.getId())
              .status(PetStatus.AVAILABLE)
              .tagsIds(List.of())
              .build();

      ResponseEntity<PetResponseDTO> response =
          restTemplate.postForEntity(PETS_URL, request, PetResponseDTO.class);

      assertEquals(HttpStatus.CREATED, response.getStatusCode());
      assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldCreatePetAndPersistInDatabase() {
      PetRequestDTO request =
          PetRequestDTO.builder()
              .name("Bolinha")
              .specieId(savedSpecies.getId())
              .status(PetStatus.AVAILABLE)
              .tagsIds(List.of())
              .build();

      restTemplate.postForEntity(PETS_URL, request, PetResponseDTO.class);

      assertThat(petsRepository.findAll()).hasSize(1);
      assertThat(petsRepository.findAll().getFirst().getName()).isEqualTo("Bolinha");
    }

    @Test
    void shouldReturnPetWithCorrectName() {
      PetRequestDTO request =
          PetRequestDTO.builder()
              .name("Luna")
              .specieId(savedSpecies.getId())
              .status(PetStatus.AVAILABLE)
              .tagsIds(List.of())
              .build();

      ResponseEntity<PetResponseDTO> response =
          restTemplate.postForEntity(PETS_URL, request, PetResponseDTO.class);

      assertEquals(HttpStatus.CREATED, response.getStatusCode());
      assertThat(response.getBody().name()).isEqualTo("Luna");
    }

    @Test
    void shouldReturnPetWithGeneratedId() {
      PetRequestDTO request =
          PetRequestDTO.builder()
              .name("Max")
              .specieId(savedSpecies.getId())
              .status(PetStatus.AVAILABLE)
              .tagsIds(List.of())
              .build();

      ResponseEntity<PetResponseDTO> response =
          restTemplate.postForEntity(PETS_URL, request, PetResponseDTO.class);

      assertEquals(HttpStatus.CREATED, response.getStatusCode());
      assertThat(response.getBody().id()).isNotNull();
    }

    @Test
    void shouldReturnPetWithCorrectSpecies() {
      PetRequestDTO request =
          PetRequestDTO.builder()
              .name("Thor")
              .specieId(savedSpecies.getId())
              .status(PetStatus.AVAILABLE)
              .tagsIds(List.of())
              .build();

      ResponseEntity<PetResponseDTO> response =
          restTemplate.postForEntity(PETS_URL, request, PetResponseDTO.class);

      assertEquals(HttpStatus.CREATED, response.getStatusCode());
      assertThat(response.getBody().species().name()).isEqualTo("Cachorro");
    }

    @Test
    void shouldReturnPetWithCorrectStatus() {
      PetRequestDTO request =
          PetRequestDTO.builder()
              .name("Mel")
              .specieId(savedSpecies.getId())
              .status(PetStatus.AVAILABLE)
              .tagsIds(List.of())
              .build();

      ResponseEntity<PetResponseDTO> response =
          restTemplate.postForEntity(PETS_URL, request, PetResponseDTO.class);

      assertEquals(HttpStatus.CREATED, response.getStatusCode());
      assertThat(response.getBody().status()).isEqualTo(PetStatus.AVAILABLE);
    }

    @Test
    void shouldCreatePetWithTags() {
      Tags tag1 = tagsRepository.save(new Tags(null, "Vacinado"));
      Tags tag2 = tagsRepository.save(new Tags(null, "Castrado"));

      PetRequestDTO request =
          PetRequestDTO.builder()
              .name("Pipoca")
              .specieId(savedSpecies.getId())
              .status(PetStatus.AVAILABLE)
              .tagsIds(List.of(tag1.getId(), tag2.getId()))
              .build();

      ResponseEntity<PetResponseDTO> response =
          restTemplate.postForEntity(PETS_URL, request, PetResponseDTO.class);

      assertEquals(HttpStatus.CREATED, response.getStatusCode());
      assertThat(response.getBody().tags()).hasSize(2);
    }

    @Test
    void shouldCreatePetWithEmptyTags() {
      PetRequestDTO request =
          PetRequestDTO.builder()
              .name("Trovão")
              .specieId(savedSpecies.getId())
              .status(PetStatus.AVAILABLE)
              .tagsIds(List.of())
              .build();

      ResponseEntity<PetResponseDTO> response =
          restTemplate.postForEntity(PETS_URL, request, PetResponseDTO.class);

      assertEquals(HttpStatus.CREATED, response.getStatusCode());
      assertThat(response.getBody().tags()).isEmpty();
    }

    @Test
    void shouldReturnNotFoundWhenSpeciesDoesNotExist() {
      PetRequestDTO request =
          PetRequestDTO.builder()
              .name("Rex")
              .specieId(UUID.randomUUID())
              .status(PetStatus.AVAILABLE)
              .tagsIds(List.of())
              .build();

      ResponseEntity<PetResponseDTO> response =
          restTemplate.postForEntity(PETS_URL, request, PetResponseDTO.class);

      assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldReturnNotFoundWhenSomeTagsDoNotExist() {
      Tags tag1 = tagsRepository.save(new Tags(null, "Vacinado"));
      UUID nonExistentTagId = UUID.randomUUID();

      PetRequestDTO request =
          PetRequestDTO.builder()
              .name("Bingo")
              .specieId(savedSpecies.getId())
              .status(PetStatus.AVAILABLE)
              .tagsIds(List.of(tag1.getId(), nonExistentTagId))
              .build();

      ResponseEntity<PetResponseDTO> response =
          restTemplate.postForEntity(PETS_URL, request, PetResponseDTO.class);

      assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldReturnNotFoundWhenAllTagsDoNotExist() {
      UUID nonExistentTagId1 = UUID.randomUUID();
      UUID nonExistentTagId2 = UUID.randomUUID();

      PetRequestDTO request =
          PetRequestDTO.builder()
              .name("Spike")
              .specieId(savedSpecies.getId())
              .status(PetStatus.AVAILABLE)
              .tagsIds(List.of(nonExistentTagId1, nonExistentTagId2))
              .build();

      ResponseEntity<PetResponseDTO> response =
          restTemplate.postForEntity(PETS_URL, request, PetResponseDTO.class);

      assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldNotPersistPetWhenSpeciesDoesNotExist() {
      PetRequestDTO request =
          PetRequestDTO.builder()
              .name("Rex")
              .specieId(UUID.randomUUID())
              .status(PetStatus.AVAILABLE)
              .tagsIds(List.of())
              .build();

      restTemplate.postForEntity(PETS_URL, request, PetResponseDTO.class);

      assertThat(petsRepository.findAll()).isEmpty();
    }
  }

  @Nested
  class FindPetsTests {
    @Test
    void shouldReturnAllPersistedPetsWithCorrectPagination() {
      List<String> names = List.of("Rex", "Luna");
      List<Pets> pets = names.stream().map(name -> builderWithoutId().name(name).build()).toList();

      petsRepository.saveAll(pets);

      ResponseEntity<PageResponseDTO<PetResponseDTO>> response =
          restTemplate.exchange(
              PETS_URL,
              HttpMethod.GET,
              null,
              new ParameterizedTypeReference<PageResponseDTO<PetResponseDTO>>() {});

      List<UUID> savedIds = pets.stream().map(Pets::getId).toList();

      List<UUID> responseIds =
          response.getBody().content().stream().map(PetResponseDTO::id).toList();

      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertThat(responseIds).containsExactlyInAnyOrderElementsOf(savedIds);
      assertEquals(DEFAULT_SIZE, response.getBody().size());
      assertEquals(DEFAULT_PAGE, response.getBody().number());
    }

    @Test
    void shouldReturnEmptyPageWhenNoPetsArePersisted() {
      ResponseEntity<PageResponseDTO<PetResponseDTO>> response =
          restTemplate.exchange(
              PETS_URL,
              HttpMethod.GET,
              null,
              new ParameterizedTypeReference<PageResponseDTO<PetResponseDTO>>() {});

      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertThat(response.getBody().content()).isEmpty();
      assertEquals(DEFAULT_SIZE, response.getBody().size());
      assertEquals(DEFAULT_PAGE, response.getBody().number());
    }

    @Test
    void shouldReturnSecondPageWithCorrectPagination() {
      int numberPage = 1;
      int sizePage = 3;

      List<String> names = List.of("Rex", "Luna", "Max", "Mel");
      List<Pets> pets = names.stream().map(name -> builderWithoutId().name(name).build()).toList();

      petsRepository.saveAll(pets);

      String url = PETS_URL + String.format("?page=%d&size=%d", numberPage, sizePage);

      ResponseEntity<PageResponseDTO<PetResponseDTO>> response =
          restTemplate.exchange(
              url,
              HttpMethod.GET,
              null,
              new ParameterizedTypeReference<PageResponseDTO<PetResponseDTO>>() {});

      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertEquals(names.size(), response.getBody().totalElements());
      assertEquals(numberPage, response.getBody().number());
      assertEquals(sizePage, response.getBody().size());
      assertThat(response.getBody().content()).hasSize(1);
    }
  }

  @Nested
  class FindPetTests {
    @Test
    void shouldReturnPetByIdSuccessfully() {
      Pets pet = builderWithoutId().build();
      Pets savedPet = petsRepository.save(pet);

      ResponseEntity<PetResponseDTO> response =
          restTemplate.getForEntity(PET_ID_URL, PetResponseDTO.class, savedPet.getId());
      PetResponseDTO currentPet = response.getBody();

      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertThat(currentPet.id()).isEqualTo(savedPet.getId());
      assertThat(currentPet.name()).isEqualTo(savedPet.getName());
      assertThat(currentPet.species().name()).isEqualTo(savedPet.getSpecies().getName());
      assertThat(currentPet.status()).isEqualTo(savedPet.getStatus());
      assertThat(currentPet.tags()).isEmpty();
    }
  }

  @Nested
  class UpdatePetTests {
    @Test
    void shouldUpdatePetSuccessfully() {
      Pets pet = builderWithoutId().name("Rex").status(PetStatus.AVAILABLE).build();
      Pets savedPet = petsRepository.save(pet);

      Tags tag = tagsRepository.save(TagsFixture.builder().id(null).build());

      PetRequestDTO updateRequest =
          PetRequestDTO.builder()
              .name("Rex Atualizado")
              .specieId(savedSpecies.getId())
              .status(PetStatus.ADOPTED)
              .tagsIds(List.of(tag.getId()))
              .build();

      HttpEntity<PetRequestDTO> requestEntity = new HttpEntity<>(updateRequest);

      ResponseEntity<PetResponseDTO> response =
          restTemplate.exchange(
              PET_ID_URL, HttpMethod.PUT, requestEntity, PetResponseDTO.class, savedPet.getId());

      PetResponseDTO updatedPet = response.getBody();

      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertThat(updatedPet.name()).isEqualTo(updateRequest.name());
      assertThat(updatedPet.status()).isEqualTo(updateRequest.status());
      assertThat(updatedPet.species().name()).isEqualTo(savedSpecies.getName());
      assertThat(updatedPet.tags()).hasSize(1);
      assertThat(updatedPet.tags().getFirst().name()).isEqualTo(tag.getName());
    }
  }

  @Nested
  class DeletePetTests {
    @Test
    void shouldDeletePetSuccessfully() {
      Pets pet = builderWithoutId().build();
      Pets savedPet = petsRepository.save(pet);

      ResponseEntity<Void> response =
          restTemplate.exchange(PET_ID_URL, HttpMethod.DELETE, null, Void.class, savedPet.getId());

      assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
      assertThat(petsRepository.findById(savedPet.getId())).isEmpty();
    }
  }

  public Pets.PetsBuilder builderWithoutId() {
    return PetsFixture.builder().id(null).species(savedSpecies);
  }
}
