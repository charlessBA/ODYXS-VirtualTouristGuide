package com.odyxs.vg.controller;

import com.odyxs.vg.service.EventoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Controller
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping("/eventos")
    public String eventos(@RequestParam(required = false) String desde,
                          Model model, HttpSession session) {
        if (session.getAttribute("usuarioId") == null) return "redirect:/login";

        if (desde != null && !desde.isBlank()) {
            model.addAttribute("eventos", eventoService.obtenerActivosDesde(LocalDate.parse(desde)));
        } else {
            model.addAttribute("eventos", eventoService.obtenerActivos());
        }
        model.addAttribute("desde", desde);
        return "eventos";
    }

    @GetMapping("/proponer-evento")
    public String proponerForm(HttpSession session) {
        if (session.getAttribute("usuarioId") == null) return "redirect:/login";
        return "proponer-evento";
    }

    @PostMapping("/proponer-evento")
    public String proponerEvento(@RequestParam String nombre,
                                 @RequestParam(required = false) String descripcion,
                                 @RequestParam String fecha,
                                 @RequestParam(required = false) String lugar,
                                 @RequestParam(required = false) MultipartFile imagen,
                                 Model model, HttpSession session) {
        if (session.getAttribute("usuarioId") == null) return "redirect:/login";
        eventoService.guardar(nombre, descripcion, fecha, lugar, imagen, false);
        model.addAttribute("mensaje", "¡Propuesta de evento enviada! Será revisada por el equipo ODYXS.");
        return "proponer-evento";
    }
}
