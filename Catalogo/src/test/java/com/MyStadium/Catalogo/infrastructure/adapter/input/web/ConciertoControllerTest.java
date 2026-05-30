package com.MyStadium.Catalogo.infrastructure.adapter.input.web;

import com.MyStadium.Catalogo.domain.entity.Concierto;
import com.MyStadium.Catalogo.domain.usecase.ConciertoUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConciertoController.class)
class ConciertoControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockBean private ConciertoUseCase conciertoUseCase;

    @Test
    void crearConcierto_Exito() throws Exception {
        Concierto c = Concierto.builder().id("1").nombre("Tour").artista("Artista").build();
        when(conciertoUseCase.guardarConcierto(any(Concierto.class))).thenReturn(c);
        mockMvc.perform(post("/api/conciertos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Tour\",\"artista\":\"Artista\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Tour"));
    }

    @Test
    void crearConcierto_Falla() throws Exception {
        when(conciertoUseCase.guardarConcierto(any(Concierto.class)))
                .thenThrow(new IllegalArgumentException("Nombre y Artista son obligatorios"));
        mockMvc.perform(post("/api/conciertos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Tour\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void obtenerConcierto_Encontrado() throws Exception {
        Concierto c = Concierto.builder().id("1").nombre("Tour").artista("Artista").build();
        when(conciertoUseCase.obtenerConciertoPorId("1")).thenReturn(c);
        mockMvc.perform(get("/api/conciertos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.artista").value("Artista"));
    }

    @Test
    void obtenerConcierto_NoEncontrado() throws Exception {
        when(conciertoUseCase.obtenerConciertoPorId("99")).thenReturn(null);
        mockMvc.perform(get("/api/conciertos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerTodos_Exito() throws Exception {
        when(conciertoUseCase.obtenerTodosLosConciertos())
                .thenReturn(List.of(Concierto.builder().id("1").nombre("Tour").build()));
        mockMvc.perform(get("/api/conciertos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Tour"));
    }

    @Test
    void eliminarConcierto_Exito() throws Exception {
        when(conciertoUseCase.eliminarConcierto("1")).thenReturn(true);
        mockMvc.perform(delete("/api/conciertos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminarConcierto_Falla() throws Exception {
        when(conciertoUseCase.eliminarConcierto("1")).thenReturn(false);
        mockMvc.perform(delete("/api/conciertos/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void obtenerImagenConcierto_NoEncontrado() throws Exception {
        when(conciertoUseCase.obtenerConciertoPorId("99")).thenReturn(null);
        mockMvc.perform(get("/api/conciertos/99/imagen"))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerImagenConcierto_ConImagenUrl_ArchivoNoExiste() throws Exception {
        Concierto c = Concierto.builder().id("1").imagenUrl("noexiste.jpg").artista("Artista").build();
        when(conciertoUseCase.obtenerConciertoPorId("1")).thenReturn(c);
        mockMvc.perform(get("/api/conciertos/1/imagen"))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerImagenConcierto_ImagenUrlNulaYArtistaNulo() throws Exception {
        Concierto c = Concierto.builder().id("1").imagenUrl(null).artista(null).build();
        when(conciertoUseCase.obtenerConciertoPorId("1")).thenReturn(c);
        mockMvc.perform(get("/api/conciertos/1/imagen"))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerTodos_Vacio() throws Exception {
        when(conciertoUseCase.obtenerTodosLosConciertos()).thenReturn(List.of());
        mockMvc.perform(get("/api/conciertos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
