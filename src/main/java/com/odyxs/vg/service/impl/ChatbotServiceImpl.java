package com.odyxs.vg.service.impl;

import com.odyxs.vg.repository.ChatbotRespuestaRepository;
import com.odyxs.vg.service.ChatbotService;
import org.springframework.stereotype.Service;

@Service
public class ChatbotServiceImpl implements ChatbotService {

    private final ChatbotRespuestaRepository chatbotRepository;

    public ChatbotServiceImpl(ChatbotRespuestaRepository chatbotRepository) {
        this.chatbotRepository = chatbotRepository;
    }

    @Override
    public String responder(String mensaje) {
        String clave = detectarClave(mensaje.toLowerCase().trim());
        return chatbotRepository.findByClave(clave)
            .map(r -> r.getRespuesta())
            .orElse("🤔 No estoy seguro de cómo responder a eso. Prueba preguntarme sobre:\n"
                  + "• clima • playas • transporte • hoteles\n"
                  + "• historia • actividades • eventos • gastronomía");
    }

    private String detectarClave(String msg) {
        if (msg.matches("hola|buenos días|buenas|hi|hey|saludos")) return "saludo";
        if (msg.contains("clima") || msg.contains("lluvi") || msg.contains("temperatur")
                || msg.contains("cuando visit")) return "clima";
        if (msg.contains("playa") || msg.contains("baru") || msg.contains("rosario")
                || msg.contains("bocagrande")) return "playas";
        if (msg.contains("transport") || msg.contains("mover") || msg.contains("llegar")
                || msg.contains("bus") || msg.contains("taxi") || msg.contains("yate")) return "transporte";
        if (msg.contains("hotel") || msg.contains("aloj") || msg.contains("hostal")
                || msg.contains("donde dormir")) return "hoteles";
        if (msg.contains("histor") || msg.contains("coloni") || msg.contains("murall")
                || msg.contains("castill") || msg.contains("fundaci")) return "historico";
        if (msg.contains("actividad") || msg.contains("hacer") || msg.contains("tour")
                || msg.contains("buceo") || msg.contains("kayak")) return "actividades";
        if (msg.contains("evento") || msg.contains("festival") || msg.contains("hay festival")
                || msg.contains("ficci")) return "eventos";
        if (msg.contains("costo") || msg.contains("precio") || msg.contains("caro")
                || msg.contains("barato") || msg.contains("dinero")) return "costo";
        if (msg.contains("comer") || msg.contains("comida") || msg.contains("gastronom")
                || msg.contains("restaur") || msg.contains("ceviche")) return "gastronomia";
        if (msg.contains("adios") || msg.contains("gracias") || msg.contains("bye")
                || msg.contains("chao")) return "despedida";
        return "saludo";
    }
}
