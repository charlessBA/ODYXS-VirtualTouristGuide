package com.odyxs.vg.controller;

import com.odyxs.vg.service.ChatbotService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class ChatbotController {

    private final ChatbotService chatbotService;

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @GetMapping("/chatbot")
    public String chatbotPage(HttpSession session) {
        if (session.getAttribute("usuarioId") == null) return "redirect:/login";
        return "chatbot";
    }

    @PostMapping("/chatbot/respuesta")
    @ResponseBody
    public ResponseEntity<Map<String, String>> responder(@RequestBody Map<String, String> body) {
        String mensaje = body.getOrDefault("mensaje", "");
        return ResponseEntity.ok(Map.of("respuesta", chatbotService.responder(mensaje)));
    }
}
