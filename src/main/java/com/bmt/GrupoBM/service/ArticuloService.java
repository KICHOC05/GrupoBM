package com.bmt.GrupoBM.service;

import com.bmt.GrupoBM.dto.ArticuloDTO;
import com.bmt.GrupoBM.models.Articulo;
import com.bmt.GrupoBM.repository.ArticuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ArticuloService {

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    public List<Articulo> listarActivos() {
        return articuloRepository.findByActivoTrueOrderByIdDesc();
    }

    public Page<Articulo> listarPaginado(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return articuloRepository.findByActivoTrueOrderByIdDesc(pageable);
    }

    public List<Articulo> listarDestacados() {
        return articuloRepository.findByActivoTrueAndDestacadoTrueOrderByIdDesc();
    }

    public List<Articulo> listarPorCategoria(String categoria) {
        return articuloRepository.findByActivoTrueAndCategoriaOrderByIdDesc(categoria);
    }

    public Articulo buscarPorId(int id) {
        return articuloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artículo no encontrado: " + id));
    }

    public Articulo buscarPorIdPublico(int id) {
        return articuloRepository.findByIdAndActivoTrue(id);
    }

    public void guardar(ArticuloDTO dto) throws IOException {
        Articulo articulo = dto.getId() > 0 ? buscarPorId(dto.getId()) : new Articulo();

        articulo.setTitulo(dto.getTitulo());
        articulo.setResumen(dto.getResumen());
        articulo.setContenido(dto.getContenido());
        articulo.setCategoria(dto.getCategoria());
        articulo.setDestacado(dto.isDestacado());
        articulo.setActivo(dto.isActivo());

        if (dto.getImagenFile() != null && !dto.getImagenFile().isEmpty()) {
            if (articulo.getImagenPortada() != null && !articulo.getImagenPortada().isEmpty()) {
                try {
                    String publicId = extractPublicIdFromUrl(articulo.getImagenPortada());
                    if (publicId != null) cloudinaryService.eliminarImagen(publicId);
                } catch (Exception e) { /* log */ }
            }
            String nuevaUrl = cloudinaryService.subirImagen(dto.getImagenFile(), "blog");
            articulo.setImagenPortada(nuevaUrl);
        } else if (dto.getId() == 0) {
            throw new RuntimeException("Debe seleccionar una imagen de portada para un nuevo artículo");
        }

        articuloRepository.save(articulo);
    }

    public void eliminar(int id) throws IOException {
        Articulo articulo = buscarPorId(id);
        if (articulo.getImagenPortada() != null && !articulo.getImagenPortada().isEmpty()) {
            String publicId = extractPublicIdFromUrl(articulo.getImagenPortada());
            if (publicId != null) cloudinaryService.eliminarImagen(publicId);
        }
        articuloRepository.deleteById(id);
    }

    private String extractPublicIdFromUrl(String url) {
        try {
            if (url == null || url.isEmpty()) return null;
            String uploadMarker = "/upload/";
            int idx = url.indexOf(uploadMarker);
            if (idx == -1) return null;
            String path = url.substring(idx + uploadMarker.length());
            if (path.startsWith("v") && path.contains("/")) {
                path = path.substring(path.indexOf("/") + 1);
            }
            int dot = path.lastIndexOf(".");
            if (dot != -1) path = path.substring(0, dot);
            return path;
        } catch (Exception e) {
            return null;
        }
    }

    public ArticuloDTO toDTO(Articulo articulo) {
        ArticuloDTO dto = new ArticuloDTO();
        dto.setId(articulo.getId());
        dto.setTitulo(articulo.getTitulo());
        dto.setResumen(articulo.getResumen());
        dto.setContenido(articulo.getContenido());
        dto.setImagenPortada(articulo.getImagenPortada());
        dto.setCategoria(articulo.getCategoria());
        dto.setDestacado(articulo.isDestacado());
        dto.setActivo(articulo.isActivo());
        return dto;
    }
}