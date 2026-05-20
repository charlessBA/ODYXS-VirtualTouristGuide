package com.odyxs.vg.service.impl;

import com.odyxs.vg.model.Evento;
import com.odyxs.vg.repository.EventoRepository;
import com.odyxs.vg.service.EventoService;
import com.odyxs.vg.service.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventoServiceImpl implements EventoService {

    private final EventoRepository eventoRepository;
    private final FileStorageService fileStorageService;

    public EventoServiceImpl(EventoRepository eventoRepository,
                             FileStorageService fileStorageService) {
        this.eventoRepository = eventoRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public List<Evento> obtenerActivos() {
        return eventoRepository.findByActivoTrueOrderByFechaAsc();
    }

    @Override
    public List<Evento> obtenerActivosDesde(LocalDate desde) {
        return eventoRepository.findByActivoTrueAndFechaGreaterThanEqualOrderByFechaAsc(desde);
    }

    @Override
    public List<Evento> obtenerPendientes() {
        return eventoRepository.findByActivoFalseOrderByIdDesc();
    }

    @Override
    public List<Evento> obtenerTodos() {
        return eventoRepository.findByActivoTrueOrderByFechaAsc();
    }

    @Override
    public Evento obtenerPorId(Long id) {
        return eventoRepository.findById(id).orElse(null);
    }

    @Override
    public String guardar(String nombre, String descripcion, String fecha,
                          String lugar, MultipartFile imagen, boolean activo) {
        Evento e = new Evento();
        e.setNombre(nombre);
        e.setDescripcion(descripcion);
        e.setFecha(LocalDate.parse(fecha));
        e.setLugar(lugar);
        e.setImagenUrl(fileStorageService.guardar(imagen, "eventos"));
        e.setActivo(activo);
        eventoRepository.save(e);
        return "Evento guardado.";
    }

    @Override
    public String actualizar(Long id, String nombre, String descripcion,
                             String fecha, String lugar, MultipartFile imagen) {
        Evento e = eventoRepository.findById(id).orElse(null);
        if (e == null) return "Evento no encontrado.";
        e.setNombre(nombre);
        e.setDescripcion(descripcion);
        e.setFecha(LocalDate.parse(fecha));
        e.setLugar(lugar);
        String nuevaUrl = fileStorageService.guardar(imagen, "eventos");
        if (nuevaUrl != null) {
            fileStorageService.eliminar(e.getImagenUrl());
            e.setImagenUrl(nuevaUrl);
        }
        eventoRepository.save(e);
        return "Evento actualizado.";
    }

    @Override
    public String aprobar(Long id) {
        Evento e = eventoRepository.findById(id).orElse(null);
        if (e == null) return "Evento no encontrado.";
        e.setActivo(true);
        eventoRepository.save(e);
        return "Evento aprobado.";
    }

    @Override
    public String rechazar(Long id) {
        Evento e = eventoRepository.findById(id).orElse(null);
        if (e == null) return "Evento no encontrado.";
        fileStorageService.eliminar(e.getImagenUrl());
        eventoRepository.deleteById(id);
        return "Evento rechazado.";
    }

    @Override
    public String eliminar(Long id) {
        Evento e = eventoRepository.findById(id).orElse(null);
        if (e == null) return "Evento no encontrado.";
        fileStorageService.eliminar(e.getImagenUrl());
        eventoRepository.deleteById(id);
        return "Evento eliminado.";
    }
}
