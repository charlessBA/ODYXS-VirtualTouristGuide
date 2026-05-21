package com.odyxs.vg.service;

import com.odyxs.vg.model.Resena;
import java.util.List;

public interface ResenaService {
    String guardar(Long lugarId, Long usuarioId, String comentario, Integer calificacion);
    String editar(Long resenaId, Long usuarioId, String comentario, Integer calificacion);
    String eliminar(Long resenaId, Long usuarioId);
    List<Resena> obtenerPorLugar(Long lugarId);
    Resena obtenerPorId(Long id);
}
