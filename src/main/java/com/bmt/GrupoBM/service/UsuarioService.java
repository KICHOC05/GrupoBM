package com.bmt.GrupoBM.service;

import com.bmt.GrupoBM.dto.UsuarioDTO;
import com.bmt.GrupoBM.models.AppUser;
import com.bmt.GrupoBM.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<AppUser> listarUsuarios(Pageable pageable, String busqueda) {
        if (busqueda != null && !busqueda.isEmpty()) {
            return userRepository.findByNombreContainingIgnoreCaseOrEmailContainingIgnoreCase(busqueda, busqueda, pageable);
        }
        return userRepository.findAll(pageable);
    }

    public AppUser buscarPorId(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    public boolean existeEmail(String email, int idActual) {
        Optional<AppUser> existing = userRepository.findByEmail(email);
        return existing.isPresent() && existing.get().getId() != idActual;
    }

    public void guardar(UsuarioDTO dto, boolean cambiarContrasena) {
        AppUser user;
        if (dto.getId() > 0) {
            user = buscarPorId(dto.getId());
        } else {
            user = new AppUser();
        }

        user.setNombre(dto.getNombre());
        user.setApellido(dto.getApellido());
        user.setEmail(dto.getEmail());
        user.setTelefono(dto.getTelefono());
        user.setDireccion(dto.getDireccion());
        user.setRol(dto.getRol());

        // Manejo de contraseña
        if (cambiarContrasena && dto.getContrasena() != null && !dto.getContrasena().isEmpty()) {
            user.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        } else if (user.getContrasena() == null) {
            // Para usuarios nuevos sin contraseña (nunca debería pasar porque el formulario la exige)
            throw new RuntimeException("La contraseña es obligatoria para usuarios nuevos");
        }

        userRepository.save(user);
    }

    public void eliminar(int id) {
        // Opcional: impedir que un admin se elimine a sí mismo (se puede validar en controller)
        userRepository.deleteById(id);
    }

    public void cambiarRol(int id, String nuevoRol) {
        AppUser user = buscarPorId(id);
        user.setRol(nuevoRol);
        userRepository.save(user);
    }

    /**
     * Retorna el número total de usuarios registrados.
     * @return total de usuarios
     */
    public long countAll() {
        return userRepository.count();
    }

    public UsuarioDTO toDTO(AppUser user) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(user.getId());
        dto.setNombre(user.getNombre());
        dto.setApellido(user.getApellido());
        dto.setEmail(user.getEmail());
        dto.setTelefono(user.getTelefono());
        dto.setDireccion(user.getDireccion());
        dto.setRol(user.getRol());
        // La contraseña no se pasa al DTO por seguridad
        return dto;
    }
}