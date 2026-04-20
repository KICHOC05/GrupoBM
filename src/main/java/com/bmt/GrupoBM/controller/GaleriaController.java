package com.bmt.GrupoBM.controller;

import com.bmt.GrupoBM.dto.GaleriaDTO;
import com.bmt.GrupoBM.models.Galeria;
import com.bmt.GrupoBM.service.GaleriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/galeria")
public class GaleriaController {

    @Autowired
    private GaleriaService galeriaService;

    @GetMapping
    public String listar(Model model) {
        List<Galeria> galeriaList = galeriaService.listarTodos();
        model.addAttribute("galeriaList", galeriaList);
        return "admin/galeria/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("galeriaDTO", new GaleriaDTO());
        model.addAttribute("titulo", "Nueva Imagen");
        return "admin/galeria/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, Model model) {
        Galeria galeria = galeriaService.buscarPorId(id);
        model.addAttribute("galeriaDTO", galeriaService.toDTO(galeria));
        model.addAttribute("titulo", "Editar Imagen");
        return "admin/galeria/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute GaleriaDTO galeriaDTO,
                          BindingResult result,
                          RedirectAttributes flash) {
        if (result.hasErrors()) {
            flash.addFlashAttribute("error", "Error en los datos del formulario");
            return "redirect:/admin/galeria";
        }
        try {
            galeriaService.guardar(galeriaDTO);
            flash.addFlashAttribute("exito", "Imagen guardada correctamente.");
        } catch (MaxUploadSizeExceededException e) {
            flash.addFlashAttribute("error", "El archivo excede el tamaño máximo (50MB).");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/admin/galeria";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id, RedirectAttributes flash) {
        try {
            galeriaService.eliminar(id);
            flash.addFlashAttribute("exito", "Imagen eliminada correctamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/admin/galeria";
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(RedirectAttributes flash) {
        flash.addFlashAttribute("error", "El archivo excede el tamaño máximo permitido (50MB).");
        return "redirect:/admin/galeria";
    }
}