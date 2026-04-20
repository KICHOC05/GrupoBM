package com.bmt.GrupoBM.controller;

import com.bmt.GrupoBM.dto.ClienteDTO;
import com.bmt.GrupoBM.models.Cliente;
import com.bmt.GrupoBM.service.ClienteService;
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
@RequestMapping("/admin/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String listar(Model model) {
        List<Cliente> clientes = clienteService.listarTodos();
        model.addAttribute("clientes", clientes);
        return "admin/clientes/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("clienteDTO", new ClienteDTO());
        model.addAttribute("titulo", "Nuevo Cliente");
        return "admin/clientes/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, Model model) {
        Cliente cliente = clienteService.buscarPorId(id);
        model.addAttribute("clienteDTO", clienteService.toDTO(cliente));
        model.addAttribute("titulo", "Editar Cliente");
        return "admin/clientes/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute ClienteDTO clienteDTO,
                          BindingResult result,
                          RedirectAttributes flash) {
        if (result.hasErrors()) {
            flash.addFlashAttribute("error", "Error en los datos del formulario");
            return "redirect:/admin/clientes";
        }
        try {
            clienteService.guardar(clienteDTO);
            flash.addFlashAttribute("exito", "Cliente guardado correctamente.");
        } catch (MaxUploadSizeExceededException e) {
            flash.addFlashAttribute("error", "El archivo excede el tamaño máximo (50MB).");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/admin/clientes";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id, RedirectAttributes flash) {
        try {
            clienteService.eliminar(id);
            flash.addFlashAttribute("exito", "Cliente eliminado correctamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/admin/clientes";
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(RedirectAttributes flash) {
        flash.addFlashAttribute("error", "El archivo excede el tamaño máximo permitido (50MB).");
        return "redirect:/admin/clientes";
    }
}