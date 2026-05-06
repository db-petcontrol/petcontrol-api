package br.com.db.petcontrol.mapper;

import br.com.db.petcontrol.dto.response.TagsResponseDTO;
import br.com.db.petcontrol.model.Tags;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagsMapper {

  TagsResponseDTO toDTO(Tags tags);

  List<TagsResponseDTO> toDTOList(List<Tags> tags);
}
