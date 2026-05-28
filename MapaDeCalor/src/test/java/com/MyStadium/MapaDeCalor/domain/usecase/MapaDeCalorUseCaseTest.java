package com.MyStadium.MapaDeCalor.domain.usecase;

import com.MyStadium.MapaDeCalor.domain.entity.Boleto;
import com.MyStadium.MapaDeCalor.domain.entity.Zona;
import com.MyStadium.MapaDeCalor.domain.gateway.BoletoGateway;
import com.MyStadium.MapaDeCalor.domain.gateway.ZonaGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MapaDeCalorUseCaseTest {
    @Mock private ZonaGateway zonaGateway;
    @Mock private BoletoGateway boletoGateway;
    @InjectMocks private MapaDeCalorUseCase useCase;
    private Zona zona;
    private final LocalDateTime fechaEvento = LocalDateTime.now().plusDays(30);

    @BeforeEach
    void setUp() {
        zona = Zona.builder().id("z1").conciertoId("c1").nombre("vip")
                .capacidad(100).ocupados(30).precio(250000.0).build();
    }

    @Test
    void obtenerZonas_DesdeDb() {
        when(zonaGateway.buscarPorConciertoId("c1")).thenReturn(List.of(zona));
        List<Zona> zonas = useCase.obtenerZonas("c1");
        assertFalse(zonas.isEmpty());
        assertEquals(1, zonas.size());
        verify(zonaGateway, never()).guardar(any());
    }

    @Test
    void obtenerZonas_CreaPorDefecto() {
        when(zonaGateway.buscarPorConciertoId("c1")).thenReturn(List.of());
        when(zonaGateway.guardar(any(Zona.class))).thenAnswer(i -> i.getArgument(0));
        List<Zona> zonas = useCase.obtenerZonas("c1");
        assertEquals(12, zonas.size());
        verify(zonaGateway, times(12)).guardar(any(Zona.class));
    }

    @Test
    void obtenerZonas_CreaPorDefectoPatron2() {
        when(zonaGateway.buscarPorConciertoId("c2")).thenReturn(List.of());
        when(zonaGateway.guardar(any(Zona.class))).thenAnswer(i -> i.getArgument(0));
        List<Zona> zonas = useCase.obtenerZonas("c2");
        assertEquals(12, zonas.size());
    }

    @Test
    void obtenerZonas_CreaPorDefectoPatron3() {
        when(zonaGateway.buscarPorConciertoId("c3")).thenReturn(List.of());
        when(zonaGateway.guardar(any(Zona.class))).thenAnswer(i -> i.getArgument(0));
        List<Zona> zonas = useCase.obtenerZonas("c3");
        assertEquals(12, zonas.size());
    }

    @Test
    void comprarBoletos_Exito() {
        when(zonaGateway.buscarPorId("z1")).thenReturn(Optional.of(zona));
        when(boletoGateway.guardar(any(Boleto.class))).thenAnswer(i -> i.getArgument(0));
        Map<String, Object> resultado = useCase.comprarBoletos("u1", "c1", "Concierto", "Artista", "z1", 3,
                "Estadio Test", fechaEvento, "VIP");
        assertEquals(3, ((List<?>) resultado.get("boletos")).size());
        assertEquals(750000.0, (Double) resultado.get("total"));
        verify(zonaGateway).actualizarOcupados("z1", 33);
    }

    @Test
    void comprarBoletos_Agotado() {
        zona.setOcupados(100);
        when(zonaGateway.buscarPorId("z1")).thenReturn(Optional.of(zona));
        assertThrows(IllegalArgumentException.class, () ->
                useCase.comprarBoletos("u1", "c1", "C", "A", "z1", 1,
                        "Estadio Test", fechaEvento, "VIP"));
    }

    @Test
    void comprarBoletos_SinCapacidad() {
        zona.setOcupados(98);
        when(zonaGateway.buscarPorId("z1")).thenReturn(Optional.of(zona));
        assertThrows(IllegalArgumentException.class, () ->
                useCase.comprarBoletos("u1", "c1", "C", "A", "z1", 5,
                        "Estadio Test", fechaEvento, "VIP"));
    }

    @Test
    void comprarBoletos_ZonaNoEncontrada() {
        when(zonaGateway.buscarPorId("z99")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () ->
                useCase.comprarBoletos("u1", "c1", "C", "A", "z99", 1,
                        "Estadio Test", fechaEvento, "VIP"));
    }

    @Test
    void comprarBoletos_GeneraCodigosUnicos() {
        when(zonaGateway.buscarPorId("z1")).thenReturn(Optional.of(zona));
        when(boletoGateway.guardar(any(Boleto.class))).thenAnswer(i -> i.getArgument(0));
        Map<String, Object> resultado = useCase.comprarBoletos("u1", "c1", "C", "A", "z1", 2,
                "Estadio Test", fechaEvento, "VIP");
        List<Boleto> boletos = (List<Boleto>) resultado.get("boletos");
        assertTrue(boletos.get(0).getCodigoUnico().startsWith("MST-"));
        assertTrue(boletos.get(1).getCodigoUnico().startsWith("MST-"));
        assertNotEquals(boletos.get(0).getCodigoUnico(), boletos.get(1).getCodigoUnico());
    }

    @Test
    void obtenerBoletosPorUsuario_Exito() {
        Boleto b = Boleto.builder().codigoUnico("MST-001").build();
        when(boletoGateway.buscarPorUsuarioId("u1")).thenReturn(List.of(b));
        List<Boleto> boletos = useCase.obtenerBoletosPorUsuario("u1");
        assertEquals(1, boletos.size());
    }

    @Test
    void obtenerBoletosPorUsuario_Vacio() {
        when(boletoGateway.buscarPorUsuarioId("u1")).thenReturn(List.of());
        assertTrue(useCase.obtenerBoletosPorUsuario("u1").isEmpty());
    }

    @Test
    void obtenerBoletoPorCodigo_Encontrado() {
        Boleto b = Boleto.builder().codigoUnico("MST-001").build();
        when(boletoGateway.buscarPorCodigo("MST-001")).thenReturn(Optional.of(b));
        Optional<Boleto> resultado = useCase.obtenerBoletoPorCodigo("MST-001");
        assertTrue(resultado.isPresent());
    }

    @Test
    void obtenerBoletoPorCodigo_NoEncontrado() {
        when(boletoGateway.buscarPorCodigo("MST-999")).thenReturn(Optional.empty());
        assertFalse(useCase.obtenerBoletoPorCodigo("MST-999").isPresent());
    }
}
