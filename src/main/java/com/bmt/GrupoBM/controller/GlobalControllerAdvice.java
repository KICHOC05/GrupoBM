package com.bmt.GrupoBM.controller;

import com.bmt.GrupoBM.models.ConfiguracionFooter;
import com.bmt.GrupoBM.models.Servicio;
import com.bmt.GrupoBM.service.ConfiguracionFooterService;
import com.bmt.GrupoBM.service.ServicioService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private ConfiguracionFooterService configFooterService;

    @Autowired
    private ServicioService servicioService;

    @ModelAttribute("footerConfig")
    public ConfiguracionFooter footerConfig() {
        return configFooterService.obtenerConfiguracion();
    }

    @ModelAttribute("serviciosActivos")
    public List<Servicio> serviciosActivos() {
        return servicioService.listarActivos();
    }
}