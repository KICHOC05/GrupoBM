package com.bmt.GrupoBM.service;

import com.bmt.GrupoBM.models.AppUser;
import com.bmt.GrupoBM.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Configuration
@Service
public class AppUserService implements UserDetailsService {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Spring Security llama este método al hacer login
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getContrasena(),
            List.of(new SimpleGrantedAuthority(user.getRol()))
        );
    }

    // Inserta un admin de prueba al arrancar si no existe
    @Bean
    public CommandLineRunner insertarAdminDePrueba() {
        return args -> {
            if (userRepository.findByEmail("admin@grupobm.com").isEmpty()) {
                AppUser admin = new AppUser();
                admin.setNombre("Admin");
                admin.setApellido("GrupoBM");
                admin.setEmail("admin@grupobm.com");
                admin.setContrasena(passwordEncoder.encode("admin123"));
                admin.setRol("ROLE_ADMIN");
                userRepository.save(admin);
                System.out.println("✅ Admin de prueba creado: admin@grupobm.com / admin123");
            }
        };
    }
}