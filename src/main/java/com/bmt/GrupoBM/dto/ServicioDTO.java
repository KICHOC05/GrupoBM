package com.bmt.GrupoBM.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public class ServicioDTO {

    private int id;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150, message = "Máximo 150 caracteres")
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    private String mensaje;

    private boolean activo = true;

    // Lista de características desde el formulario
    private List<String> caracteristicasList;

    // URLs ya guardadas en BD (imágenes existentes que se conservan)
    private List<String> imagenesList;

    // Archivos nuevos que sube el admin desde el formulario
    private List<MultipartFile> imagenesNuevas;

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

    public List<String> getCaracteristicasList() { return caracteristicasList; }
    public void setCaracteristicasList(List<String> caracteristicasList) { this.caracteristicasList = caracteristicasList; }

    public List<String> getImagenesList() { return imagenesList; }
    public void setImagenesList(List<String> imagenesList) { this.imagenesList = imagenesList; }

    public List<MultipartFile> getImagenesNuevas() { return imagenesNuevas; }
    public void setImagenesNuevas(List<MultipartFile> imagenesNuevas) { this.imagenesNuevas = imagenesNuevas; }
}