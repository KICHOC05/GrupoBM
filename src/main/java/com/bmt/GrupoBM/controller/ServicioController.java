package com.bmt.GrupoBM.controller;

import com.bmt.GrupoBM.dto.ServicioDTO;
import com.bmt.GrupoBM.models.Servicio;
import com.bmt.GrupoBM.service.ServicioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/servicios")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    /**
     * Listar todos los servicios
     */
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("servicios", servicioService.listarTodos());
        return "admin/servicios/lista";
    }

    /**
     * Formulario para nuevo servicio
     */
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        ServicioDTO dto = new ServicioDTO();
        dto.setCaracteristicasList(Arrays.asList(""));
        dto.setImagenesList(Arrays.asList());
        dto.setActivo(true);
        
        model.addAttribute("servicioDTO", dto);
        model.addAttribute("titulo", "Nuevo Servicio");
        return "admin/servicios/formulario";
    }

    /**
     * Formulario para editar servicio
     */
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, Model model) {
        ServicioDTO dto = servicioService.toDTO(servicioService.buscarPorId(id));
        if (dto.getCaracteristicasList() == null || dto.getCaracteristicasList().isEmpty()) {
            dto.setCaracteristicasList(Arrays.asList(""));
        }
        
        model.addAttribute("servicioDTO", dto);
        model.addAttribute("titulo", "Editar Servicio");
        return "admin/servicios/formulario";
    }

    /**
     * Guardar servicio (crear o actualizar)
     */
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute ServicioDTO servicioDTO,
                          BindingResult result,
                          @RequestParam(value = "imagenesFile", required = false) MultipartFile[] imagenesFile,
                          @RequestParam(value = "imagenesEliminar", required = false) String imagenesEliminar,
                          Model model,
                          RedirectAttributes flash) {

        // Validar errores
        if (result.hasErrors()) {
            model.addAttribute("titulo", servicioDTO.getId() > 0 ? "Editar Servicio" : "Nuevo Servicio");
            return "admin/servicios/formulario";
        }

        try {
            // Procesar imágenes a eliminar
            List<String> imagenesParaEliminar = null;
            if (imagenesEliminar != null && !imagenesEliminar.isEmpty()) {
                imagenesParaEliminar = Arrays.asList(imagenesEliminar.split(","));
            }

            // Guardar servicio
            servicioService.guardarConImagenes(servicioDTO, imagenesFile, imagenesParaEliminar);
            
            flash.addFlashAttribute("exito", "Servicio guardado correctamente.");
            
        } catch (MaxUploadSizeExceededException e) {
            flash.addFlashAttribute("error", "El archivo es demasiado grande. Máximo permitido: 50MB");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/admin/servicios";
    }

    /**
     * Alternar estado activo/inactivo del servicio
     */
    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable int id, RedirectAttributes flash) {
        try {
            servicioService.toggleActivo(id);
            flash.addFlashAttribute("exito", "Estado del servicio actualizado.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
        }
        return "redirect:/admin/servicios";
    }

    /**
     * Eliminar servicio
     */
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id, RedirectAttributes flash) {
        try {
            servicioService.eliminar(id);
            flash.addFlashAttribute("exito", "Servicio eliminado correctamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/admin/servicios";
    }

    /**
     * Manejador global para excepción de tamaño de archivo
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(RedirectAttributes flash) {
        flash.addFlashAttribute("error", "El archivo excede el tamaño máximo permitido (50MB).");
        return "redirect:/admin/servicios";
    }
    
    /**
     * Ver detalle de un servicio por ID
     */
    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable int id, Model model) {
        Servicio servicio = servicioService.buscarPorId(id);
        model.addAttribute("servicio", servicio);
        return "admin/servicios/detalle";
    }
    
    @GetMapping("/api/servicios/{id}")
    @ResponseBody
    public ServicioDTO obtenerServicio(@PathVariable int id) {
        Servicio servicio = servicioService.buscarPorId(id);
        return servicioService.toDTO(servicio);
    }
    
}