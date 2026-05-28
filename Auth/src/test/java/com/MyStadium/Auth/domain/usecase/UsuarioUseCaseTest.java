package com.MyStadium.Auth.domain.usecase;

import com.MyStadium.Auth.domain.entity.Usuario;
import com.MyStadium.Auth.domain.gateway.EncriptadorGateway;
import com.MyStadium.Auth.domain.gateway.UsuarioGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioUseCaseTest {
    @Mock private UsuarioGateway usuarioGateway;
    @Mock private EncriptadorGateway encriptadorGateway;
    @InjectMocks private UsuarioUseCase usuarioUseCase;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder().id("1").correo("test@test.com").contrasena("1234").build();
    }

    @Test void guardarUsuario_Exito() {
        when(encriptadorGateway.encriptar(anyString())).thenReturn("ENCRYPTED_1234");
        when(usuarioGateway.guardarUsuario(any(Usuario.class))).thenReturn(usuario);
        Usuario saved = usuarioUseCase.guardarUsuario(usuario);
        assertNotNull(saved);
        verify(encriptadorGateway, times(1)).encriptar(anyString());
        verify(usuarioGateway, times(1)).guardarUsuario(any(Usuario.class));
    }

    @Test void guardarUsuario_FallaPorCorreoNulo() {
        usuario.setCorreo(null);
        assertThrows(NullPointerException.class, () -> usuarioUseCase.guardarUsuario(usuario));
    }

    @Test void guardarUsuario_FallaPorContrasenaNula() {
        usuario.setContrasena(null);
        assertThrows(NullPointerException.class, () -> usuarioUseCase.guardarUsuario(usuario));
    }

    @Test void buscarUsuarioPorId_Exito() {
        when(usuarioGateway.buscarUsuarioPorId("1")).thenReturn(usuario);
        Usuario result = usuarioUseCase.buscarUsuarioPorId("1");
        assertEquals("test@test.com", result.getCorreo());
    }

    @Test void buscarUsuarioPorId_Excepcion() {
        when(usuarioGateway.buscarUsuarioPorId("1")).thenThrow(new RuntimeException("Error DB"));
        Usuario result = usuarioUseCase.buscarUsuarioPorId("1");
        assertNull(result);
    }

    @Test void eliminarUsuarioPorId_Exito() {
        doNothing().when(usuarioGateway).eliminarUsuarioPorId("1");
        assertTrue(usuarioUseCase.eliminarUsuarioPorId("1"));
    }

    @Test void eliminarUsuarioPorId_Excepcion() {
        doThrow(new RuntimeException("Error")).when(usuarioGateway).eliminarUsuarioPorId("1");
        assertFalse(usuarioUseCase.eliminarUsuarioPorId("1"));
    }

    @Test void iniciarSesion_Exito() {
        when(usuarioGateway.buscarPorCorreo("test@test.com")).thenReturn(usuario);
        when(encriptadorGateway.coinciden("1234", "1234")).thenReturn(true);
        assertEquals("Login exitoso", usuarioUseCase.iniciarSesion("test@test.com", "1234"));
    }

    @Test void iniciarSesion_FallaPorNulos() {
        assertThrows(RuntimeException.class, () -> usuarioUseCase.iniciarSesion(null, "1234"));
    }

    @Test void iniciarSesion_FallaPorCorreoInvalido() {
        assertThrows(RuntimeException.class, () -> usuarioUseCase.iniciarSesion("test", "1234"));
    }

    @Test void iniciarSesion_FallaUsuarioNoEncontrado() {
        when(usuarioGateway.buscarPorCorreo("test@test.com")).thenReturn(null);
        assertThrows(RuntimeException.class, () -> usuarioUseCase.iniciarSesion("test@test.com", "1234"));
    }

    @Test void iniciarSesion_FallaContrasenaIncorrecta() {
        when(usuarioGateway.buscarPorCorreo("test@test.com")).thenReturn(usuario);
        when(encriptadorGateway.coinciden("wrong", "1234")).thenReturn(false);
        assertThrows(RuntimeException.class, () -> usuarioUseCase.iniciarSesion("test@test.com", "wrong"));
    }
}
