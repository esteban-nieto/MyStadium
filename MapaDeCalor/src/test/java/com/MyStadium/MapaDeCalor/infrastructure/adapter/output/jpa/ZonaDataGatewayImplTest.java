package com.MyStadium.MapaDeCalor.infrastructure.adapter.output.jpa;

import com.MyStadium.MapaDeCalor.domain.entity.Zona;
import com.MyStadium.MapaDeCalor.infrastructure.adapter.output.jpa.mapper.ZonaMapper;
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
class ZonaDataGatewayImplTest {
    @Mock private ZonaDataJpaRepository repository;
    @Mock private ZonaMapper mapper;
    @InjectMocks private ZonaDataGatewayImpl gateway;
    private Zona zona;
    private ZonaData data;

    @BeforeEach
    void setUp() {
        zona = Zona.builder().id("z1").conciertoId("c1").nombre("vip").capacidad(100).ocupados(30).precio(250000.0).build();
        data = ZonaData.builder().id("z1").conciertoId("c1").nombre("vip").capacidad(100).ocupados(30).precio(250000.0).build();
    }

    @Test
    void guardar_DelegaEnRepository() {
        when(mapper.toData(zona)).thenReturn(data);
        when(repository.save(data)).thenReturn(data);
        when(mapper.toModel(data)).thenReturn(zona);
        Zona result = gateway.guardar(zona);
        assertEquals("vip", result.getNombre());
        verify(repository).save(data);
    }

    @Test
    void buscarPorId_Encontrado() {
        when(repository.findById("z1")).thenReturn(Optional.of(data));
        when(mapper.toModel(data)).thenReturn(zona);
        Optional<Zona> result = gateway.buscarPorId("z1");
        assertTrue(result.isPresent());
    }

    @Test
    void buscarPorId_NoEncontrado() {
        when(repository.findById("z99")).thenReturn(Optional.empty());
        assertFalse(gateway.buscarPorId("z99").isPresent());
    }

    @Test
    void buscarPorConciertoId_ConDatos() {
        when(repository.findByConciertoId("c1")).thenReturn(List.of(data));
        when(mapper.toModel(data)).thenReturn(zona);
        List<Zona> results = gateway.buscarPorConciertoId("c1");
        assertEquals(1, results.size());
    }

    @Test
    void buscarPorConciertoId_Vacio() {
        when(repository.findByConciertoId("c99")).thenReturn(List.of());
        assertTrue(gateway.buscarPorConciertoId("c99").isEmpty());
    }

    @Test
    void actualizarOcupados_Exito() {
        when(repository.findById("z1")).thenReturn(Optional.of(data));
        when(repository.save(data)).thenReturn(data);
        gateway.actualizarOcupados("z1", 40);
        assertEquals(40, data.getOcupados());
        verify(repository).save(data);
    }

    @Test
    void actualizarOcupados_SinResultado() {
        when(repository.findById("z99")).thenReturn(Optional.empty());
        gateway.actualizarOcupados("z99", 50);
        verify(repository, never()).save(any());
    }
}
