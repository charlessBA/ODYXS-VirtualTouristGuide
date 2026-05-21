package com.odyxs.vg.service;

import com.odyxs.vg.model.Actividad;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ActividadService {
    List<Actividad> obtenerTodas();
    List<Actividad> obtenerAprobadas();
    List<Actividad> obtenerPendientes();
    List<Actividad> obtenerPorCategoria(Actividad.CategoriaActividad categoria);
    Actividad obtenerPorId(Long id);
    String guardar(String nombre, String descripcion, String duracion,
                   String precioAprox, String categoriaStr, boolean esOficial, MultipartFile imagen);
    String actualizar(Long id, String nombre, String descripcion, String duracion,
                      String precioAprox, String categoriaStr, MultipartFile imagen);
    String aprobar(Long id);
    String rechazar(Long id);
    String eliminar(Long id);
}
