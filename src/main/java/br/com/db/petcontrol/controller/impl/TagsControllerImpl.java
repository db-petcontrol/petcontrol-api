package br.com.db.petcontrol.controller.impl;

import br.com.db.petcontrol.controller.TagsController;
import br.com.db.petcontrol.dto.response.TagsResponseDTO;
import br.com.db.petcontrol.service.TagsService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TagsControllerImpl implements TagsController {

  private final TagsService service;

  public TagsControllerImpl(TagsService service) {
    this.service = service;
  }

  @Override
  public ResponseEntity<List<TagsResponseDTO>> getAllTags() {
    List<TagsResponseDTO> tags = service.getAllTags();
    return ResponseEntity.ok(tags);
  }
}
