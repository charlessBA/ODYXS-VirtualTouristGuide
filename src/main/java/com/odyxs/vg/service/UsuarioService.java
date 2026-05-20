package com.odyxs.vg.service;

import com.odyxs.vg.model.Usuario;

public interface UsuarioService {
    String registrar(String nombre, String correo, String contrasena,
                     String country, String fechaNacimiento);
    Usuario login(String correo, String contrasena);
    void inicializarAdmin();
}
