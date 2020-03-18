package org.agaray.pap.repository;

import org.agaray.pap.domain.LineaDeVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LdvRepository extends JpaRepository<LineaDeVenta, Long> {
}
