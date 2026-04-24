package br.com.db.petcontrol.service.impl;

import br.com.db.petcontrol.dto.response.TagsResponseDTO;
import br.com.db.petcontrol.mapper.TagsMapper;
import br.com.db.petcontrol.repository.TagsRepository;
import br.com.db.petcontrol.service.TagsService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TagsServiceImpl implements TagsService {

  private final TagsRepository repository;
  private final TagsMapper mapper;

  public TagsServiceImpl(TagsRepository repository, TagsMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public List<TagsResponseDTO> getAllTags() {
    return repository.findAllByOrderByNameAsc().stream().map(mapper::toDTO).toList();
  }
}
