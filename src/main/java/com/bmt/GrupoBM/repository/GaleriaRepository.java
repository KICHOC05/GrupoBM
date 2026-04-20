package com.bmt.GrupoBM.repository;

import com.bmt.GrupoBM.models.Galeria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GaleriaRepository extends JpaRepository<Galeria, Integer> {
    List<Galeria> findByActivoTrueOrderByOrdenAsc();
}