package com.odyxs.vg.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.odyxs.vg.model.Resena;

public interface ResenaRepository extends JpaRepository<Resena, Long> {
    List<Resena> findByLugarId(Long lugarId);
}