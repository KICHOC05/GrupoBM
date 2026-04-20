package com.bmt.GrupoBM.dto;

import org.springframework.web.multipart.MultipartFile;

public class ClienteDTO {
    private int id;
    private String nombre;
    private String imagenUrl;
    private int orden;
    private boolean activo = true;
    private MultipartFile imagenFile;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public MultipartFile getImagenFile() { return imagenFile; }
    public void setImagenFile(MultipartFile imagenFile) { this.imagenFile = imagenFile; }
}