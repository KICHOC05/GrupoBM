package com.bmt.GrupoBM.repository;

import com.bmt.GrupoBM.models.Articulo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArticuloRepository extends JpaRepository<Articulo, Integer> {
    List<Articulo> findByActivoTrueOrderByIdDesc();
    Page<Articulo> findByActivoTrueOrderByIdDesc(Pageable pageable);
    List<Articulo> findByActivoTrueAndDestacadoTrueOrderByIdDesc();
    List<Articulo> findByActivoTrueAndCategoriaOrderByIdDesc(String categoria);
    Articulo findByIdAndActivoTrue(int id);
}