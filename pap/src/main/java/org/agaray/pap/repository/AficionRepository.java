package org.agaray.pap.repository;

import org.agaray.pap.domain.Aficion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AficionRepository extends JpaRepository<Aficion, Long>{
}
