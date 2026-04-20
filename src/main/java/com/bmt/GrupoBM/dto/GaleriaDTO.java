package com.bmt.GrupoBM.dto;

import org.springframework.web.multipart.MultipartFile;

public class GaleriaDTO {
    private int id;
    private String imagenUrl;
    private String descripcion;
    private int orden;
    private boolean activo = true;

    // Para subir nueva imagen
    private MultipartFile imagenFile;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public MultipartFile getImagenFile() { return imagenFile; }
    public void setImagenFile(MultipartFile imagenFile) { this.imagenFile = imagenFile; }
}