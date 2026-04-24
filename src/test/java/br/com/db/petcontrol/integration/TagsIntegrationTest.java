package br.com.db.petcontrol.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.db.petcontrol.dto.response.TagsResponseDTO;
import br.com.db.petcontrol.model.Tags;
import br.com.db.petcontrol.repository.TagsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class TagsIntegrationTest {

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private TagsRepository repository;

  private static final String TAGS_URL = "/tags";

  @AfterEach
  void tearDown() {
    repository.deleteAll();
  }

  @Test
  void shouldReturnTagsOrderedByNameAsc() {
    repository.save(new Tags(null, "adaptado a apartamento"));
    repository.save(new Tags(null, "amigável"));
    repository.save(new Tags(null, "castrado"));

    ResponseEntity<TagsResponseDTO[]> response =
        restTemplate.getForEntity(TAGS_URL, TagsResponseDTO[].class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertThat(response.getBody()).hasSize(3);
    assertThat(response.getBody()[0].name()).isEqualTo("adaptado a apartamento");
    assertThat(response.getBody()[1].name()).isEqualTo("amigável");
    assertThat(response.getBody()[2].name()).isEqualTo("castrado");
  }

  @Test
  void shouldReturnEmptyListWhenNoTagsExist() {
    ResponseEntity<TagsResponseDTO[]> response =
        restTemplate.getForEntity(TAGS_URL, TagsResponseDTO[].class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertThat(response.getBody()).isEmpty();
  }

  @Test
  void shouldReturnMultipleTagsWithCorrectIds() {
    Tags tags1 = repository.save(new Tags(null, "adaptado a apartamento"));
    Tags tags2 = repository.save(new Tags(null, "amigável"));

    ResponseEntity<TagsResponseDTO[]> response =
        restTemplate.getForEntity(TAGS_URL, TagsResponseDTO[].class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertThat(response.getBody()).hasSize(2);
    assertThat(response.getBody()[0].id()).isEqualTo(tags1.getId());
    assertThat(response.getBody()[1].id()).isEqualTo(tags2.getId());
  }
}
