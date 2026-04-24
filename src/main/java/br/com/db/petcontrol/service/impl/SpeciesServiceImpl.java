package br.com.db.petcontrol.service.impl;

import br.com.db.petcontrol.dto.response.SpeciesResponseDTO;
import br.com.db.petcontrol.repository.SpeciesRepository;
import br.com.db.petcontrol.service.SpeciesService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SpeciesServiceImpl implements SpeciesService {

  private final SpeciesRepository repository;

  public SpeciesServiceImpl(SpeciesRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<SpeciesResponseDTO> getAllSpecies() {
    return repository.findAllByOrderByNameAsc().stream()
        .map(species -> new SpeciesResponseDTO(species.getId(), species.getName()))
        .toList();
  }
}
