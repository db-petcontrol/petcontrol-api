package br.com.db.petcontrol.repository;

import br.com.db.petcontrol.model.Species;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpeciesRepository extends JpaRepository<Species, UUID> {

  List<Species> findAllByOrderByNameAsc();
}
