package com.odyxs.vg.service;

import com.odyxs.vg.model.Categoria;
import java.util.List;

public interface CategoriaService {
    List<Categoria> obtenerTodas();
    Categoria obtenerPorId(Long id);
    String guardar(String nombre);
}
