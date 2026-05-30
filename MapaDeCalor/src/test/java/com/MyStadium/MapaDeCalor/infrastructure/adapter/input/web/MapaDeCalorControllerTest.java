package com.MyStadium.MapaDeCalor.infrastructure.adapter.input.web;

import com.MyStadium.MapaDeCalor.domain.entity.Boleto;
import com.MyStadium.MapaDeCalor.domain.entity.Zona;
import com.MyStadium.MapaDeCalor.domain.usecase.MapaDeCalorUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MapaDeCalorController.class)
class MapaDeCalorControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockBean private MapaDeCalorUseCase mapaDeCalorUseCase;

    @Test
    void obtenerZonas_Exito() throws Exception {
        when(mapaDeCalorUseCase.obtenerZonas("c1"))
                .thenReturn(List.of(Zona.builder().id("z1").nombre("vip").build()));
        mockMvc.perform(get("/api/mapa-calor/c1/zonas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("vip"));
    }

    @Test
    void comprarBoletos_Exito() throws Exception {
        when(mapaDeCalorUseCase.comprarBoletos(anyString(), anyString(), anyString(),
                anyString(), anyString(), anyInt(), nullable(String.class), any(LocalDateTime.class), nullable(String.class)))
                .thenReturn(Map.of("boletos", List.of(), "total", 500000.0));
        mockMvc.perform(post("/api/mapa-calor/comprar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuarioId\":\"u1\",\"conciertoId\":\"c1\"," +
                                "\"conciertoNombre\":\"C\",\"artista\":\"A\",\"zonaId\":\"z1\",\"cantidad\":2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(500000.0));
    }

    @Test
    void comprarBoletos_Falla() throws Exception {
        when(mapaDeCalorUseCase.comprarBoletos(anyString(), anyString(), anyString(),
                anyString(), anyString(), anyInt(), nullable(String.class), any(LocalDateTime.class), nullable(String.class)))
                .thenThrow(new IllegalArgumentException("Zona agotada"));
        mockMvc.perform(post("/api/mapa-calor/comprar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuarioId\":\"u1\",\"conciertoId\":\"c1\"," +
                                "\"conciertoNombre\":\"C\",\"artista\":\"A\",\"zonaId\":\"z1\",\"cantidad\":2}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Zona agotada"));
    }

    @Test
    void obtenerBoletosPorUsuario_Exito() throws Exception {
        when(mapaDeCalorUseCase.obtenerBoletosPorUsuario("u1"))
                .thenReturn(List.of(Boleto.builder().codigoUnico("MST-001").build()));
        mockMvc.perform(get("/api/mapa-calor/boletos/u1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigoUnico").value("MST-001"));
    }

    @Test
    void obtenerBoletoPorCodigo_Encontrado() throws Exception {
        when(mapaDeCalorUseCase.obtenerBoletoPorCodigo("MST-001"))
                .thenReturn(Optional.of(Boleto.builder().codigoUnico("MST-001")
                        .fechaCompra(LocalDateTime.now()).build()));
        mockMvc.perform(get("/api/mapa-calor/boleto/MST-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigoUnico").value("MST-001"));
    }

    @Test
    void obtenerBoletoPorCodigo_NoEncontrado() throws Exception {
        when(mapaDeCalorUseCase.obtenerBoletoPorCodigo("MST-999"))
                .thenReturn(Optional.empty());
        mockMvc.perform(get("/api/mapa-calor/boleto/MST-999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void comprarBoletos_CantidadNoNumerica() throws Exception {
        mockMvc.perform(post("/api/mapa-calor/comprar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuarioId\":\"u1\",\"conciertoId\":\"c1\"," +
                                "\"conciertoNombre\":\"C\",\"artista\":\"A\",\"zonaId\":\"z1\",\"cantidad\":\"abc\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void comprarBoletos_FechaEventoInvalida() throws Exception {
        when(mapaDeCalorUseCase.comprarBoletos(anyString(), anyString(), anyString(),
                anyString(), anyString(), anyInt(), nullable(String.class), any(LocalDateTime.class), nullable(String.class)))
                .thenThrow(new IllegalArgumentException("Formato de fecha inválido"));
        mockMvc.perform(post("/api/mapa-calor/comprar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuarioId\":\"u1\",\"conciertoId\":\"c1\"," +
                                "\"conciertoNombre\":\"C\",\"artista\":\"A\",\"zonaId\":\"z1\"," +
                                "\"cantidad\":2,\"fechaEvento\":\"fecha-invalida\"}"))
                .andExpect(status().isBadRequest());
    }
}
