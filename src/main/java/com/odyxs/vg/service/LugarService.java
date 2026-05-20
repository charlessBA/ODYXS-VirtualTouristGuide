package com.odyxs.vg.service;

import com.odyxs.vg.model.Lugar;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface LugarService {
    List<Lugar> obtenerTodos();
    Lugar obtenerPorId(Long id);
    List<Lugar> obtenerPorCategoria(Long categoriaId);
    List<Lugar> obtenerAprobados();
    List<Lugar> obtenerPendientes();
    List<Lugar> obtenerPorCategoriaAprobados(Long categoriaId);
    List<Lugar> buscar(String texto);
    String guardar(Long categoriaId, String nombre, String descripcion,
                   String ubicacion, String urlMapa, boolean esOficial,
                   MultipartFile imagen);
    String guardar(Long categoriaId, String nombre, String descripcion,
                   String ubicacion, String urlMapa, boolean esOficial,
                   MultipartFile imagen, String imagenUrlExterna);
    String actualizar(Long id, String nombre, String descripcion, String ubicacion,
                      String urlMapa, Long categoriaId, MultipartFile imagen, String imagenUrlExterna);
    String actualizarImagen(Long id, MultipartFile imagen);
    String aprobar(Long id);
    String rechazar(Long id);
    String eliminar(Long id);
}
