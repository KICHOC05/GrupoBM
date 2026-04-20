package com.bmt.GrupoBM.service;

import com.bmt.GrupoBM.dto.ClienteDTO;
import com.bmt.GrupoBM.models.Cliente;
import com.bmt.GrupoBM.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    public List<Cliente> listarActivos() {
        return clienteRepository.findByActivoTrueOrderByOrdenAsc();
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(int id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + id));
    }

    public void guardar(ClienteDTO dto) throws IOException {
        Cliente cliente = dto.getId() > 0 ? buscarPorId(dto.getId()) : new Cliente();

        cliente.setNombre(dto.getNombre());
        cliente.setOrden(dto.getOrden());
        cliente.setActivo(dto.isActivo());

        if (dto.getImagenFile() != null && !dto.getImagenFile().isEmpty()) {
            // Eliminar imagen anterior si existe
            if (cliente.getImagenUrl() != null && !cliente.getImagenUrl().isEmpty()) {
                try {
                    String publicId = extractPublicIdFromUrl(cliente.getImagenUrl());
                    if (publicId != null) cloudinaryService.eliminarImagen(publicId);
                } catch (Exception e) { /* log */ }
            }
            String nuevaUrl = cloudinaryService.subirImagen(dto.getImagenFile(), "clientes");
            cliente.setImagenUrl(nuevaUrl);
        } else if (dto.getId() == 0) {
            throw new RuntimeException("Debe seleccionar una imagen para un nuevo cliente");
        }

        clienteRepository.save(cliente);
    }

    public void eliminar(int id) throws IOException {
        Cliente cliente = buscarPorId(id);
        if (cliente.getImagenUrl() != null && !cliente.getImagenUrl().isEmpty()) {
            String publicId = extractPublicIdFromUrl(cliente.getImagenUrl());
            if (publicId != null) cloudinaryService.eliminarImagen(publicId);
        }
        clienteRepository.deleteById(id);
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

    public ClienteDTO toDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setImagenUrl(cliente.getImagenUrl());
        dto.setOrden(cliente.getOrden());
        dto.setActivo(cliente.isActivo());
        return dto;
    }
}