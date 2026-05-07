package br.com.db.petcontrol.controller.impl;

import br.com.db.petcontrol.controller.PetsController;
import br.com.db.petcontrol.dto.request.PageableRequestDTO;
import br.com.db.petcontrol.dto.request.PetRequestDTO;
import br.com.db.petcontrol.dto.response.PageResponseDTO;
import br.com.db.petcontrol.dto.response.PetResponseDTO;
import br.com.db.petcontrol.service.PetsService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PetsControllerImpl implements PetsController {

  private final PetsService petsService;

  @Override
  public ResponseEntity<PetResponseDTO> create(PetRequestDTO dto) {
    PetResponseDTO response = petsService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Override
  public ResponseEntity<PetResponseDTO> update(UUID id, PetRequestDTO dto) {
    PetResponseDTO response = petsService.update(id, dto);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @Override
  public ResponseEntity<Void> delete(UUID id) {
    petsService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Override
  public ResponseEntity<PageResponseDTO<PetResponseDTO>> findAll(PageableRequestDTO pageable) {
    PageResponseDTO<PetResponseDTO> response = petsService.findAll(pageable);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @Override
  public ResponseEntity<PetResponseDTO> find(UUID id) {
    PetResponseDTO response = petsService.find(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
