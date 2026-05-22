package com.odyxs.vg.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TurismoController {

    // ── Transporte ────────────────────────────────────────────
    @GetMapping("/transporte")
    public String transporte(HttpSession session) {
        if (session.getAttribute("usuarioId") == null) return "redirect:/login";
        return "transporte";
    }

    // ── Guía completa (info, clima, top5, cuándo visitar) ─────
    @GetMapping("/guia")
    public String guia(HttpSession session, Model model) {
        if (session.getAttribute("usuarioId") == null) return "redirect:/login";
        return "guia";
    }

    // ── Mapa interactivo ──────────────────────────────────────
    @GetMapping("/mapa")
    public String mapa(HttpSession session) {
        if (session.getAttribute("usuarioId") == null) return "redirect:/login";
        return "mapa";
    }
}
