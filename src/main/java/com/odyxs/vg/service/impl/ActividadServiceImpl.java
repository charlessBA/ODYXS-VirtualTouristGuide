package com.odyxs.vg.service.impl;

import com.odyxs.vg.model.Actividad;
import com.odyxs.vg.repository.ActividadRepository;
import com.odyxs.vg.service.ActividadService;
import com.odyxs.vg.service.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ActividadServiceImpl implements ActividadService {

    private final ActividadRepository actividadRepository;
    private final FileStorageService fileStorageService;

    public ActividadServiceImpl(ActividadRepository actividadRepository,
                                FileStorageService fileStorageService) {
        this.actividadRepository = actividadRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public List<Actividad> obtenerTodas() {
        return actividadRepository.findAll();
    }

    @Override
    public List<Actividad> obtenerAprobadas() {
        return actividadRepository.findByEstado(Actividad.Estado.APROBADO);
    }

    @Override
    public List<Actividad> obtenerPendientes() {
        return actividadRepository.findByEstado(Actividad.Estado.PENDIENTE);
    }

    @Override
    public List<Actividad> obtenerPorCategoria(Actividad.CategoriaActividad categoria) {
        return actividadRepository.findByCategoriaAndEstado(categoria, Actividad.Estado.APROBADO);
    }

    @Override
    public Actividad obtenerPorId(Long id) {
        return actividadRepository.findById(id).orElse(null);
    }

    @Override
    public String guardar(String nombre, String descripcion, String duracion,
                          String precioAprox, String categoriaStr,
                          boolean esOficial, MultipartFile imagen) {
        try {
            Actividad.CategoriaActividad cat =
                Actividad.CategoriaActividad.valueOf(categoriaStr.toUpperCase());

            String imagenUrl = fileStorageService.guardar(imagen, "actividades");
            if (imagen != null && !imagen.isEmpty() && imagenUrl == null)
                return "Error al guardar la imagen.";

            Actividad a = new Actividad();
            a.setNombre(nombre);
            a.setDescripcion(descripcion);
            a.setDuracion(duracion);
            a.setPrecioAprox(precioAprox);
            a.setCategoria(cat);
            a.setEsOficial(esOficial);
            a.setEstado(esOficial ? Actividad.Estado.APROBADO : Actividad.Estado.PENDIENTE);
            a.setImagenUrl(imagenUrl);
            actividadRepository.save(a);
            return "Actividad guardada.";
        } catch (IllegalArgumentException e) {
            return "Categoría inválida.";
        }
    }

    @Override
    public String actualizar(Long id, String nombre, String descripcion, String duracion,
                             String precioAprox, String categoriaStr, MultipartFile imagen) {
        Actividad a = actividadRepository.findById(id).orElse(null);
        if (a == null) return "Actividad no encontrada.";
        try {
            a.setNombre(nombre);
            a.setDescripcion(descripcion);
            a.setDuracion(duracion);
            a.setPrecioAprox(precioAprox);
            a.setCategoria(Actividad.CategoriaActividad.valueOf(categoriaStr.toUpperCase()));
            if (imagen != null && !imagen.isEmpty()) {
                String url = fileStorageService.guardar(imagen, "actividades");
                if (url != null) {
                    fileStorageService.eliminar(a.getImagenUrl());
                    a.setImagenUrl(url);
                }
            }
            actividadRepository.save(a);
            return "Actividad actualizada.";
        } catch (IllegalArgumentException e) {
            return "Categoría inválida.";
        }
    }

    @Override
    public String aprobar(Long id) {
        Actividad a = actividadRepository.findById(id).orElse(null);
        if (a == null) return "Actividad no encontrada.";
        a.setEstado(Actividad.Estado.APROBADO);
        actividadRepository.save(a);
        return "Actividad aprobada.";
    }

    @Override
    public String rechazar(Long id) {
        Actividad a = actividadRepository.findById(id).orElse(null);
        if (a == null) return "Actividad no encontrada.";
        a.setEstado(Actividad.Estado.RECHAZADO);
        actividadRepository.save(a);
        return "Actividad rechazada.";
    }

    @Override
    public String eliminar(Long id) {
        Actividad a = actividadRepository.findById(id).orElse(null);
        if (a == null) return "Actividad no encontrada.";
        fileStorageService.eliminar(a.getImagenUrl());
        actividadRepository.deleteById(id);
        return "Actividad eliminada.";
    }
}
