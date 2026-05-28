package com.MyStadium.Catalogo.domain.usecase;

import com.MyStadium.Catalogo.domain.entity.Concierto;
import com.MyStadium.Catalogo.domain.gateway.ConciertoGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConciertoUseCaseTest {
    @Mock private ConciertoGateway conciertoGateway;
    @InjectMocks private ConciertoUseCase conciertoUseCase;
    private Concierto concierto;

    @BeforeEach
    void setUp() {
        concierto = Concierto.builder().id("1").nombre("Tour 2026").artista("Banda Ejemplo")
                .estadio("Estadio El Campin").fecha(LocalDate.now()).precioBase(150000.0).build();
    }

    @Test void guardarConcierto_Exito() {
        when(conciertoGateway.guardar(any(Concierto.class))).thenReturn(concierto);
        Concierto result = conciertoUseCase.guardarConcierto(concierto);
        assertNotNull(result);
        assertEquals("Tour 2026", result.getNombre());
    }

    @Test void guardarConcierto_FallaPorNombreNulo() {
        concierto.setNombre(null);
        assertThrows(IllegalArgumentException.class, () -> conciertoUseCase.guardarConcierto(concierto));
    }

    @Test void obtenerConciertoPorId_Exito() {
        when(conciertoGateway.buscarPorId("1")).thenReturn(concierto);
        Concierto result = conciertoUseCase.obtenerConciertoPorId("1");
        assertNotNull(result);
        assertEquals("Banda Ejemplo", result.getArtista());
    }

    @Test void obtenerTodosLosConciertos_Exito() {
        when(conciertoGateway.buscarTodos()).thenReturn(List.of(concierto));
        List<Concierto> results = conciertoUseCase.obtenerTodosLosConciertos();
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
    }

    @Test void eliminarConcierto_Exito() {
        doNothing().when(conciertoGateway).eliminarPorId("1");
        assertTrue(conciertoUseCase.eliminarConcierto("1"));
    }

    @Test void eliminarConcierto_Falla() {
        doThrow(new RuntimeException("Error")).when(conciertoGateway).eliminarPorId("1");
        assertFalse(conciertoUseCase.eliminarConcierto("1"));
    }
}
