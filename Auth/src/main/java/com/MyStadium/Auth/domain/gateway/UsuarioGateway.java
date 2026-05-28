package com.MyStadium.Auth.domain.gateway;

import com.MyStadium.Auth.domain.entity.Usuario;

public interface UsuarioGateway {
    Usuario guardarUsuario(Usuario usuario);
    Usuario buscarUsuarioPorId(String id);
    Usuario buscarPorCorreo(String correo);
    void eliminarUsuarioPorId(String id);
}
