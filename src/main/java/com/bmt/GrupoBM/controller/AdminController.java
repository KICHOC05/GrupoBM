package com.bmt.GrupoBM.controller;

import com.bmt.GrupoBM.models.AppUser;
import com.bmt.GrupoBM.repository.AppUserRepository;
import com.bmt.GrupoBM.service.GaleriaService;
import com.bmt.GrupoBM.service.ServicioService;
import com.bmt.GrupoBM.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;




@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private ServicioService servicioService;
	
	@Autowired
	private UsuarioService userService;
	
	@Autowired
	private GaleriaService galeriaService;
	
    @Autowired
    private AppUserRepository appUserRepository;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model, Authentication authentication) {
        System.out.println("========== DASHBOARD ==========");
        System.out.println("Authentication: " + (authentication != null ? authentication.getName() : "null"));
        
        // Intentar obtener usuario de la sesión
        AppUser user = (AppUser) session.getAttribute("user");
        
        // Si no está en sesión, obtenerlo de la autenticación
        if (user == null && authentication != null && authentication.isAuthenticated()) {
            Optional<AppUser> userOpt = appUserRepository.findByEmail(authentication.getName());
            if (userOpt.isPresent()) {
                user = userOpt.get();
                session.setAttribute("user", user);
                System.out.println("Usuario cargado desde authentication: " + user.getNombre());
            }
        }
        
        if (user == null) {
            System.out.println("No hay usuario, redirigiendo a login");
            return "redirect:/login";
        }
        
        if (!"ROLE_ADMIN".equals(user.getRol())) {
            System.out.println("No es admin, redirigiendo");
            return "redirect:/acceso-denegado";
        }
        
        model.addAttribute("user", user);
        System.out.println("Mostrando dashboard para: " + user.getEmail());
        return "admin/dashboard";
    }

    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model, Authentication authentication) {
        AppUser user = (AppUser) session.getAttribute("user");
        
        if (user == null && authentication != null) {
            Optional<AppUser> userOpt = appUserRepository.findByEmail(authentication.getName());
            if (userOpt.isPresent()) {
                user = userOpt.get();
                session.setAttribute("user", user);
            }
        }
        
        if (user == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", user);
        return "admin/perfil";
    }

    @GetMapping("/api/stats")
    @ResponseBody
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userService.countAll());
        stats.put("totalServices", servicioService.listarTodos().size());
        stats.put("totalGallery", galeriaService.listarTodos().size());
        return stats;
    }
    
}
