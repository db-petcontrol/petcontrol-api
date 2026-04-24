package br.com.db.petcontrol.mapper;

import br.com.db.petcontrol.dto.response.SpeciesResponseDTO;
import br.com.db.petcontrol.model.Species;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpeciesMapper {

  SpeciesResponseDTO toDTO(Species species);
}
