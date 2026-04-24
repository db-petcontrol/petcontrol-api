package br.com.db.petcontrol.controlle;

import br.com.db.petcontrol.dto.response.SpeciesResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Species", description = "Operações para listagem de espécies disponíveis")
@RequestMapping("/species")
public interface SpeciesController {

  @Operation(
      summary = "Listar todas as espécies",
      description = "Retorna todas as espécies cadastradas no sistema em ordem alfabética")
  @ApiResponse(
      responseCode = "200",
      description = "Lista de espécies retornada com sucesso",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = SpeciesResponseDTO.class)))
  @GetMapping
  ResponseEntity<List<SpeciesResponseDTO>> getAll();
}
