package com.odyxs.vg.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.odyxs.vg.Entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    boolean existsByNombre(String nombre);
}
