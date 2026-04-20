package com.bmt.GrupoBM.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    /**
     * Sube una imagen a Cloudinary.
     * @param archivo  archivo recibido del formulario
     * @param carpeta  subcarpeta dentro de grupobm/ (ej: "servicios")
     * @return URL HTTPS pública de la imagen subida
     */
    public String subirImagen(MultipartFile archivo, String carpeta) throws IOException {
        Map resultado = cloudinary.uploader().upload(
            archivo.getBytes(),
            ObjectUtils.asMap(
                "folder",        "grupobm/" + carpeta,
                "resource_type", "auto"
            )
        );
        return (String) resultado.get("secure_url");
    }

    /**
     * Elimina una imagen de Cloudinary por su public_id.
     * El public_id es la parte de la URL sin dominio ni extensión.
     * Ej: "grupobm/servicios/mi-imagen"
     */
    public void eliminarImagen(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}