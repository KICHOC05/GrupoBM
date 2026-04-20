package com.bmt.GrupoBM.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "servicios")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(columnDefinition = "TEXT")
    private String mensaje;

    private boolean activo = true;

    // Características separadas por | → "Característica 1|Característica 2"
    @Column(columnDefinition = "TEXT")
    private String caracteristicas;

    // URLs de Cloudinary separadas por | → "https://res.cloudinary.com/.../a.webp|..."
    @Column(columnDefinition = "TEXT")
    private String imagenes;

    // ── Getters y Setters ──────────────────────────────────────────
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getCaracteristicas() { return caracteristicas; }
    public void setCaracteristicas(String caracteristicas) { this.caracteristicas = caracteristicas; }

    public String getImagenes() { return imagenes; }
    public void setImagenes(String imagenes) { this.imagenes = imagenes; }

    // ── Helpers ───────────────────────────────────────────────────
    public List<String> getCaracteristicasList() {
        if (caracteristicas == null || caracteristicas.isBlank()) return new ArrayList<>();
        return List.of(caracteristicas.split("\\|"));
    }

    public List<String> getImagenesList() {
        if (imagenes == null || imagenes.isBlank()) return new ArrayList<>();
        return List.of(imagenes.split("\\|"));
    }

    // Devuelve la primera URL para mostrar como miniatura
    public String getImagenPrincipal() {
        List<String> list = getImagenesList();
        return list.isEmpty() ? "" : list.get(0);
    }
}