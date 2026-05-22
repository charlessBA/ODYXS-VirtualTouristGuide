package com.odyxs.vg.controller;

import com.odyxs.vg.model.*;
import com.odyxs.vg.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class LugarController {

    @Autowired private LugarService      lugarService;
    @Autowired private ResenaService     resenaService;
    @Autowired private CategoriaService  categoriaService;

    // ── Listado por categoria ─────────────────────────────────
    @GetMapping("/lugares")
    public String lugares(@RequestParam(required = false) Long categoriaId,
                          Model model, HttpSession session) {
        if (session.getAttribute("usuarioId") == null) return "redirect:/login";

        List<Lugar> lista = (categoriaId == null || categoriaId == 0)
                ? lugarService.obtenerAprobados()
                : lugarService.obtenerPorCategoriaAprobados(categoriaId);

        model.addAttribute("lugares",     lista);
        model.addAttribute("categorias",  categoriaService.obtenerTodas());
        model.addAttribute("categoriaId", categoriaId);
        model.addAttribute("catNombre",
            lista.isEmpty() || categoriaId == null || categoriaId == 0 ? "Todos los Lugares"
                : lista.get(0).getCategoria().getNombre());
        return "lugares";
    }

    // ── Detalle ───────────────────────────────────────────────
    @GetMapping("/lugares/{id}")
    public String detalle(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("usuarioId") == null) return "redirect:/login";
        Lugar lugar = lugarService.obtenerPorId(id);
        if (lugar == null) return "redirect:/menu";
        model.addAttribute("lugar",   lugar);
        model.addAttribute("resenas", resenaService.obtenerPorLugar(id));
        return "detalle";
    }

    // ── Busqueda global ───────────────────────────────────────
    @GetMapping("/buscar")
    public String buscar(@RequestParam(defaultValue = "") String q,
                         Model model, HttpSession session) {
        if (session.getAttribute("usuarioId") == null) return "redirect:/login";
        List<Lugar> resultados = q.isBlank()
                ? lugarService.obtenerAprobados()
                : lugarService.buscar(q);
        model.addAttribute("lugares", resultados);
        model.addAttribute("query",   q);
        return "buscar";
    }

    // ── Proponer lugar ────────────────────────────────────────
    @GetMapping("/proponer-lugar")
    public String proponerForm(Model model, HttpSession session) {
        if (session.getAttribute("usuarioId") == null) return "redirect:/login";
        model.addAttribute("categorias", categoriaService.obtenerTodas());
        return "proponer-lugar";
    }

    @PostMapping("/proponer-lugar")
    public String proponerLugar(@RequestParam String nombre,
                                @RequestParam(required = false) String descripcion,
                                @RequestParam String ubicacion,
                                @RequestParam Long categoriaId,
                                @RequestParam(required = false) String urlMapa,
                                @RequestParam(required = false) MultipartFile imagen,
                                Model model, HttpSession session) {
        if (session.getAttribute("usuarioId") == null) return "redirect:/login";

        String resultado = lugarService.guardar(categoriaId, nombre, descripcion,
                                                ubicacion, urlMapa, false, imagen);

        if (resultado.equals("Lugar guardado.")) {
            model.addAttribute("mensaje", "¡Propuesta enviada! Será revisada por el equipo ODYXS.");
        } else {
            model.addAttribute("error", resultado);
        }
        model.addAttribute("categorias", categoriaService.obtenerTodas());
        return "proponer-lugar";
    }
}
