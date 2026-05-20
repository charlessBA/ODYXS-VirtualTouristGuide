package com.odyxs.vg.service.impl;

import com.odyxs.vg.model.Categoria;
import com.odyxs.vg.repository.CategoriaRepository;
import com.odyxs.vg.service.CategoriaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public List<Categoria> obtenerTodas() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria obtenerPorId(Long id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    @Override
    public String guardar(String nombre) {
        if (categoriaRepository.existsByNombre(nombre)) return "Ya existe esa categoría.";
        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);
        categoriaRepository.save(categoria);
        return "Categoría guardada.";
    }
}
