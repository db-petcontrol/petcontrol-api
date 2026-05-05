package br.com.db.petcontrol.controller;

import br.com.db.petcontrol.dto.request.PageableRequestDTO;
import br.com.db.petcontrol.dto.request.PetRequestDTO;
import br.com.db.petcontrol.dto.response.ErrorResponseDTO;
import br.com.db.petcontrol.dto.response.PageResponseDTO;
import br.com.db.petcontrol.dto.response.PetResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Pets", description = "Operações para gerenciamento de pets")
@RequestMapping("/pets")
public interface PetsController {

  @Operation(summary = "Cadastrar pet", description = "Cadastra um novo pet no sistema")
  @ApiResponse(
      responseCode = "201",
      description = "Pet cadastrado com sucesso",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = PetResponseDTO.class)))
  @ApiResponse(responseCode = "404", description = "Espécie não encontrada")
  @PostMapping
  ResponseEntity<PetResponseDTO> create(@Valid @RequestBody PetRequestDTO dto);

  @Operation(summary = "Buscar pets", description = "Retorna uma lista paginada de pets")
  @ApiResponse(
      responseCode = "200",
      description = "Pagina com pets buscada com sucesso",
      content = @Content(schema = @Schema(implementation = PageResponseDTO.class)))
  @GetMapping
  ResponseEntity<PageResponseDTO<PetResponseDTO>> findAll(
      @ParameterObject PageableRequestDTO pageable);

  @Operation(summary = "Buscar um pet", description = "Busca um pet pelo seu id")
  @ApiResponse(
      responseCode = "200",
      description = "Pet buscado com sucesso",
      content = @Content(schema = @Schema(implementation = PetResponseDTO.class)))
  @ApiResponse(
      responseCode = "404",
      description = "Pet não encontrado",
      content =
          @Content(
              schema = @Schema(implementation = ErrorResponseDTO.class),
              examples =
                  @ExampleObject(
                      name = "Pet não encontrado",
                      value = "{\"messages\": [\"Pet not found\"]}")))
  @GetMapping("/{id}")
  ResponseEntity<PetResponseDTO> find(@PathVariable UUID id);
}
