package com.bmt.GrupoBM.controller;

import com.bmt.GrupoBM.dto.UsuarioDTO;
import com.bmt.GrupoBM.models.AppUser;
import com.bmt.GrupoBM.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listar(@RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size,
                         @RequestParam(required = false) String busqueda,
                         Model model) {
        Page<AppUser> usuariosPage = usuarioService.listarUsuarios(
                PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id")),
                busqueda
        );
        model.addAttribute("usuarios", usuariosPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", usuariosPage.getTotalPages());
        model.addAttribute("busqueda", busqueda);
        return "admin/usuarios/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioDTO());
        model.addAttribute("titulo", "Nuevo Usuario");
        return "admin/usuarios/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, Model model) {
        AppUser user = usuarioService.buscarPorId(id);
        model.addAttribute("usuarioDTO", usuarioService.toDTO(user));
        model.addAttribute("titulo", "Editar Usuario");
        return "admin/usuarios/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute UsuarioDTO usuarioDTO,
                          BindingResult result,
                          @RequestParam(value = "cambiarContrasena", required = false) boolean cambiarContrasena,
                          RedirectAttributes flash,
                          Model model) {

        // Validar email único
        if (usuarioService.existeEmail(usuarioDTO.getEmail(), usuarioDTO.getId())) {
            result.rejectValue("email", "error.email", "El email ya está registrado por otro usuario");
        }

        // Validar contraseña si es nuevo o si se quiere cambiar
        boolean esNuevo = usuarioDTO.getId() == 0;
        if (esNuevo || cambiarContrasena) {
            if (usuarioDTO.getContrasena() == null || usuarioDTO.getContrasena().isEmpty()) {
                result.rejectValue("contrasena", "error.contrasena", "La contraseña es obligatoria");
            } else if (usuarioDTO.getContrasena().length() < 6) {
                result.rejectValue("contrasena", "error.contrasena", "La contraseña debe tener al menos 6 caracteres");
            } else if (!usuarioDTO.getContrasena().equals(usuarioDTO.getConfirmarContrasena())) {
                result.rejectValue("confirmarContrasena", "error.confirmarContrasena", "Las contraseñas no coinciden");
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("titulo", usuarioDTO.getId() > 0 ? "Editar Usuario" : "Nuevo Usuario");
            return "admin/usuarios/formulario";
        }

        try {
            usuarioService.guardar(usuarioDTO, esNuevo || cambiarContrasena);
            flash.addFlashAttribute("exito", "Usuario guardado correctamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id, RedirectAttributes flash) {
        try {
            usuarioService.eliminar(id);
            flash.addFlashAttribute("exito", "Usuario eliminado correctamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/cambiar-rol/{id}")
    public String cambiarRol(@PathVariable int id, @RequestParam String rol, RedirectAttributes flash) {
        try {
            usuarioService.cambiarRol(id, rol);
            flash.addFlashAttribute("exito", "Rol actualizado correctamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al cambiar rol: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }
}