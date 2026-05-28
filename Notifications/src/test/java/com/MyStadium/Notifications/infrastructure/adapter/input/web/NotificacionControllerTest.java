package com.MyStadium.Notifications.infrastructure.adapter.input.web;

import com.MyStadium.Notifications.domain.entity.Notificacion;
import com.MyStadium.Notifications.domain.usecase.NotificacionUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Map;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificacionController.class)
class NotificacionControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockBean private NotificacionUseCase notificacionUseCase;

    @Test
    void enviarVerificacion_Exito() throws Exception {
        when(notificacionUseCase.enviarEmailVerificacion("test@test.com", "u1"))
                .thenReturn(Notificacion.builder().estado("SENT").build());
        mockMvc.perform(post("/api/notificaciones/enviar-verificacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@test.com\",\"usuarioId\":\"u1\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void enviarVerificacion_Falla() throws Exception {
        when(notificacionUseCase.enviarEmailVerificacion("test@test.com", "u1"))
                .thenThrow(new IllegalArgumentException("Email requerido"));
        mockMvc.perform(post("/api/notificaciones/enviar-verificacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@test.com\",\"usuarioId\":\"u1\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void enviarRecuperacion_Exito() throws Exception {
        when(notificacionUseCase.enviarEmailRecuperacion("test@test.com", "link"))
                .thenReturn(Notificacion.builder().estado("SENT").build());
        mockMvc.perform(post("/api/notificaciones/enviar-recuperacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@test.com\",\"enlaceRecuperacion\":\"link\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void enviarReciboCompra_Exito() throws Exception {
        when(notificacionUseCase.enviarReciboCompra(any()))
                .thenAnswer(i -> i.getArgument(0));
        mockMvc.perform(post("/api/notificaciones/recibo-compra")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"emailUsuario\":\"test@test.com\",\"codigoOrden\":\"ORD-001\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void enviarReciboCompraConPdf_Exito() throws Exception {
        when(notificacionUseCase.enviarReciboConPdf(anyList(), anyString(), anyString()))
                .thenReturn(Map.of("mensaje", "Recibo enviado"));
        mockMvc.perform(post("/api/notificaciones/enviar-recibo-compra")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigos\":[\"MST-001\"],\"usuarioId\":\"u1\",\"email\":\"test@test.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Recibo enviado"));
    }

    @Test
    void enviarReciboCompraConPdf_FaltaParametros() throws Exception {
        mockMvc.perform(post("/api/notificaciones/enviar-recibo-compra")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuarioId\":\"u1\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void descargarPdf_Encontrado() throws Exception {
        when(notificacionUseCase.obtenerPdf("MST-001")).thenReturn(new byte[]{25,80,68,70});
        mockMvc.perform(get("/api/notificaciones/pdf/MST-001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }

    @Test
    void descargarPdf_NoEncontrado() throws Exception {
        when(notificacionUseCase.obtenerPdf("NOEXISTE")).thenReturn(null);
        mockMvc.perform(get("/api/notificaciones/pdf/NOEXISTE"))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerHistorial_Exito() throws Exception {
        when(notificacionUseCase.obtenerHistorialNotificaciones("u1"))
                .thenReturn(List.of(Notificacion.builder().usuarioId("u1").build()));
        mockMvc.perform(get("/api/notificaciones/u1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuarioId").value("u1"));
    }
}
