package com.odyxs.vg.Controller;

import com.odyxs.vg.Entity.*;
import com.odyxs.vg.Repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

@Controller
public class TurismoController {

    @Autowired private EventoRepository     eventoRepo;
    @Autowired private ActividadRepository  actividadRepo;
    @Autowired private ChatbotRespuestaRepository chatbotRepo;

    private static final String EVENTOS_DIR =
        System.getProperty("user.dir") + "/src/main/resources/static/uploads/eventos/";

    private String guardarImagenEvento(MultipartFile imagen) {
        if (imagen == null || imagen.isEmpty()) return null;
        String tipo = imagen.getContentType();
        if (tipo == null || !tipo.startsWith("image/")) return null;
        try {
            Files.createDirectories(Paths.get(EVENTOS_DIR));
            String nombreArchivo = System.currentTimeMillis() + ".jpg";
            Path destino = Paths.get(EVENTOS_DIR + nombreArchivo);
            BufferedImage img = ImageIO.read(imagen.getInputStream());
            if (img == null) return null;
            ImageIO.write(img, "jpg", destino.toFile());
            return "/uploads/eventos/" + nombreArchivo;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // ── Eventos ───────────────────────────────────────────────

    @GetMapping("/eventos")
    public String eventos(
            @RequestParam(required = false) String desde,
            Model model, HttpSession session) {
        if (session.getAttribute("usuarioId") == null) return "redirect:/login";

        List<Evento> lista;
        if (desde != null && !desde.isBlank()) {
            lista = eventoRepo.findByActivoTrueAndFechaGreaterThanEqualOrderByFechaAsc(
                        LocalDate.parse(desde));
        } else {
            lista = eventoRepo.findByActivoTrueOrderByFechaAsc();
        }
        model.addAttribute("eventos", lista);
        model.addAttribute("desde", desde);
        return "eventos";
    }

    // CRUD Admin: agregar evento
    @PostMapping("/turismo/eventos/agregar")
    public String agregarEvento(@RequestParam String nombre,
                                @RequestParam String descripcion,
                                @RequestParam String fecha,
                                @RequestParam(required = false) String lugar,
                                @RequestParam(required = false) String imagenUrl,
                                HttpSession session) {
        if (!esAdmin(session)) return "redirect:/login";

        Evento e = new Evento();
        e.setNombre(nombre);
        e.setDescripcion(descripcion);
        e.setFecha(LocalDate.parse(fecha));
        e.setLugar(lugar);
        e.setImagenUrl(imagenUrl);

        eventoRepo.save(e);

        return "redirect:/admin";
    }


    // ── Proponer evento (usuarios) ────────────────────────────

    @GetMapping("/proponer-evento")
    public String proponerEventoForm(HttpSession session) {
        if (session.getAttribute("usuarioId") == null) return "redirect:/login";
        return "proponer-evento";
    }

    @PostMapping("/proponer-evento")
    public String proponerEvento(@RequestParam String nombre,
                                 @RequestParam(required = false) String descripcion,
                                 @RequestParam String fecha,
                                 @RequestParam(required = false) String lugar,
                                 @RequestParam(required = false) MultipartFile imagen,
                                 org.springframework.ui.Model model,
                                 HttpSession session) {
        if (session.getAttribute("usuarioId") == null) return "redirect:/login";

        Evento e = new Evento();
        e.setNombre(nombre);
        e.setDescripcion(descripcion);
        e.setFecha(LocalDate.parse(fecha));
        e.setLugar(lugar);
        e.setImagenUrl(guardarImagenEvento(imagen));
        e.setActivo(false); // pendiente de aprobación admin

        eventoRepo.save(e);

        model.addAttribute("mensaje", "¡Propuesta de evento enviada! Será revisada por el equipo ODYXS.");
        return "proponer-evento";
    }

    // ── Transporte ────────────────────────────────────────────
    // Datos estáticos definidos en i18n/messages*.properties — no requiere BD

    @GetMapping("/transporte")
    public String transporte(HttpSession session) {
        if (session.getAttribute("usuarioId") == null) return "redirect:/login";
        return "transporte";
    }

    // ── Actividades ── gestionado por ActividadController ───────

    // ── Guía completa (info, clima, top5, cuándo visitar) ─────

    @GetMapping("/guia")
    public String guia(HttpSession session, Model model) {
        if (session.getAttribute("usuarioId") == null) return "redirect:/login";
        return "guia";
    }

    // ── Chatbot ───────────────────────────────────────────────

    @GetMapping("/chatbot")
    public String chatbotPage(HttpSession session) {
        if (session.getAttribute("usuarioId") == null) return "redirect:/login";
        return "chatbot";
    }

    @PostMapping("/chatbot/respuesta")
    @ResponseBody
    public ResponseEntity<Map<String, String>> responderChatbot(
            @RequestBody Map<String, String> body) {

        String mensaje = body.getOrDefault("mensaje", "").toLowerCase().trim();
        String respuesta = buscarRespuesta(mensaje);
        return ResponseEntity.ok(Map.of("respuesta", respuesta));
    }

    private String buscarRespuesta(String msg) {
        // Intentar coincidencia directa con clave
        String clave = detectarClave(msg);
        Optional<ChatbotRespuesta> opt = chatbotRepo.findByClave(clave);
        if (opt.isPresent()) return opt.get().getRespuesta();

        // Fallback genérico
        return "🤔 No estoy seguro de cómo responder a eso. Prueba preguntarme sobre:\n"
             + "• clima • playas • transporte • hoteles\n"
             + "• historia • actividades • eventos • gastronomía";
    }

    private String detectarClave(String msg) {
        if (msg.matches("hola|buenos días|buenas|hi|hey|saludos")) return "saludo";
        if (msg.contains("clima") || msg.contains("lluvi") || msg.contains("temperatur") || msg.contains("cuando visit")) return "clima";
        if (msg.contains("playa") || msg.contains("baru") || msg.contains("rosario") || msg.contains("bocagrande")) return "playas";
        if (msg.contains("transport") || msg.contains("mover") || msg.contains("llegar") || msg.contains("bus") || msg.contains("taxi") || msg.contains("yate")) return "transporte";
        if (msg.contains("hotel") || msg.contains("aloj") || msg.contains("hostal") || msg.contains("donde dormir")) return "hoteles";
        if (msg.contains("histor") || msg.contains("coloni") || msg.contains("murall") || msg.contains("castill") || msg.contains("fundaci")) return "historico";
        if (msg.contains("actividad") || msg.contains("hacer") || msg.contains("tour") || msg.contains("buceo") || msg.contains("kayak")) return "actividades";
        if (msg.contains("evento") || msg.contains("festival") || msg.contains("hay festival") || msg.contains("ficci")) return "eventos";
        if (msg.contains("costo") || msg.contains("precio") || msg.contains("caro") || msg.contains("barato") || msg.contains("dinero")) return "costo";
        if (msg.contains("comer") || msg.contains("comida") || msg.contains("gastronom") || msg.contains("restaur") || msg.contains("ceviche")) return "gastronomia";
        if (msg.contains("adios") || msg.contains("gracias") || msg.contains("bye") || msg.contains("chao")) return "despedida";
        return "saludo"; // default
    }

    // ── Mapa interactivo ──────────────────────────────────────

    @GetMapping("/mapa")
    public String mapa(HttpSession session) {
        if (session.getAttribute("usuarioId") == null) return "redirect:/login";
        return "mapa";
    }

    private boolean esAdmin(HttpSession session) {
        Object rol = session.getAttribute("usuarioRol");
        return rol != null && rol.toString().equals("ADMIN");
    }
}
