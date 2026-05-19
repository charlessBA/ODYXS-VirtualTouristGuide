package com.odyxs.vg.Repository;
import com.odyxs.vg.Entity.ChatbotRespuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ChatbotRespuestaRepository extends JpaRepository<ChatbotRespuesta, Long> {
    Optional<ChatbotRespuesta> findByClave(String clave);
}
