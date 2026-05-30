package com.MyStadium.Auth.infrastructure.adapter.input.web;

import com.MyStadium.Auth.domain.entity.Usuario;
import com.MyStadium.Auth.domain.usecase.UsuarioUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.dao.DataIntegrityViolationException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockBean private UsuarioUseCase usuarioUseCase;

    @Test
    void registrar_Exito() throws Exception {
        Usuario usuario = Usuario.builder().id("1").correo("test@test.com").build();
        when(usuarioUseCase.guardarUsuario(any(Usuario.class))).thenReturn(usuario);
        mockMvc.perform(post("/api/auth/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"test@test.com\",\"contraseña\":\"1234\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correo").value("test@test.com"));
    }

    @Test
    void registrar_Falla() throws Exception {
        when(usuarioUseCase.guardarUsuario(any(Usuario.class)))
                .thenThrow(new NullPointerException("El correo y la contraseña son obligatorios"));
        mockMvc.perform(post("/api/auth/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"test@test.com\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registrar_FallaDataIntegrityViolation() throws Exception {
        when(usuarioUseCase.guardarUsuario(any(Usuario.class)))
                .thenThrow(new DataIntegrityViolationException(""));
        mockMvc.perform(post("/api/auth/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"test@test.com\",\"contraseña\":\"1234\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El usuario ya existe"));
    }

    @Test
    void iniciarSesion_Exito() throws Exception {
        when(usuarioUseCase.iniciarSesion("test@test.com", "1234")).thenReturn("Login exitoso");
        mockMvc.perform(post("/api/auth/iniciar-sesion")
                        .param("correo", "test@test.com")
                        .param("contraseña", "1234"))
                .andExpect(status().isOk())
                .andExpect(content().string("Login exitoso"));
    }

    @Test
    void iniciarSesion_Falla() throws Exception {
        when(usuarioUseCase.iniciarSesion("test@test.com", "wrong"))
                .thenThrow(new RuntimeException("Contraseña incorrecta"));
        mockMvc.perform(post("/api/auth/iniciar-sesion")
                        .param("correo", "test@test.com")
                        .param("contraseña", "wrong"))
                .andExpect(status().isBadRequest());
    }
}
