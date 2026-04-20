package com.bmt.GrupoBM.controller;

import com.bmt.GrupoBM.models.Articulo;
import com.bmt.GrupoBM.service.ArticuloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private ArticuloService articuloService;

    @GetMapping
    public String listar(@RequestParam(defaultValue = "1") int page,
                         @RequestParam(required = false) String categoria,
                         Model model) {
        // Destacados
        List<Articulo> destacados = articuloService.listarDestacados();
        Articulo destacadoPrincipal = destacados.isEmpty() ? null : destacados.get(0);
        List<Articulo> restoDestacados = destacados.size() > 1 ? destacados.subList(1, Math.min(3, destacados.size())) : List.of();

        // Paginación de artículos activos
        Page<Articulo> articulosPage = articuloService.listarPaginado(page - 1, 6);
        List<Articulo> articulos = articulosPage.getContent();

        model.addAttribute("destacadoPrincipal", destacadoPrincipal);
        model.addAttribute("restoDestacados", restoDestacados);
        model.addAttribute("articulos", articulos);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articulosPage.getTotalPages());
        model.addAttribute("categoriaSeleccionada", categoria);

        return "blog";
    }

    @GetMapping("/{id}/{slug}")
    public String detalle(@PathVariable int id, @PathVariable(required = false) String slug, Model model) {
        Articulo articulo = articuloService.buscarPorIdPublico(id);
        if (articulo == null) {
            return "redirect:/blog";
        }

        // Artículos relacionados (misma categoría, excluyendo el actual, máximo 3)
        List<Articulo> relacionados = articuloService.listarPorCategoria(articulo.getCategoria())
                .stream()
                .filter(a -> a.getId() != id)
                .limit(3)
                .collect(Collectors.toList());

        // Anterior y siguiente (por id, orden descendente)
        List<Articulo> todos = articuloService.listarActivos();
        int index = -1;
        for (int i = 0; i < todos.size(); i++) {
            if (todos.get(i).getId() == id) {
                index = i;
                break;
            }
        }
        Articulo anterior = (index > 0) ? todos.get(index - 1) : null;
        Articulo siguiente = (index < todos.size() - 1) ? todos.get(index + 1) : null;

        model.addAttribute("articulo", articulo);
        model.addAttribute("articulosRelacionados", relacionados);
        model.addAttribute("anteriorId", anterior != null ? anterior.getId() : null);
        model.addAttribute("anteriorTitulo", anterior != null ? anterior.getTitulo() : null);
        model.addAttribute("siguienteId", siguiente != null ? siguiente.getId() : null);
        model.addAttribute("siguienteTitulo", siguiente != null ? siguiente.getTitulo() : null);

        return "blog-detalle";
    }
    
    @GetMapping("/buscar")
    public String buscar(@RequestParam String q, Model model) {
        // Búsqueda simple (implementa en service si quieres)
        model.addAttribute("articulos", List.of());
        model.addAttribute("busqueda", q);
        return "blog";
    }
}