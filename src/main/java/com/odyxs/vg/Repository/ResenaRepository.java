package com.odyxs.vg.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.odyxs.vg.Entity.Resenas;

public interface ResenaRepository extends JpaRepository<Resenas, Long> {
    List<Resenas> findByLugarId(Long lugarId);
}