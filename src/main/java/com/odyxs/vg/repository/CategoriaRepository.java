package com.odyxs.vg.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.odyxs.vg.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    boolean existsByNombre(String nombre);
}
