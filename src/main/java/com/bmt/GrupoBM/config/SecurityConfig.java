package com.bmt.GrupoBM.config;

import com.bmt.GrupoBM.models.AppUser;
import com.bmt.GrupoBM.repository.AppUserRepository;
import com.bmt.GrupoBM.service.AppUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AppUserService appUserService;
    
    @Autowired
    private AppUserRepository appUserRepository;

    public SecurityConfig(@Lazy AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .userDetailsService(appUserService)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/img/**", "/webjars/**", "/favicon.ico").permitAll()
                .requestMatchers("/", "/index", "/login", "/logout", "/error", "/api/servicios/**", "/api/galeria/**", "/api/clientes/**", "/blog/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(customSuccessHandler())
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .build();
    }

    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, 
                                                HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {
                
                System.out.println("========== LOGIN EXITOSO ==========");
                System.out.println("Usuario: " + authentication.getName());
                
                // Obtener el usuario completo de la base de datos
                Optional<AppUser> userOpt = appUserRepository.findByEmail(authentication.getName());
                
                if (userOpt.isPresent()) {
                    AppUser user = userOpt.get();
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    System.out.println("Usuario guardado en sesión: " + user.getNombre());
                    System.out.println("Rol: " + user.getRol());
                }
                
                System.out.println("Redirigiendo a /admin/dashboard");
                response.sendRedirect("/admin/dashboard");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}