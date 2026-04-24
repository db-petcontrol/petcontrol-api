package br.com.db.petcontrol.mapper;

import br.com.db.petcontrol.dto.response.TagsResponseDTO;
import br.com.db.petcontrol.model.Tags;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagsMapper {

  TagsResponseDTO toDTO(Tags tags);
}
