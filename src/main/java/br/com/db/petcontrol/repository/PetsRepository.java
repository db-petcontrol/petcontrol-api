package br.com.db.petcontrol.repository;

import br.com.db.petcontrol.model.Pets;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetsRepository extends JpaRepository<Pets, UUID> {
  @EntityGraph(attributePaths = {"species", "tags"})
  Page<Pets> findAll(Pageable pageable);

  @EntityGraph(attributePaths = {"species", "tags"})
  Optional<Pets> findById(UUID id);
}
