package com.bmt.GrupoBM.service;

import com.bmt.GrupoBM.dto.ConfiguracionFooterDTO;
import com.bmt.GrupoBM.models.ConfiguracionFooter;
import com.bmt.GrupoBM.repository.ConfiguracionFooterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfiguracionFooterService {

    @Autowired
    private ConfiguracionFooterRepository repository;

    public ConfiguracionFooter obtenerConfiguracion() {
        // Solo debe haber un registro (id=1)
        return repository.findById(1L).orElseGet(() -> {
            ConfiguracionFooter nueva = new ConfiguracionFooter();
            nueva.setDescripcion("Fabricación e instalación de cortinas metálicas y estructuras industriales con los más altos estándares de calidad.");
            nueva.setDireccion("Av. Industrias 123, Col. Industrial, CDMX");
            nueva.setWhatsapp("+52 564 409 1155");
            nueva.setEmail("grupobm.industrial@gmail.com");
            nueva.setInstagramTexto("@grupobm.industrial");
            nueva.setInstagramUrl("https://instagram.com/grupobm.industrial");
            nueva.setFacebookTexto("Grupo BM Industrial");
            nueva.setFacebookUrl("https://www.facebook.com/share/18UJDsVjrE/?mibextid=wwXIfr");
            nueva.setHorarioLaboral("Lun-Vie: 8:00 - 18:00");
            nueva.setHorarioSabado("Sáb: 9:00 - 14:00");
            nueva.setCopyright("© 2026 Grupo BM – Cortinas Industriales y Cortes Metálicos. Todos los derechos reservados.");
            return repository.save(nueva);
        });
    }

    public void guardar(ConfiguracionFooterDTO dto) {
        ConfiguracionFooter config = obtenerConfiguracion();
        config.setDescripcion(dto.getDescripcion());
        config.setDireccion(dto.getDireccion());
        config.setWhatsapp(dto.getWhatsapp());
        config.setEmail(dto.getEmail());
        config.setInstagramTexto(dto.getInstagramTexto());
        config.setInstagramUrl(dto.getInstagramUrl());
        config.setFacebookTexto(dto.getFacebookTexto());
        config.setFacebookUrl(dto.getFacebookUrl());
        config.setHorarioLaboral(dto.getHorarioLaboral());
        config.setHorarioSabado(dto.getHorarioSabado());
        config.setCopyright(dto.getCopyright());
        repository.save(config);
    }

    public ConfiguracionFooterDTO toDTO(ConfiguracionFooter config) {
        ConfiguracionFooterDTO dto = new ConfiguracionFooterDTO();
        dto.setId(config.getId());
        dto.setDescripcion(config.getDescripcion());
        dto.setDireccion(config.getDireccion());
        dto.setWhatsapp(config.getWhatsapp());
        dto.setEmail(config.getEmail());
        dto.setInstagramTexto(config.getInstagramTexto());
        dto.setInstagramUrl(config.getInstagramUrl());
        dto.setFacebookTexto(config.getFacebookTexto());
        dto.setFacebookUrl(config.getFacebookUrl());
        dto.setHorarioLaboral(config.getHorarioLaboral());
        dto.setHorarioSabado(config.getHorarioSabado());
        dto.setCopyright(config.getCopyright());
        return dto;
    }
}