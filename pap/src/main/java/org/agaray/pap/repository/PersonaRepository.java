package org.agaray.pap.repository;

import java.util.List;

import org.agaray.pap.domain.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long>{
	public Persona getByLoginname(String loginname);
	public List<Persona> findDistinctByNombreIgnoreCaseContainingOrLoginnameIgnoreCaseContainingOrNaceNombreIgnoreCaseContainingOrGustosNombreIgnoreCaseContainingOrOdiosNombreIgnoreCaseContainingOrderByNaceNombreDesc(String f1,String f2, String f3, String f4, String f5);
	public List<Persona> findAllByOrderByAlturaAsc();
	public List<Persona> findAllByOrderByAlturaDesc();
	public List<Persona> findAllByOrderByFnacDesc();
}
