package com.bmt.GrupoBM.service;

import com.bmt.GrupoBM.dto.ServicioDTO;
import com.bmt.GrupoBM.models.Servicio;
import com.bmt.GrupoBM.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    public List<Servicio> listarActivos() {
        List<Servicio> activos = servicioRepository.findByActivoTrue();
        System.out.println("Servicios activos encontrados: " + activos.size());
        return activos;
    }
    
    public List<Servicio> listarTodos() {
        return servicioRepository.findAll();
    }

    public Servicio buscarPorId(int id) {
        return servicioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Servicio no encontrado: " + id));
    }

    public void guardarConImagenes(ServicioDTO dto,
                                   MultipartFile[] imagenesNuevas,
                                   List<String> imagenesEliminar) throws IOException {

        Servicio s = dto.getId() > 0
            ? servicioRepository.findById(dto.getId()).orElse(new Servicio())
            : new Servicio();

        s.setTitulo(dto.getTitulo());
        s.setDescripcion(dto.getDescripcion());
        s.setMensaje(dto.getMensaje());
        s.setActivo(dto.isActivo());

        // === CARACTERÍSTICAS ===
        if (dto.getCaracteristicasList() != null && !dto.getCaracteristicasList().isEmpty()) {
            String caracteristicasStr = dto.getCaracteristicasList().stream()
                    .filter(c -> c != null && !c.trim().isEmpty())
                    .collect(Collectors.joining("|"));
            s.setCaracteristicas(caracteristicasStr);
        } else {
            s.setCaracteristicas("");
        }

        // === IMÁGENES ===
        List<String> urlsFinales = new ArrayList<>();

        // Mantener las no eliminadas
        if (dto.getImagenesList() != null) {
            for (String url : dto.getImagenesList()) {
                if (url != null && !url.isBlank() && (imagenesEliminar == null || !imagenesEliminar.contains(url))) {
                    urlsFinales.add(url);
                } else if (imagenesEliminar != null && imagenesEliminar.contains(url)) {
                    // Eliminar de Cloudinary
                    try {
                        String publicId = extractPublicIdFromUrl(url);
                        if (publicId != null) cloudinaryService.eliminarImagen(publicId);
                    } catch (Exception e) { /* log */ }
                }
            }
        }

        // Subir nuevas
        if (imagenesNuevas != null) {
            for (MultipartFile archivo : imagenesNuevas) {
                if (archivo != null && !archivo.isEmpty()) {
                    try {
                        String url = cloudinaryService.subirImagen(archivo, "servicios");
                        urlsFinales.add(url);
                    } catch (Exception e) { /* log */ }
                }
            }
        }

        s.setImagenes(String.join("|", urlsFinales));
        servicioRepository.save(s);
    }

    public void eliminar(int id) {
        Servicio s = buscarPorId(id);
        if (s.getImagenesList() != null) {
            for (String url : s.getImagenesList()) {
                try {
                    String publicId = extractPublicIdFromUrl(url);
                    if (publicId != null) cloudinaryService.eliminarImagen(publicId);
                } catch (Exception e) { /* log */ }
            }
        }
        servicioRepository.deleteById(id);
    }

    public void toggleActivo(int id) {
        Servicio s = buscarPorId(id);
        s.setActivo(!s.isActivo());
        servicioRepository.save(s);
    }

    public ServicioDTO toDTO(Servicio s) {
        ServicioDTO dto = new ServicioDTO();
        dto.setId(s.getId());
        dto.setTitulo(s.getTitulo());
        dto.setDescripcion(s.getDescripcion());
        dto.setMensaje(s.getMensaje());
        dto.setActivo(s.isActivo());
        dto.setCaracteristicasList(s.getCaracteristicasList());
        dto.setImagenesList(s.getImagenesList());
        return dto;
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
}