package com.odyxs.vg.service.impl;

import com.odyxs.vg.model.Categoria;
import com.odyxs.vg.model.Lugar;
import com.odyxs.vg.repository.CategoriaRepository;
import com.odyxs.vg.repository.LugarRepository;
import com.odyxs.vg.service.FileStorageService;
import com.odyxs.vg.service.LugarService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class LugarServiceImpl implements LugarService {

    private final LugarRepository lugarRepository;
    private final CategoriaRepository categoriaRepository;
    private final FileStorageService fileStorageService;

    public LugarServiceImpl(LugarRepository lugarRepository,
                            CategoriaRepository categoriaRepository,
                            FileStorageService fileStorageService) {
        this.lugarRepository = lugarRepository;
        this.categoriaRepository = categoriaRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public List<Lugar> obtenerTodos() {
        return lugarRepository.findAll();
    }

    @Override
    public Lugar obtenerPorId(Long id) {
        return lugarRepository.findById(id).orElse(null);
    }

    @Override
    public List<Lugar> obtenerPorCategoria(Long categoriaId) {
        return lugarRepository.findByCategoriaId(categoriaId);
    }

    @Override
    public List<Lugar> obtenerAprobados() {
        return lugarRepository.findByEstado(Lugar.Estado.APROBADO);
    }

    @Override
    public List<Lugar> obtenerPendientes() {
        return lugarRepository.findByEstado(Lugar.Estado.PENDIENTE);
    }

    @Override
    public List<Lugar> obtenerPorCategoriaAprobados(Long categoriaId) {
        return lugarRepository.findByCategoriaIdAndEstado(categoriaId, Lugar.Estado.APROBADO);
    }

    @Override
    public List<Lugar> buscar(String texto) {
        return lugarRepository.findByNombreContainingIgnoreCaseAndEstado(texto, Lugar.Estado.APROBADO);
    }

    @Override
    public String guardar(Long categoriaId, String nombre, String descripcion,
                          String ubicacion, String urlMapa, boolean esOficial,
                          MultipartFile imagen) {
        return guardar(categoriaId, nombre, descripcion, ubicacion, urlMapa, esOficial, imagen, null);
    }

    @Override
    public String guardar(Long categoriaId, String nombre, String descripcion,
                          String ubicacion, String urlMapa, boolean esOficial,
                          MultipartFile imagen, String imagenUrlExterna) {
        Categoria categoria = categoriaRepository.findById(categoriaId).orElse(null);
        if (categoria == null) return "Categoría no encontrada.";

        if (lugarRepository.existsByNombreAndDescripcionAndUbicacionAndUrlMapa(
                nombre, descripcion, ubicacion, urlMapa)) {
            return "Ya existe un lugar con el mismo nombre, descripción, ubicación y URL.";
        }

        String imagenUrlGuardada = null;
        if (imagen != null && !imagen.isEmpty()) {
            imagenUrlGuardada = fileStorageService.guardar(imagen, "lugares");
            if (imagenUrlGuardada == null) return "Error al guardar la imagen.";
        } else if (imagenUrlExterna != null && !imagenUrlExterna.isBlank()) {
            imagenUrlGuardada = imagenUrlExterna.trim();
        }

        Lugar lugar = new Lugar();
        lugar.setNombre(nombre);
        lugar.setDescripcion(descripcion);
        lugar.setUbicacion(ubicacion);
        lugar.setUrlMapa(urlMapa);
        lugar.setEsOficial(esOficial);
        lugar.setEstado(esOficial ? Lugar.Estado.APROBADO : Lugar.Estado.PENDIENTE);
        lugar.setCategoria(categoria);
        lugar.setImagenUrl(imagenUrlGuardada);
        lugarRepository.save(lugar);
        return "Lugar guardado.";
    }

    @Override
    public String actualizar(Long id, String nombre, String descripcion, String ubicacion,
                             String urlMapa, Long categoriaId, MultipartFile imagen,
                             String imagenUrlExterna) {
        Lugar lugar = lugarRepository.findById(id).orElse(null);
        if (lugar == null) return "Lugar no encontrado.";

        lugar.setNombre(nombre);
        lugar.setDescripcion(descripcion);
        lugar.setUbicacion(ubicacion);
        lugar.setUrlMapa(urlMapa);

        Categoria cat = categoriaRepository.findById(categoriaId).orElse(null);
        if (cat != null) lugar.setCategoria(cat);

        if (imagen != null && !imagen.isEmpty()) {
            String url = fileStorageService.guardar(imagen, "lugares");
            if (url != null) {
                fileStorageService.eliminar(lugar.getImagenUrl());
                lugar.setImagenUrl(url);
            }
        } else if (imagenUrlExterna != null && !imagenUrlExterna.isBlank()) {
            fileStorageService.eliminar(lugar.getImagenUrl());
            lugar.setImagenUrl(imagenUrlExterna.trim());
        }

        lugarRepository.save(lugar);
        return "Lugar actualizado.";
    }

    @Override
    public String actualizarImagen(Long id, MultipartFile imagen) {
        Lugar lugar = lugarRepository.findById(id).orElse(null);
        if (lugar == null) return "Lugar no encontrado.";
        if (imagen == null || imagen.isEmpty()) return "No se envió imagen.";
        String url = fileStorageService.guardar(imagen, "lugares");
        if (url == null) return "Error al guardar la imagen.";
        fileStorageService.eliminar(lugar.getImagenUrl());
        lugar.setImagenUrl(url);
        lugarRepository.save(lugar);
        return "Imagen actualizada.";
    }

    @Override
    public String aprobar(Long id) {
        Lugar lugar = lugarRepository.findById(id).orElse(null);
        if (lugar == null) return "Lugar no encontrado.";
        lugar.setEstado(Lugar.Estado.APROBADO);
        lugarRepository.save(lugar);
        return "Lugar aprobado.";
    }

    @Override
    public String rechazar(Long id) {
        Lugar lugar = lugarRepository.findById(id).orElse(null);
        if (lugar == null) return "Lugar no encontrado.";
        lugar.setEstado(Lugar.Estado.RECHAZADO);
        lugarRepository.save(lugar);
        return "Lugar rechazado.";
    }

    @Override
    public String eliminar(Long id) {
        Lugar lugar = lugarRepository.findById(id).orElse(null);
        if (lugar == null) return "Lugar no encontrado.";
        fileStorageService.eliminar(lugar.getImagenUrl());
        lugarRepository.deleteById(id);
        return "Lugar eliminado.";
    }
}
