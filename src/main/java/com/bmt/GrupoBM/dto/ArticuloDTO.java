package com.bmt.GrupoBM.dto;

import org.springframework.web.multipart.MultipartFile;

public class ArticuloDTO {
    private int id;
    private String titulo;
    private String resumen;
    private String contenido;
    private String imagenPortada;
    private MultipartFile imagenFile;
    private String categoria;
    private boolean destacado;
    private boolean activo;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getResumen() { return resumen; }
    public void setResumen(String resumen) { this.resumen = resumen; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public String getImagenPortada() { return imagenPortada; }
    public void setImagenPortada(String imagenPortada) { this.imagenPortada = imagenPortada; }

    public MultipartFile getImagenFile() { return imagenFile; }
    public void setImagenFile(MultipartFile imagenFile) { this.imagenFile = imagenFile; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public boolean isDestacado() { return destacado; }
    public void setDestacado(boolean destacado) { this.destacado = destacado; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}