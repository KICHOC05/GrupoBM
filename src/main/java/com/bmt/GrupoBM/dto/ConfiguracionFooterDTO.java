package com.bmt.GrupoBM.dto;

public class ConfiguracionFooterDTO {
    private Long id;
    private String descripcion;
    private String direccion;
    private String whatsapp;
    private String email;
    private String instagramTexto;
    private String instagramUrl;
    private String facebookTexto;
    private String facebookUrl;
    private String horarioLaboral;
    private String horarioSabado;
    private String copyright;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getWhatsapp() { return whatsapp; }
    public void setWhatsapp(String whatsapp) { this.whatsapp = whatsapp; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getInstagramTexto() { return instagramTexto; }
    public void setInstagramTexto(String instagramTexto) { this.instagramTexto = instagramTexto; }

    public String getInstagramUrl() { return instagramUrl; }
    public void setInstagramUrl(String instagramUrl) { this.instagramUrl = instagramUrl; }

    public String getFacebookTexto() { return facebookTexto; }
    public void setFacebookTexto(String facebookTexto) { this.facebookTexto = facebookTexto; }

    public String getFacebookUrl() { return facebookUrl; }
    public void setFacebookUrl(String facebookUrl) { this.facebookUrl = facebookUrl; }

    public String getHorarioLaboral() { return horarioLaboral; }
    public void setHorarioLaboral(String horarioLaboral) { this.horarioLaboral = horarioLaboral; }

    public String getHorarioSabado() { return horarioSabado; }
    public void setHorarioSabado(String horarioSabado) { this.horarioSabado = horarioSabado; }

    public String getCopyright() { return copyright; }
    public void setCopyright(String copyright) { this.copyright = copyright; }
}