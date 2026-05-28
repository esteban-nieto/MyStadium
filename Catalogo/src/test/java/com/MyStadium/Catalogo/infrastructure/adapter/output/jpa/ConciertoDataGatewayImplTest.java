package com.MyStadium.Catalogo.infrastructure.adapter.output.jpa;

import com.MyStadium.Catalogo.domain.entity.Concierto;
import com.MyStadium.Catalogo.infrastructure.adapter.output.jpa.mapper.ConciertoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConciertoDataGatewayImplTest {
    @Mock private ConciertoDataJpaRepository repository;
    @Mock private ConciertoMapper mapper;
    @InjectMocks private ConciertoDataGatewayImpl gateway;
    private Concierto concierto;
    private ConciertoData data;

    @BeforeEach
    void setUp() {
        concierto = Concierto.builder().id("1").nombre("Tour").artista("Artista").build();
        data = ConciertoData.builder().id("1").nombre("Tour").artista("Artista").build();
    }

    @Test
    void guardar_DelegaEnRepository() {
        when(mapper.toData(concierto)).thenReturn(data);
        when(repository.save(data)).thenReturn(data);
        when(mapper.toModel(data)).thenReturn(concierto);
        Concierto result = gateway.guardar(concierto);
        assertEquals("Tour", result.getNombre());
        verify(repository).save(data);
    }

    @Test
    void buscarPorId_Encontrado() {
        when(repository.findById("1")).thenReturn(Optional.of(data));
        when(mapper.toModel(data)).thenReturn(concierto);
        Concierto result = gateway.buscarPorId("1");
        assertEquals("Tour", result.getNombre());
    }

    @Test
    void buscarPorId_NoEncontrado() {
        when(repository.findById("99")).thenReturn(Optional.empty());
        assertNull(gateway.buscarPorId("99"));
    }

    @Test
    void buscarTodos_ConDatos() {
        when(repository.findAll()).thenReturn(List.of(data));
        when(mapper.toModel(data)).thenReturn(concierto);
        List<Concierto> results = gateway.buscarTodos();
        assertEquals(1, results.size());
    }

    @Test
    void buscarTodos_Vacio() {
        when(repository.findAll()).thenReturn(List.of());
        assertTrue(gateway.buscarTodos().isEmpty());
    }

    @Test
    void eliminarPorId_DelegaEnRepository() {
        doNothing().when(repository).deleteById("1");
        gateway.eliminarPorId("1");
        verify(repository).deleteById("1");
    }
}
