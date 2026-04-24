package br.com.db.petcontrol.controlle.impl;

import br.com.db.petcontrol.controlle.SpeciesController;
import br.com.db.petcontrol.dto.response.SpeciesResponseDTO;
import br.com.db.petcontrol.service.SpeciesService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpeciesControllerImpl implements SpeciesController {

  private final SpeciesService service;

  public SpeciesControllerImpl(SpeciesService service) {
    this.service = service;
  }

  @Override
  public ResponseEntity<List<SpeciesResponseDTO>> getAll() {
    List<SpeciesResponseDTO> species = service.getAllSpecies();
    return ResponseEntity.ok(species);
  }
}
