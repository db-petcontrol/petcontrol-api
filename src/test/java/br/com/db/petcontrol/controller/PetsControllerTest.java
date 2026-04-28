package br.com.db.petcontrol.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.db.petcontrol.controller.impl.PetsControllerImpl;
import br.com.db.petcontrol.dto.request.PetRequestDTO;
import br.com.db.petcontrol.dto.response.PetResponseDTO;
import br.com.db.petcontrol.mapper.PetsMapper;
import br.com.db.petcontrol.mocks.PetsFixture;
import br.com.db.petcontrol.model.Pets;
import br.com.db.petcontrol.model.enums.PetStatus;
import br.com.db.petcontrol.service.PetsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
  @MockitoBean private PetsMapper petsMapper;

  private static final String PETS_URL = "/pets";

  @Nested
  class CreatePetTests {

    @Test
    void shouldCreatePetAndReturnCreatedStatus() throws Exception {
      PetRequestDTO request = PetsFixture.requestDtoBuilder().build();
      PetResponseDTO response = PetsFixture.responseDtoBuilder().build();
      Pets createdPet = PetsFixture.builder().build();

      when(petsService.create(any(PetRequestDTO.class))).thenReturn(createdPet);
      when(petsMapper.toResponse(createdPet)).thenReturn(response);

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
      Pets createdPet = PetsFixture.builder().name("Bolinha").build();
      PetResponseDTO response = PetsFixture.responseDtoBuilder().name("Bolinha").build();

      when(petsService.create(any(PetRequestDTO.class))).thenReturn(createdPet);
      when(petsMapper.toResponse(createdPet)).thenReturn(response);

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
      Pets createdPet = PetsFixture.builder().id(petId).build();
      PetResponseDTO response = PetsFixture.responseDtoBuilder().id(petId).build();

      when(petsService.create(any(PetRequestDTO.class))).thenReturn(createdPet);
      when(petsMapper.toResponse(createdPet)).thenReturn(response);

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
      Pets createdPet = PetsFixture.builder().status(PetStatus.AVAILABLE).build();
      PetResponseDTO response =
          PetsFixture.responseDtoBuilder().status(PetStatus.AVAILABLE).build();

      when(petsService.create(any(PetRequestDTO.class))).thenReturn(createdPet);
      when(petsMapper.toResponse(createdPet)).thenReturn(response);

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
      Pets createdPet = PetsFixture.builder().build();
      PetResponseDTO response = PetsFixture.responseDtoBuilder().species("Gato").build();

      when(petsService.create(any(PetRequestDTO.class))).thenReturn(createdPet);
      when(petsMapper.toResponse(createdPet)).thenReturn(response);

      mockMvc
          .perform(
              post(PETS_URL)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.species").value("Gato"));
    }

    @Test
    void shouldReturnPetWithEmptyTagsInResponse() throws Exception {
      PetRequestDTO request = PetsFixture.requestDtoBuilder().tagsIds(List.of()).build();
      Pets createdPet = PetsFixture.builder().build();
      PetResponseDTO response = PetsFixture.responseDtoBuilder().tags(List.of()).build();

      when(petsService.create(any(PetRequestDTO.class))).thenReturn(createdPet);
      when(petsMapper.toResponse(createdPet)).thenReturn(response);

      mockMvc
          .perform(
              post(PETS_URL)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.tags", hasSize(0)));
    }

    @Test
    void shouldCallServiceAndMapperWithCorrectArguments() throws Exception {
      PetRequestDTO request = PetsFixture.requestDtoBuilder().build();
      Pets createdPet = PetsFixture.builder().build();
      PetResponseDTO response = PetsFixture.responseDtoBuilder().build();

      when(petsService.create(any(PetRequestDTO.class))).thenReturn(createdPet);
      when(petsMapper.toResponse(createdPet)).thenReturn(response);

      mockMvc
          .perform(
              post(PETS_URL)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isCreated());

      verify(petsService).create(any(PetRequestDTO.class));
      verify(petsMapper).toResponse(createdPet);
    }
  }
}
