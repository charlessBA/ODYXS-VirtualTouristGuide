package com.odyxs.vg.service;

import com.odyxs.vg.model.Evento;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.List;

public interface EventoService {
    List<Evento> obtenerActivos();
    List<Evento> obtenerActivosDesde(LocalDate desde);
    List<Evento> obtenerPendientes();
    List<Evento> obtenerTodos();
    Evento obtenerPorId(Long id);
    String guardar(String nombre, String descripcion, String fecha,
                   String lugar, MultipartFile imagen, boolean activo);
    String actualizar(Long id, String nombre, String descripcion,
                      String fecha, String lugar, MultipartFile imagen);
    String aprobar(Long id);
    String rechazar(Long id);
    String eliminar(Long id);
}
