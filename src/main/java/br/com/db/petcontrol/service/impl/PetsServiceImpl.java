package br.com.db.petcontrol.service.impl;

import br.com.db.petcontrol.dto.request.PageableRequestDTO;
import br.com.db.petcontrol.dto.request.PetRequestDTO;
import br.com.db.petcontrol.dto.response.PageResponseDTO;
import br.com.db.petcontrol.dto.response.PetResponseDTO;
import br.com.db.petcontrol.exception.NotFoundException;
import br.com.db.petcontrol.mapper.PageMapper;
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
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PetsServiceImpl implements PetsService {

  private final PetsRepository petRepository;
  private final SpeciesRepository speciesRepository;
  private final TagsRepository tagRepository;
  private final PetsMapper petsMapper;
  private final PageMapper pageMapper;

  @Override
  public PetResponseDTO create(PetRequestDTO dto) {
    List<String> errors = new ArrayList<>();

    Species species = findSpeciesOrCollectErrors(dto.specieId(), errors);
    List<Tags> tags = findTagsOrCollectErrors(dto.tagsIds(), errors);

    throwingNotFoundException(errors);

    Pets pet = petsMapper.toEntity(dto, species, tags);
    Pets savedPet = petRepository.saveAndFlush(pet);
    return petsMapper.toResponse(savedPet);
  }

  @Override
  @Transactional
  public PetResponseDTO update(UUID id, PetRequestDTO dto) {
    List<String> errors = new ArrayList<>();

    Pets pet = findPet(id);
    Species species = findSpeciesOrCollectErrors(dto.specieId(), errors);
    List<Tags> tags = findTagsOrCollectErrors(dto.tagsIds(), errors);

    throwingNotFoundException(errors);

    petsMapper.toUpdateEntity(dto, pet);
    updateRelationship(pet, species, tags);

    Pets updatedPet = petRepository.saveAndFlush(pet);
    return petsMapper.toResponse(updatedPet);
  }

  @Override
  public PageResponseDTO<PetResponseDTO> findAll(PageableRequestDTO pageable) {
    Page<PetResponseDTO> page =
        petRepository.findAll(pageMapper.toPageable(pageable)).map(petsMapper::toResponse);
    return pageMapper.toPageResponseDTO(page);
  }

  @Override
  public PetResponseDTO find(UUID id) {
    return petsMapper.toResponse(findPet(id));
  }

  private void updateRelationship(Pets pet, Species species, List<Tags> tags) {
    pet.setSpecies(species);

    pet.getTags().clear();
    pet.getTags().addAll(tags);
  }

  private void throwingNotFoundException(List<String> errors) {
    if (!errors.isEmpty()) {
      throw new NotFoundException(errors);
    }
  }

  private Species findSpeciesOrCollectErrors(UUID specieId, List<String> errors) {
    Species species = speciesRepository.findById(specieId).orElse(null);

    if (Objects.isNull(species)) {
      errors.add("Species not found: " + specieId);
    }

    return species;
  }

  private List<Tags> findTagsOrCollectErrors(List<UUID> ids, List<String> errors) {
    if (Objects.isNull(ids) || ids.isEmpty()) {
      return List.of();
    }

    List<Tags> foundTags = tagRepository.findAllById(ids);

    if (foundTags.size() != ids.size()) {
      List<UUID> foundIds = foundTags.stream().map(Tags::getId).toList();

      List<UUID> notFoundIds = ids.stream().filter(id -> !foundIds.contains(id)).toList();

      errors.add("Tags not found: " + notFoundIds);
    }

    return foundTags;
  }

  private Pets findPet(UUID id) {
    return petRepository.findById(id).orElseThrow(() -> new NotFoundException("Pet not found"));
  }
}
