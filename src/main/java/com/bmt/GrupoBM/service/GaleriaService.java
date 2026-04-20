package com.bmt.GrupoBM.service;

import com.bmt.GrupoBM.dto.GaleriaDTO;
import com.bmt.GrupoBM.models.Galeria;
import com.bmt.GrupoBM.repository.GaleriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class GaleriaService {

    @Autowired
    private GaleriaRepository galeriaRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    public List<Galeria> listarActivos() {
        return galeriaRepository.findByActivoTrueOrderByOrdenAsc();
    }

    public List<Galeria> listarTodos() {
        return galeriaRepository.findAll();
    }

    public Galeria buscarPorId(int id) {
        return galeriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada: " + id));
    }

    public void guardar(GaleriaDTO dto) throws IOException {
        Galeria galeria = dto.getId() > 0 ? buscarPorId(dto.getId()) : new Galeria();

        galeria.setDescripcion(dto.getDescripcion());
        galeria.setOrden(dto.getOrden());
        galeria.setActivo(dto.isActivo());

        // Si se subió una nueva imagen
        if (dto.getImagenFile() != null && !dto.getImagenFile().isEmpty()) {
            // Eliminar imagen anterior de Cloudinary si existe
            if (galeria.getImagenUrl() != null && !galeria.getImagenUrl().isEmpty()) {
                try {
                    String publicId = extractPublicIdFromUrl(galeria.getImagenUrl());
                    if (publicId != null) cloudinaryService.eliminarImagen(publicId);
                } catch (Exception e) { /* log */ }
            }
            String nuevaUrl = cloudinaryService.subirImagen(dto.getImagenFile(), "galeria");
            galeria.setImagenUrl(nuevaUrl);
        } else if (dto.getId() == 0) {
            throw new RuntimeException("Debe seleccionar una imagen para un nuevo registro");
        }

        galeriaRepository.save(galeria);
    }

    public void eliminar(int id) throws IOException {
        Galeria galeria = buscarPorId(id);
        // Eliminar de Cloudinary
        if (galeria.getImagenUrl() != null && !galeria.getImagenUrl().isEmpty()) {
            String publicId = extractPublicIdFromUrl(galeria.getImagenUrl());
            if (publicId != null) cloudinaryService.eliminarImagen(publicId);
        }
        galeriaRepository.deleteById(id);
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

    public GaleriaDTO toDTO(Galeria galeria) {
        GaleriaDTO dto = new GaleriaDTO();
        dto.setId(galeria.getId());
        dto.setImagenUrl(galeria.getImagenUrl());
        dto.setDescripcion(galeria.getDescripcion());
        dto.setOrden(galeria.getOrden());
        dto.setActivo(galeria.isActivo());
        return dto;
    }
}