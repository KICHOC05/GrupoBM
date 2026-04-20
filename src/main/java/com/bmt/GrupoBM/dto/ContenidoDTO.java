package com.bmt.GrupoBM.dto;

import org.springframework.web.multipart.MultipartFile;

public class ContenidoDTO {
    private String heroTitulo;
    private String heroSubtitulo;
    private String nosotrosTitulo;
    private String nosotrosParrafo1;
    private String nosotrosParrafo2;
    private String nosotrosParrafo3;
    private MultipartFile logoFile;

    // Getters y Setters
    public String getHeroTitulo() { return heroTitulo; }
    public void setHeroTitulo(String heroTitulo) { this.heroTitulo = heroTitulo; }

    public String getHeroSubtitulo() { return heroSubtitulo; }
    public void setHeroSubtitulo(String heroSubtitulo) { this.heroSubtitulo = heroSubtitulo; }

    public String getNosotrosTitulo() { return nosotrosTitulo; }
    public void setNosotrosTitulo(String nosotrosTitulo) { this.nosotrosTitulo = nosotrosTitulo; }

    public String getNosotrosParrafo1() { return nosotrosParrafo1; }
    public void setNosotrosParrafo1(String nosotrosParrafo1) { this.nosotrosParrafo1 = nosotrosParrafo1; }

    public String getNosotrosParrafo2() { return nosotrosParrafo2; }
    public void setNosotrosParrafo2(String nosotrosParrafo2) { this.nosotrosParrafo2 = nosotrosParrafo2; }

    public String getNosotrosParrafo3() { return nosotrosParrafo3; }
    public void setNosotrosParrafo3(String nosotrosParrafo3) { this.nosotrosParrafo3 = nosotrosParrafo3; }

    public MultipartFile getLogoFile() { return logoFile; }
    public void setLogoFile(MultipartFile logoFile) { this.logoFile = logoFile; }
}