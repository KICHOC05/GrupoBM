package com.bmt.GrupoBM.service;

import com.bmt.GrupoBM.dto.ContenidoDTO;
import com.bmt.GrupoBM.models.Contenido;
import com.bmt.GrupoBM.repository.ContenidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ContenidoService {

    @Autowired
    private ContenidoRepository contenidoRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    public Map<String, String> getMapaContenido() {
        Map<String, String> mapa = new HashMap<>();
        contenidoRepository.findAll().forEach(c -> mapa.put(c.getClave(), c.getValor()));
        return mapa;
    }

    public String getValor(String clave, String defaultValue) {
        return contenidoRepository.findByClave(clave)
                .map(Contenido::getValor)
                .orElse(defaultValue);
    }

    public void setValor(String clave, String valor) {
        Contenido contenido = contenidoRepository.findByClave(clave).orElse(new Contenido());
        contenido.setClave(clave);
        contenido.setValor(valor);
        contenidoRepository.save(contenido);
    }

    public void guardarContenido(ContenidoDTO dto) throws IOException {
        // Hero
        setValor("hero_titulo", dto.getHeroTitulo());
        setValor("hero_subtitulo", dto.getHeroSubtitulo());

        // Nosotros
        setValor("nosotros_titulo", dto.getNosotrosTitulo());
        setValor("nosotros_parrafo1", dto.getNosotrosParrafo1());
        setValor("nosotros_parrafo2", dto.getNosotrosParrafo2());
        setValor("nosotros_parrafo3", dto.getNosotrosParrafo3());

        // Logo
        if (dto.getLogoFile() != null && !dto.getLogoFile().isEmpty()) {
            String oldLogo = getValor("logo_url", null);
            if (oldLogo != null && !oldLogo.isEmpty()) {
                try {
                    String publicId = extractPublicIdFromUrl(oldLogo);
                    if (publicId != null) cloudinaryService.eliminarImagen(publicId);
                } catch (Exception e) { /* log */ }
            }
            String nuevaUrl = cloudinaryService.subirImagen(dto.getLogoFile(), "contenido");
            setValor("logo_url", nuevaUrl);
        }
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