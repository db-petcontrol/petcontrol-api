package br.com.db.petcontrol.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.db.petcontrol.dto.response.SpeciesResponseDTO;
import br.com.db.petcontrol.model.Species;
import br.com.db.petcontrol.repository.SpeciesRepository;
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
class SpeciesIntegrationTest {

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private SpeciesRepository repository;

  private static final String SPECIES_URL = "/species";

  @AfterEach
  void tearDown() {
    repository.deleteAll();
  }

  @Test
  void shouldReturnSpeciesOrderedByNameAsc() {
    repository.save(new Species(null, "Gato"));
    repository.save(new Species(null, "Cachorro"));
    repository.save(new Species(null, "Calopsita"));

    ResponseEntity<SpeciesResponseDTO[]> response =
        restTemplate.getForEntity(SPECIES_URL, SpeciesResponseDTO[].class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertThat(response.getBody()).hasSize(3);
    assertThat(response.getBody()[0].name()).isEqualTo("Cachorro");
    assertThat(response.getBody()[1].name()).isEqualTo("Calopsita");
    assertThat(response.getBody()[2].name()).isEqualTo("Gato");
  }

  @Test
  void shouldReturnEmptyListWhenNoSpeciesExist() {
    ResponseEntity<SpeciesResponseDTO[]> response =
        restTemplate.getForEntity(SPECIES_URL, SpeciesResponseDTO[].class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertThat(response.getBody()).isEmpty();
  }

  @Test
  void shouldReturnMultipleSpeciesWithCorrectIds() {
    Species species1 = repository.save(new Species(null, "Ave"));
    Species species2 = repository.save(new Species(null, "Cachorro"));

    ResponseEntity<SpeciesResponseDTO[]> response =
        restTemplate.getForEntity(SPECIES_URL, SpeciesResponseDTO[].class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertThat(response.getBody()).hasSize(2);
    assertThat(response.getBody()[0].id()).isEqualTo(species1.getId());
    assertThat(response.getBody()[1].id()).isEqualTo(species2.getId());
  }
}
