package com.bmt.GrupoBM.repository;

import com.bmt.GrupoBM.models.Contenido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ContenidoRepository extends JpaRepository<Contenido, Integer> {
    Optional<Contenido> findByClave(String clave);
}