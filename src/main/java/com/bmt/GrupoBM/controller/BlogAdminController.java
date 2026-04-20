package com.bmt.GrupoBM.controller;

import com.bmt.GrupoBM.dto.ArticuloDTO;
import com.bmt.GrupoBM.models.Articulo;
import com.bmt.GrupoBM.service.ArticuloService;
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
@RequestMapping("/admin/blogs")
public class BlogAdminController {

    @Autowired
    private ArticuloService articuloService;

    @GetMapping
    public String listar(Model model) {
        List<Articulo> articulos = articuloService.listarActivos();
        model.addAttribute("articulos", articulos);
        return "admin/blog/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        ArticuloDTO dto = new ArticuloDTO();
        dto.setActivo(true);
        model.addAttribute("articuloDTO", dto);
        model.addAttribute("titulo", "Nuevo Artículo");
        return "admin/blog/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, Model model) {
        Articulo articulo = articuloService.buscarPorId(id);
        model.addAttribute("articuloDTO", articuloService.toDTO(articulo));
        model.addAttribute("titulo", "Editar Artículo");
        return "admin/blog/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute ArticuloDTO articuloDTO,
                          BindingResult result,
                          RedirectAttributes flash) {
        if (result.hasErrors()) {
            flash.addFlashAttribute("error", "Error en los datos del formulario");
            return "redirect:/admin/blogs";
        }
        try {
            articuloService.guardar(articuloDTO);
            flash.addFlashAttribute("exito", "Artículo guardado correctamente.");
        } catch (MaxUploadSizeExceededException e) {
            flash.addFlashAttribute("error", "La imagen excede el tamaño máximo (50MB).");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/admin/blogs";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id, RedirectAttributes flash) {
        try {
            articuloService.eliminar(id);
            flash.addFlashAttribute("exito", "Artículo eliminado correctamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/admin/blogs";
    }
}