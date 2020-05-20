package org.agaray.pap.repository;

import org.agaray.pap.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	public Categoria getByNombre(String nombre); //ordena ascendentente
	
}
