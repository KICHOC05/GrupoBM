package com.bmt.GrupoBM.controller;

import com.bmt.GrupoBM.dto.ContenidoDTO;
import com.bmt.GrupoBM.service.ContenidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/contenido")
public class ContenidoController {

    @Autowired
    private ContenidoService contenidoService;

    @GetMapping
    public String editar(Model model) {
        ContenidoDTO dto = new ContenidoDTO();
        // Hero
        dto.setHeroTitulo(contenidoService.getValor("hero_titulo", ""));
        dto.setHeroSubtitulo(contenidoService.getValor("hero_subtitulo", ""));
        // Nosotros
        dto.setNosotrosTitulo(contenidoService.getValor("nosotros_titulo", ""));
        dto.setNosotrosParrafo1(contenidoService.getValor("nosotros_parrafo1", ""));
        dto.setNosotrosParrafo2(contenidoService.getValor("nosotros_parrafo2", ""));
        dto.setNosotrosParrafo3(contenidoService.getValor("nosotros_parrafo3", ""));

        model.addAttribute("contenidoDTO", dto);
        model.addAttribute("logoActual", contenidoService.getValor("logo_url", null));
        return "admin/contenido/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(ContenidoDTO dto, RedirectAttributes flash) {
        try {
            contenidoService.guardarContenido(dto);
            flash.addFlashAttribute("exito", "Contenido actualizado correctamente.");
        } catch (MaxUploadSizeExceededException e) {
            flash.addFlashAttribute("error", "El logo excede el tamaño máximo (50MB).");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/admin/contenido";
    }
}