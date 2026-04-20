package com.bmt.GrupoBM.repository;

import com.bmt.GrupoBM.models.ConfiguracionFooter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfiguracionFooterRepository extends JpaRepository<ConfiguracionFooter, Long> {
}