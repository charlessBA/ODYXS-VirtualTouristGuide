package com.odyxs.vg.service.impl;

import com.odyxs.vg.model.Lugar;
import com.odyxs.vg.model.Resena;
import com.odyxs.vg.model.Usuario;
import com.odyxs.vg.repository.LugarRepository;
import com.odyxs.vg.repository.ResenaRepository;
import com.odyxs.vg.repository.UsuarioRepository;
import com.odyxs.vg.service.ResenaService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ResenaServiceImpl implements ResenaService {

    private final ResenaRepository resenaRepository;
    private final LugarRepository lugarRepository;
    private final UsuarioRepository usuarioRepository;

    public ResenaServiceImpl(ResenaRepository resenaRepository,
                             LugarRepository lugarRepository,
                             UsuarioRepository usuarioRepository) {
        this.resenaRepository = resenaRepository;
        this.lugarRepository = lugarRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public String guardar(Long lugarId, Long usuarioId, String comentario, Integer calificacion) {
        Lugar lugar = lugarRepository.findById(lugarId).orElse(null);
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (lugar == null || usuario == null) return "Lugar o usuario no encontrado.";
        if (calificacion < 1 || calificacion > 5) return "La calificación debe estar entre 1 y 5.";

        Resena resena = new Resena();
        resena.setLugar(lugar);
        resena.setUsuario(usuario);
        resena.setComentario(comentario);
        resena.setCalificacion(calificacion);
        resena.setFecha(LocalDate.now());
        resenaRepository.save(resena);
        return "Reseña guardada.";
    }

    @Override
    public String editar(Long resenaId, Long usuarioId, String comentario, Integer calificacion) {
        Resena resena = resenaRepository.findById(resenaId).orElse(null);
        if (resena == null) return "Reseña no encontrada.";
        if (!resena.getUsuario().getId().equals(usuarioId)) return "No tienes permiso para editar esta reseña.";
        if (calificacion < 1 || calificacion > 5) return "La calificación debe estar entre 1 y 5.";
        resena.setComentario(comentario);
        resena.setCalificacion(calificacion);
        resenaRepository.save(resena);
        return "Reseña actualizada.";
    }

    @Override
    public String eliminar(Long resenaId, Long usuarioId) {
        Resena resena = resenaRepository.findById(resenaId).orElse(null);
        if (resena == null) return "Reseña no encontrada.";
        if (!resena.getUsuario().getId().equals(usuarioId)) return "No tienes permiso para eliminar esta reseña.";
        resenaRepository.deleteById(resenaId);
        return "Reseña eliminada.";
    }

    @Override
    public List<Resena> obtenerPorLugar(Long lugarId) {
        return resenaRepository.findByLugarId(lugarId);
    }

    @Override
    public Resena obtenerPorId(Long id) {
        return resenaRepository.findById(id).orElse(null);
    }
}
