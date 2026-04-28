package br.com.db.petcontrol.service;

import br.com.db.petcontrol.dto.request.PetRequestDTO;
import br.com.db.petcontrol.model.Pets;

public interface PetsService {
  Pets create(PetRequestDTO dto);
}
