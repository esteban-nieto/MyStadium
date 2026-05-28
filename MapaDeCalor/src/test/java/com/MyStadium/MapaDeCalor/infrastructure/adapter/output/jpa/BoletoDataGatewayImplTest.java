package com.MyStadium.MapaDeCalor.infrastructure.adapter.output.jpa;

import com.MyStadium.MapaDeCalor.domain.entity.Boleto;
import com.MyStadium.MapaDeCalor.infrastructure.adapter.output.jpa.mapper.BoletoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoletoDataGatewayImplTest {
    @Mock private BoletoDataJpaRepository repository;
    @Mock private BoletoMapper mapper;
    @InjectMocks private BoletoDataGatewayImpl gateway;
    private Boleto boleto;
    private BoletoData data;

    @BeforeEach
    void setUp() {
        boleto = Boleto.builder().codigoUnico("MST-001").usuarioId("u1").build();
        data = BoletoData.builder().codigoUnico("MST-001").usuarioId("u1").build();
    }

    @Test
    void guardar_DelegaEnRepository() {
        when(mapper.toData(boleto)).thenReturn(data);
        when(repository.save(data)).thenReturn(data);
        when(mapper.toModel(data)).thenReturn(boleto);
        Boleto result = gateway.guardar(boleto);
        assertEquals("MST-001", result.getCodigoUnico());
        verify(repository).save(data);
    }

    @Test
    void buscarPorCodigo_Encontrado() {
        when(repository.findByCodigoUnico("MST-001")).thenReturn(Optional.of(data));
        when(mapper.toModel(data)).thenReturn(boleto);
        Optional<Boleto> result = gateway.buscarPorCodigo("MST-001");
        assertTrue(result.isPresent());
    }

    @Test
    void buscarPorCodigo_NoEncontrado() {
        when(repository.findByCodigoUnico("MST-999")).thenReturn(Optional.empty());
        assertFalse(gateway.buscarPorCodigo("MST-999").isPresent());
    }

    @Test
    void buscarPorUsuarioId_ConDatos() {
        when(repository.findByUsuarioId("u1")).thenReturn(List.of(data));
        when(mapper.toModel(data)).thenReturn(boleto);
        List<Boleto> results = gateway.buscarPorUsuarioId("u1");
        assertEquals(1, results.size());
    }

    @Test
    void buscarPorUsuarioId_Vacio() {
        when(repository.findByUsuarioId("u99")).thenReturn(List.of());
        assertTrue(gateway.buscarPorUsuarioId("u99").isEmpty());
    }
}
