package br.com.db.petcontrol.mapper;

import br.com.db.petcontrol.dto.request.PageableRequestDTO;
import br.com.db.petcontrol.dto.response.PageResponseDTO;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Mapper(componentModel = "spring")
public interface PageMapper {
  default <T> PageResponseDTO<T> toPageResponseDTO(Page<T> page) {
    return new PageResponseDTO<>(
        page.getContent(),
        page.getTotalPages(),
        page.getTotalElements(),
        page.getSize(),
        page.getNumber());
  }

  default Pageable toPageable(PageableRequestDTO dto) {
    Sort sort = Sort.by(Sort.Direction.ASC, "name");
    return PageRequest.of(dto.page(), dto.size(), sort);
  }
}
