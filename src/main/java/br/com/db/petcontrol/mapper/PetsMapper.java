package br.com.db.petcontrol.mapper;

import br.com.db.petcontrol.dto.request.PetRequestDTO;
import br.com.db.petcontrol.dto.response.PetResponseDTO;
import br.com.db.petcontrol.model.Pets;
import br.com.db.petcontrol.model.Species;
import br.com.db.petcontrol.model.Tags;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PetsMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "name", source = "dto.name")
  @Mapping(target = "status", source = "dto.status")
  Pets toEntity(PetRequestDTO dto, Species species, List<Tags> tags);

  @Mapping(target = "species", source = "species.name")
  @Mapping(target = "tags", source = "tags")
  PetResponseDTO toResponse(Pets pet);

  default List<String> map(List<Tags> tags) {
    if (tags == null) {
      return null;
    }
    return tags.stream().map(Tags::getName).toList();
  }
}
