package br.com.db.petcontrol.mocks;

import br.com.db.petcontrol.dto.request.PageableRequestDTO;
import br.com.db.petcontrol.dto.response.PageResponseDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageFixture {

  public static final Integer DEFAULT_PAGE = 0;
  public static final Integer DEFAULT_SIZE = 6;

  private PageFixture() {}

  public static PageableRequestDTO.PageableRequestDTOBuilder pageableRequestDtoBuilder() {
    return PageableRequestDTO.builder().page(DEFAULT_PAGE).size(DEFAULT_SIZE);
  }

  public static <T> PageResponseDTO.PageResponseDTOBuilder pageResponseDtoBuilder(List<T> content) {
    return PageResponseDTO.<T>builder()
        .content(content)
        .totalPages(1)
        .totalElements(content.size())
        .size(DEFAULT_SIZE)
        .number(DEFAULT_PAGE);
  }

  public static Pageable getPageableMock() {
    return PageRequest.of(DEFAULT_PAGE, DEFAULT_SIZE, Sort.by("nome").ascending());
  }

  public static <T> Page<T> getPageMock(List<T> conteudo) {
    return new PageImpl<>(conteudo, getPageableMock(), conteudo.size());
  }
}
