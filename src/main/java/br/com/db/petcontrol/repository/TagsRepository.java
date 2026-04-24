package br.com.db.petcontrol.repository;

import br.com.db.petcontrol.model.Tags;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagsRepository extends JpaRepository<Tags, UUID> {

  List<Tags> findAllByOrderByNameAsc();
}
