package br.com.db.petcontrol.repository;

import br.com.db.petcontrol.model.Pets;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetsRepository extends JpaRepository<Pets, UUID> {}
