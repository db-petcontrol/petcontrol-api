package br.com.db.petcontrol.controller;

import br.com.db.petcontrol.dto.response.TagsResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Tags", description = "Operações para listagem de tags disponíveis")
@RequestMapping("/Tags")
public interface TagsController {

  @Operation(
      summary = "Listar todas as tags",
      description = "Retorna todas as tags cadastradas no sistema em ordem alfabética")
  @ApiResponse(
      responseCode = "200",
      description = "Lista de tags retornada com sucesso",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = TagsResponseDTO.class)))
  @GetMapping
  ResponseEntity<List<TagsResponseDTO>> getAllTags();
}
