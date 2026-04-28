package br.com.db.petcontrol.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.db.petcontrol.dto.request.PetRequestDTO;
import br.com.db.petcontrol.dto.response.PetResponseDTO;
import br.com.db.petcontrol.model.Species;
import br.com.db.petcontrol.model.Tags;
import br.com.db.petcontrol.model.enums.PetStatus;
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
      assertThat(response.getBody().species()).isEqualTo("Cachorro");
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
}
