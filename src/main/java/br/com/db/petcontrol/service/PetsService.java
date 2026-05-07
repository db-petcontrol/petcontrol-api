package br.com.db.petcontrol.service;

import br.com.db.petcontrol.dto.request.PageableRequestDTO;
import br.com.db.petcontrol.dto.request.PetRequestDTO;
import br.com.db.petcontrol.dto.response.PageResponseDTO;
import br.com.db.petcontrol.dto.response.PetResponseDTO;
import java.util.UUID;

public interface PetsService {
  PetResponseDTO create(PetRequestDTO dto);

  PetResponseDTO update(UUID id, PetRequestDTO dto);

  void delete(UUID id);

  PageResponseDTO<PetResponseDTO> findAll(PageableRequestDTO pageable);

  PetResponseDTO find(UUID id);
}
