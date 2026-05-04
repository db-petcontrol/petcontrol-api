package br.com.db.petcontrol.service;

import br.com.db.petcontrol.dto.request.PageableRequestDTO;
import br.com.db.petcontrol.dto.request.PetRequestDTO;
import br.com.db.petcontrol.dto.response.PageResponseDTO;
import br.com.db.petcontrol.dto.response.PetResponseDTO;

public interface PetsService {
  PetResponseDTO create(PetRequestDTO dto);

  PageResponseDTO<PetResponseDTO> findAll(PageableRequestDTO pageable);
}
