package com.bmt.GrupoBM.repository;

import com.bmt.GrupoBM.models.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServicioRepository extends JpaRepository<Servicio, Integer> {
    List<Servicio> findByActivoTrue();
}