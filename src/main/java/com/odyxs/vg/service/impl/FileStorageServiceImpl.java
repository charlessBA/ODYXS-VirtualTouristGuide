package com.odyxs.vg.service.impl;

import com.odyxs.vg.service.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final String UPLOAD_BASE =
        System.getProperty("user.dir") + "/src/main/resources/static/uploads/";
    private static final long MAX_BYTES = 2 * 1024 * 1024;

    @Override
    public String guardar(MultipartFile imagen, String subdirectorio) {
        if (imagen == null || imagen.isEmpty()) return null;
        if (imagen.getSize() > MAX_BYTES) return null;
        String tipo = imagen.getContentType();
        if (tipo == null || !tipo.startsWith("image/")) return null;
        try {
            Path dir = Paths.get(UPLOAD_BASE + subdirectorio + "/");
            Files.createDirectories(dir);
            String nombreArchivo = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
            Files.copy(imagen.getInputStream(), dir.resolve(nombreArchivo),
                       StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + subdirectorio + "/" + nombreArchivo;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void eliminar(String imagenUrl) {
        if (imagenUrl == null || imagenUrl.isBlank()) return;
        if (!imagenUrl.startsWith("/uploads/")) return;
        try {
            Path p = Paths.get(UPLOAD_BASE + imagenUrl.substring("/uploads/".length()));
            Files.deleteIfExists(p);
        } catch (IOException e) {
            // best-effort
        }
    }
}
