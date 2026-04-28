package br.com.db.petcontrol.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.db.petcontrol.controller.impl.SpeciesControllerImpl;
import br.com.db.petcontrol.dto.response.SpeciesResponseDTO;
import br.com.db.petcontrol.mocks.SpeciesFixture;
import br.com.db.petcontrol.service.SpeciesService;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SpeciesControllerImpl.class)
class SpeciesControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private SpeciesService service;

  private static final String SPECIES_URL = "/species";

  @Nested
  class GetAllSpeciesTests {

    @Test
    void shouldListAllSpeciesSuccessfully() throws Exception {
      SpeciesResponseDTO species1 = SpeciesFixture.dtoBuilder().name("Cachorro").build();
      SpeciesResponseDTO species2 = SpeciesFixture.dtoBuilder().name("Gato").build();
      SpeciesResponseDTO species3 = SpeciesFixture.dtoBuilder().name("Pássaro").build();

      when(service.getAllSpecies()).thenReturn(List.of(species1, species2, species3));

      mockMvc
          .perform(get(SPECIES_URL).contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$", hasSize(3)))
          .andExpect(jsonPath("$[0].name").value("Cachorro"))
          .andExpect(jsonPath("$[1].name").value("Gato"))
          .andExpect(jsonPath("$[2].name").value("Pássaro"));
    }

    @Test
    void shouldReturnListWithOneSpecies() throws Exception {
      SpeciesResponseDTO species = SpeciesFixture.dtoBuilder().name("Coelho").build();

      when(service.getAllSpecies()).thenReturn(List.of(species));

      mockMvc
          .perform(get(SPECIES_URL).contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(1)))
          .andExpect(jsonPath("$[0].name").value("Coelho"));
    }

    @Test
    void shouldReturnEmptyListWhenNoSpeciesExist() throws Exception {
      when(service.getAllSpecies()).thenReturn(List.of());

      mockMvc
          .perform(get(SPECIES_URL).contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(0)));
    }
  }
}
