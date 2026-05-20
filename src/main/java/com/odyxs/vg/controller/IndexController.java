package com.odyxs.vg.Controller;

import com.odyxs.vg.Repository.EventoRepository;
import com.odyxs.vg.Services.CategoriaService;
import com.odyxs.vg.Services.LugarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;

@Controller
public class IndexController {

    @Autowired private LugarService     lugarService;
    @Autowired private CategoriaService categoriaService;
    @Autowired private EventoRepository eventoRepo;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("lugares",    lugarService.obtenerAprobados());
        model.addAttribute("categorias", categoriaService.obtenerTodas());
        model.addAttribute("eventos",
            eventoRepo.findByActivoTrueAndFechaGreaterThanEqualOrderByFechaAsc(LocalDate.now()));
        return "index";
    }
}
