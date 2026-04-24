package br.com.db.petcontrol.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.db.petcontrol.controller.impl.TagsControllerImpl;
import br.com.db.petcontrol.dto.response.TagsResponseDTO;
import br.com.db.petcontrol.mocks.TagsFixture;
import br.com.db.petcontrol.service.TagsService;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TagsControllerImpl.class)
class TagsControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private TagsService service;

  private static final String TAGS_URL = "/tags";

  @Nested
  class GetAllTagsTests {

    @Test
    void shouldListAllTagsSuccessfully() throws Exception {
      TagsResponseDTO tags1 = TagsFixture.dtoBuilder().name("adaptado a apartamento").build();
      TagsResponseDTO tags2 = TagsFixture.dtoBuilder().name("amigável").build();
      TagsResponseDTO tags3 = TagsFixture.dtoBuilder().name("castrado").build();

      when(service.getAllTags()).thenReturn(List.of(tags1, tags2, tags3));

      mockMvc
          .perform(get(TAGS_URL).contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$", hasSize(3)))
          .andExpect(jsonPath("$[0].name").value("adaptado a apartamento"))
          .andExpect(jsonPath("$[1].name").value("amigável"))
          .andExpect(jsonPath("$[2].name").value("castrado"));
    }

    @Test
    void shouldReturnListWithOneTags() throws Exception {
      TagsResponseDTO tags = TagsFixture.dtoBuilder().name("amigável").build();

      when(service.getAllTags()).thenReturn(List.of(tags));

      mockMvc
          .perform(get(TAGS_URL).contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(1)))
          .andExpect(jsonPath("$[0].name").value("amigável"));
    }

    @Test
    void shouldReturnEmptyListWhenNoTagsExist() throws Exception {
      when(service.getAllTags()).thenReturn(List.of());

      mockMvc
          .perform(get(TAGS_URL).contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(0)));
    }
  }
}
