package br.com.db.petcontrol.service.impl;

import br.com.db.petcontrol.dto.request.PetRequestDTO;
import br.com.db.petcontrol.exception.NotFoundException;
import br.com.db.petcontrol.mapper.PetsMapper;
import br.com.db.petcontrol.model.Pets;
import br.com.db.petcontrol.model.Species;
import br.com.db.petcontrol.model.Tags;
import br.com.db.petcontrol.repository.PetsRepository;
import br.com.db.petcontrol.repository.SpeciesRepository;
import br.com.db.petcontrol.repository.TagsRepository;
import br.com.db.petcontrol.service.PetsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetsServiceImpl implements PetsService {

  private final PetsRepository petRepository;
  private final SpeciesRepository speciesRepository;
  private final TagsRepository tagRepository;
  private final PetsMapper petsMapper;

  public Pets create(PetRequestDTO dto) {
    Species species =
        speciesRepository
            .findById(dto.specieId())
            .orElseThrow(() -> new NotFoundException("Species not found: " + dto.specieId()));

    List<Tags> tags = tagRepository.findAllById(dto.tagsIds());

    Pets pet = petsMapper.toEntity(dto, species, tags);
    return petRepository.saveAndFlush(pet);
  }
}
