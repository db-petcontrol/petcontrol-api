package br.com.db.petcontrol.service.impl;

import br.com.db.petcontrol.dto.request.PetRequestDTO;
import br.com.db.petcontrol.dto.response.PetResponseDTO;
import br.com.db.petcontrol.exception.NotFoundException;
import br.com.db.petcontrol.mapper.PetsMapper;
import br.com.db.petcontrol.model.Pets;
import br.com.db.petcontrol.model.Species;
import br.com.db.petcontrol.model.Tags;
import br.com.db.petcontrol.repository.PetsRepository;
import br.com.db.petcontrol.repository.SpeciesRepository;
import br.com.db.petcontrol.repository.TagsRepository;
import br.com.db.petcontrol.service.PetsService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetsServiceImpl implements PetsService {

  private final PetsRepository petRepository;
  private final SpeciesRepository speciesRepository;
  private final TagsRepository tagRepository;
  private final PetsMapper petsMapper;

  public PetResponseDTO create(PetRequestDTO dto) {
    List<String> errors = new ArrayList<>();

    Species species = speciesRepository.findById(dto.specieId()).orElse(null);
    if (species == null) {
      errors.add("Species not found: " + dto.specieId());
    }

    List<Tags> tags = findTagsOrCollectErrors(dto.tagsIds(), errors);

    if (!errors.isEmpty()) {
      throw new NotFoundException(errors);
    }

    Pets pet = petsMapper.toEntity(dto, species, tags);
    Pets savedPet = petRepository.saveAndFlush(pet);
    return petsMapper.toResponse(savedPet);
  }

  private List<Tags> findTagsOrCollectErrors(List<UUID> ids, List<String> errors) {
    if (ids == null || ids.isEmpty()) {
      return List.of();
    }

    List<Tags> foundTags = tagRepository.findAllById(ids);

    if (foundTags.size() != ids.size()) {
      List<UUID> notFoundIds =
          ids.stream()
              .filter(id -> foundTags.stream().noneMatch(tag -> tag.getId().equals(id)))
              .toList();

      errors.add("Tags not found: " + notFoundIds);
    }

    return foundTags;
  }
}
