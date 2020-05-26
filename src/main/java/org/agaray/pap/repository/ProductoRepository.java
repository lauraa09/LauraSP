package org.agaray.pap.repository;

import java.util.List;

import org.agaray.pap.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

	public List<Producto> findAllByOrderByNombreAsc();

	public List <Producto> findAllByOrderByNombreAscCategoriaNombreAsc();

	public List <Producto> findAllByOrderByCategoriaNombreAscNombreAsc();


	
	

}
