package com.odyxs.vg.service.impl;

import com.odyxs.vg.model.Usuario;
import com.odyxs.vg.repository.UsuarioRepository;
import com.odyxs.vg.service.UsuarioService;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public String registrar(String nombre, String correo, String contrasena,
                            String country, String fechaNacimiento) {
        if ("admin@odyxs.com".equalsIgnoreCase(correo)) return "Este correo está reservado.";
        if (usuarioRepository.existsByCorreo(correo)) return "El correo ya está registrado.";
        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setCorreo(correo);
        u.setContrasena(contrasena);
        u.setRol(Usuario.Rol.USUARIO);
        if (country != null && !country.isBlank()) u.setCountry(country);
        if (fechaNacimiento != null && !fechaNacimiento.isBlank()) u.setFechaNacimiento(fechaNacimiento);
        usuarioRepository.save(u);
        return "Registro exitoso.";
    }

    @Override
    public Usuario login(String correo, String contrasena) {
        return usuarioRepository.findByCorreo(correo)
            .filter(u -> u.getContrasena().equals(contrasena))
            .orElse(null);
    }

    @Override
    public void inicializarAdmin() {
        if (!usuarioRepository.existsByCorreo("admin@odyxs.com")) {
            Usuario a = new Usuario();
            a.setNombre("Administrador ODYXS");
            a.setCorreo("admin@odyxs.com");
            a.setContrasena("admin2026");
            a.setRol(Usuario.Rol.ADMIN);
            usuarioRepository.save(a);
        }
    }
}
