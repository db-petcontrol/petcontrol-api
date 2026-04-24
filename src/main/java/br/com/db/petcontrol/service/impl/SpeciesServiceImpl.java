package br.com.db.petcontrol.service.impl;

import br.com.db.petcontrol.dto.response.SpeciesResponseDTO;
import br.com.db.petcontrol.mapper.SpeciesMapper;
import br.com.db.petcontrol.repository.SpeciesRepository;
import br.com.db.petcontrol.service.SpeciesService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SpeciesServiceImpl implements SpeciesService {

  private final SpeciesRepository repository;
  private final SpeciesMapper mapper;

  public SpeciesServiceImpl(SpeciesRepository repository, SpeciesMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public List<SpeciesResponseDTO> getAllSpecies() {
    return repository.findAllByOrderByNameAsc().stream().map(mapper::toDTO).toList();
  }
}
