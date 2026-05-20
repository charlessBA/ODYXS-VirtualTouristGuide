package com.odyxs.vg.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    /** Guarda la imagen en uploads/{subdirectorio}/ y retorna la URL relativa o null si falla. */
    String guardar(MultipartFile imagen, String subdirectorio);
    /** Elimina la imagen física si su URL empieza por /uploads/. Ignora URLs externas. */
    void eliminar(String imagenUrl);
}
