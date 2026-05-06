package br.com.db.petcontrol.mapper;

import br.com.db.petcontrol.dto.request.PetRequestDTO;
import br.com.db.petcontrol.dto.response.PetResponseDTO;
import br.com.db.petcontrol.model.Pets;
import br.com.db.petcontrol.model.Species;
import br.com.db.petcontrol.model.Tags;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
    componentModel = "spring",
    uses = {SpeciesMapper.class, TagsMapper.class})
public interface PetsMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "name", source = "dto.name")
  @Mapping(target = "status", source = "dto.status")
  Pets toEntity(PetRequestDTO dto, Species species, List<Tags> tags);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "species", ignore = true)
  @Mapping(target = "tags", ignore = true)
  void toUpdateEntity(PetRequestDTO dto, @MappingTarget Pets pet);

  PetResponseDTO toResponse(Pets pet);
}
