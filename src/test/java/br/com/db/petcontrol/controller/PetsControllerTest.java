package br.com.db.petcontrol.controller;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.db.petcontrol.controller.impl.PetsControllerImpl;
import br.com.db.petcontrol.dto.request.PageableRequestDTO;
import br.com.db.petcontrol.dto.request.PetRequestDTO;
import br.com.db.petcontrol.dto.response.PageResponseDTO;
import br.com.db.petcontrol.dto.response.PetResponseDTO;
import br.com.db.petcontrol.dto.response.SpeciesResponseDTO;
import br.com.db.petcontrol.enums.PetStatus;
import br.com.db.petcontrol.exception.NotFoundException;
import br.com.db.petcontrol.mocks.PageFixture;
import br.com.db.petcontrol.mocks.PetsFixture;
import br.com.db.petcontrol.mocks.SpeciesFixture;
import br.com.db.petcontrol.service.PetsService;
import br.com.db.petcontrol.utils.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PetsControllerImpl.class)
class PetsControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private PetsService petsService;

  private static final String PETS_URL = "/pets";
  private static final String PET_ID_URL = PETS_URL + "/{id}";

  @Nested
  class CreatePetTests {

    @Test
    void shouldCreatePetAndReturnCreatedStatus() throws Exception {
      PetRequestDTO request = PetsFixture.requestDtoBuilder().build();
      PetResponseDTO response = PetsFixture.responseDtoBuilder().build();

      when(petsService.create(any(PetRequestDTO.class))).thenReturn(response);

      mockMvc
          .perform(
              post(PETS_URL)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isCreated())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldReturnPetWithCorrectNameInResponse() throws Exception {
      PetRequestDTO request = PetsFixture.requestDtoBuilder().name("Bolinha").build();
      PetResponseDTO response = PetsFixture.responseDtoBuilder().name("Bolinha").build();

      when(petsService.create(any(PetRequestDTO.class))).thenReturn(response);

      mockMvc
          .perform(
              post(PETS_URL)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.name").value("Bolinha"));
    }

    @Test
    void shouldReturnPetWithCorrectIdInResponse() throws Exception {
      UUID petId = UUID.randomUUID();
      PetRequestDTO request = PetsFixture.requestDtoBuilder().build();
      PetResponseDTO response = PetsFixture.responseDtoBuilder().id(petId).build();

      when(petsService.create(any(PetRequestDTO.class))).thenReturn(response);

      mockMvc
          .perform(
              post(PETS_URL)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.id").value(petId.toString()));
    }

    @Test
    void shouldReturnPetWithCorrectStatusInResponse() throws Exception {
      PetRequestDTO request = PetsFixture.requestDtoBuilder().status(PetStatus.AVAILABLE).build();
      PetResponseDTO response =
          PetsFixture.responseDtoBuilder().status(PetStatus.AVAILABLE).build();

      when(petsService.create(any(PetRequestDTO.class))).thenReturn(response);

      mockMvc
          .perform(
              post(PETS_URL)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.status").value(PetStatus.AVAILABLE.name()));
    }

    @Test
    void shouldReturnPetWithCorrectSpeciesInResponse() throws Exception {
      PetRequestDTO request = PetsFixture.requestDtoBuilder().build();
      SpeciesResponseDTO speciesResponse = SpeciesFixture.dtoBuilder().name("Gato").build();
      PetResponseDTO response = PetsFixture.responseDtoBuilder().species(speciesResponse).build();

      when(petsService.create(any(PetRequestDTO.class))).thenReturn(response);

      mockMvc
          .perform(
              post(PETS_URL)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.species.name").value("Gato"));
    }

    @Test
    void shouldReturnPetWithEmptyTagsInResponse() throws Exception {
      PetRequestDTO request = PetsFixture.requestDtoBuilder().tagsIds(List.of()).build();
      PetResponseDTO response = PetsFixture.responseDtoBuilder().tags(List.of()).build();

      when(petsService.create(any(PetRequestDTO.class))).thenReturn(response);

      mockMvc
          .perform(
              post(PETS_URL)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.tags", hasSize(0)));
    }

    @Test
    void shouldCallServiceWithCorrectArguments() throws Exception {
      PetRequestDTO request = PetsFixture.requestDtoBuilder().build();
      PetResponseDTO response = PetsFixture.responseDtoBuilder().build();

      when(petsService.create(any(PetRequestDTO.class))).thenReturn(response);

      mockMvc
          .perform(
              post(PETS_URL)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isCreated());

      verify(petsService).create(any(PetRequestDTO.class));
    }
  }

  @Nested
  class FindPetsTests {
    @ParameterizedTest
    @MethodSource("petsProviders")
    void shouldReturnAllPetsWithCorrectPagination(List<PetResponseDTO> pets) throws Exception {
      PageableRequestDTO pageable = PageFixture.pageableRequestDtoBuilder().build();
      PageResponseDTO page = PageFixture.pageResponseDtoBuilder(pets).build();

      when(petsService.findAll(pageable)).thenReturn(page);

      mockMvc
          .perform(get(PETS_URL).contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(content().json(JsonUtils.convertToJson(page)));
    }

    static Stream<List<PetResponseDTO>> petsProviders() {
      return Stream.of(
          List.of(
              PetsFixture.responseDtoBuilder().build(), PetsFixture.responseDtoBuilder().build()),
          List.of());
    }
  }

  @Nested
  class FindPetTests {
    @Test
    void shouldReturnPetByIdSuccessfully() throws Exception {
      PetResponseDTO response = PetsFixture.responseDtoBuilder().build();

      when(petsService.find(response.id())).thenReturn(response);

      mockMvc
          .perform(get(PET_ID_URL, response.id()).contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(content().json(JsonUtils.convertToJson(response)));
    }

    @Test
    void shouldReturnNotFoundWhenPetDoesNotExist() throws Exception {
      UUID invalidId = UUID.randomUUID();
      String errorMessage = "Pet not found";

      when(petsService.find(invalidId)).thenThrow(new NotFoundException(errorMessage));

      mockMvc
          .perform(get(PET_ID_URL, invalidId).contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.messages[0]").value(errorMessage));
    }

    @Test
    void shouldReturnBadRequestWhenIdIsInvalid() throws Exception {
      String invalidId = "1";
      String expectedMessage = "Valor inválido '1' para o parâmetro 'id'";

      mockMvc
          .perform(get(PET_ID_URL, invalidId).contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
    }
  }

  @Nested
  class UpdatePetTests {

    @Test
    void shouldUpdatePetSuccessfully() throws Exception {
      UUID petId = UUID.randomUUID();

      PetRequestDTO request =
          PetsFixture.requestDtoBuilder().name("Rex").status(PetStatus.AVAILABLE).build();

      PetResponseDTO response =
          PetsFixture.responseDtoBuilder()
              .id(petId)
              .name("Rex Atualizado")
              .status(PetStatus.ADOPTED)
              .build();

      when(petsService.update(petId, request)).thenReturn(response);

      mockMvc
          .perform(
              put(PET_ID_URL, petId)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.convertToJson(request)))
          .andExpect(status().isOk())
          .andExpect(content().json(JsonUtils.convertToJson(response)));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonexistentPet() throws Exception {
      UUID invalidId = UUID.randomUUID();
      PetRequestDTO request = PetsFixture.requestDtoBuilder().build();
      String errorMessage = "Pet not found";

      when(petsService.update(invalidId, request)).thenThrow(new NotFoundException(errorMessage));

      mockMvc
          .perform(
              put(PET_ID_URL, invalidId)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.convertToJson(request)))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.messages[0]").value(errorMessage));
    }

    @ParameterizedTest
    @MethodSource("invalidPetRequestsProvider")
    void shouldReturnBadRequestForInvalidRequest(PetRequestDTO invalidRequest) throws Exception {
      UUID petId = UUID.randomUUID();

      mockMvc
          .perform(
              put(PET_ID_URL, petId)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.convertToJson(invalidRequest)))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.messages.length()").value(greaterThan(0)));
    }

    static Stream<PetRequestDTO> invalidPetRequestsProvider() {
      return Stream.of(
          PetsFixture.requestDtoBuilder().name(null).build(),
          PetsFixture.requestDtoBuilder().name("a".repeat(65)).build(),
          PetsFixture.requestDtoBuilder().name("Rex@#%$!").build(),
          PetsFixture.requestDtoBuilder().name("Rex123").build(),
          PetsFixture.requestDtoBuilder().specieId(null).build(),
          PetsFixture.requestDtoBuilder().status(null).build());
    }
  }

  @Nested
  class DeletePetTests {
    @Test
    void shouldDeletePetSuccessfully() throws Exception {
      UUID petId = UUID.randomUUID();

      doNothing().when(petsService).delete(petId);

      mockMvc
          .perform(delete(PET_ID_URL, petId).contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNoContent())
          .andExpect(content().string(""));
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonexistentPet() throws Exception {
      UUID invalidId = UUID.randomUUID();
      String errorMessage = "Pet not found";

      doThrow(new NotFoundException(errorMessage)).when(petsService).delete(invalidId);

      mockMvc
          .perform(delete(PET_ID_URL, invalidId).contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.messages[0]").value(errorMessage));
    }
  }
}
