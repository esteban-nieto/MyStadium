package com.MyStadium.Auth.domain.usecase;

import com.MyStadium.Auth.domain.entity.Usuario;
import com.MyStadium.Auth.domain.gateway.UsuarioGateway;
import com.MyStadium.Auth.domain.gateway.EncriptadorGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsuarioUseCase {
    private final UsuarioGateway usuarioGateway;
    private final EncriptadorGateway encriptadorGateway;

    public Usuario guardarUsuario(Usuario usuario) {
        if (usuario.getCorreo() == null || usuario.getContrasena() == null)
            throw new NullPointerException("El correo y la contraseña son obligatorios");
        String contrasenaEncriptada = encriptadorGateway.encriptar(usuario.getContrasena());
        usuario.setContrasena(contrasenaEncriptada);
        return usuarioGateway.guardarUsuario(usuario);
    }

    public Usuario buscarUsuarioPorId(String id) {
        try { return usuarioGateway.buscarUsuarioPorId(id); }
        catch (Exception e) { return null; }
    }

    public boolean eliminarUsuarioPorId(String id) {
        try { usuarioGateway.eliminarUsuarioPorId(id); return true; }
        catch (Exception e) { return false; }
    }

    public String iniciarSesion(String correo, String contrasena) {
        if (correo == null || contrasena == null) throw new RuntimeException("Email y contraseña son obligatorios");
        if (!correo.contains("@")) throw new RuntimeException("Correo inválido");
        Usuario usuario = usuarioGateway.buscarPorCorreo(correo);
        if (usuario == null || usuario.getId() == null) throw new RuntimeException("Usuario no encontrado");
        if (!encriptadorGateway.coinciden(contrasena, usuario.getContrasena())) throw new RuntimeException("Contraseña incorrecta");
        return "Login exitoso";
    }
}
