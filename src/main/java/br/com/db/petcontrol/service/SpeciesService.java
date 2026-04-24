package br.com.db.petcontrol.service;

import br.com.db.petcontrol.dto.response.SpeciesResponseDTO;
import java.util.List;

public interface SpeciesService {
  List<SpeciesResponseDTO> getAllSpecies();
}
