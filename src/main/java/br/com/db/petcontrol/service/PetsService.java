package br.com.db.petcontrol.service;

import br.com.db.petcontrol.dto.request.PetRequestDTO;
import br.com.db.petcontrol.dto.response.PetResponseDTO;

public interface PetsService {
  PetResponseDTO create(PetRequestDTO dto);
}
