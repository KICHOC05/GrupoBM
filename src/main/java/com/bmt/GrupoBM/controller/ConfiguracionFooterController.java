package com.bmt.GrupoBM.controller;

import com.bmt.GrupoBM.dto.ConfiguracionFooterDTO;
import com.bmt.GrupoBM.service.ConfiguracionFooterService;
import com.bmt.GrupoBM.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/configuracion")
public class ConfiguracionFooterController {

    @Autowired
    private ConfiguracionFooterService configService;

    @Autowired
    private ServicioService servicioService;

    @GetMapping
    public String formulario(Model model) {
        ConfiguracionFooterDTO config = configService.toDTO(configService.obtenerConfiguracion());
        model.addAttribute("config", config);
        model.addAttribute("servicios", servicioService.listarActivos()); // lista de servicios activos (solo títulos)
        return "admin/configuracion/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(ConfiguracionFooterDTO config, RedirectAttributes flash) {
        configService.guardar(config);
        flash.addFlashAttribute("exito", "Configuración guardada correctamente.");
        return "redirect:/admin/configuracion";
    }
}