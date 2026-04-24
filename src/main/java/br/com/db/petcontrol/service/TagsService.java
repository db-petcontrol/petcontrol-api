package br.com.db.petcontrol.service;

import br.com.db.petcontrol.dto.response.TagsResponseDTO;
import java.util.List;

public interface TagsService {

  List<TagsResponseDTO> getAllTags();
}
