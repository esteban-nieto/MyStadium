package com.MyStadium.MapaDeCalor.infrastructure.adapter.output.jpa.mapper;

import com.MyStadium.MapaDeCalor.domain.entity.Boleto;
import com.MyStadium.MapaDeCalor.infrastructure.adapter.output.jpa.BoletoData;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class BoletoMapperTest {
    private final BoletoMapper mapper = new BoletoMapper();

    @Test
    void toModel_MapeaTodosLosCampos() {
        BoletoData data = BoletoData.builder().id("1").codigoUnico("MST-001")
                .usuarioId("u1").conciertoId("c1").conciertoNombre("C").artista("A")
                .zonaNombre("VIP").zonaPrecio(250000.0).asiento("A1")
                .totalPagado(250000.0).fechaCompra(LocalDateTime.of(2026,6,1,12,0)).build();
        Boleto model = mapper.toModel(data);
        assertEquals("1", model.getId());
        assertEquals("MST-001", model.getCodigoUnico());
        assertEquals("u1", model.getUsuarioId());
        assertEquals("c1", model.getConciertoId());
        assertEquals("C", model.getConciertoNombre());
        assertEquals("A", model.getArtista());
        assertEquals("VIP", model.getZonaNombre());
        assertEquals(250000.0, model.getZonaPrecio());
        assertEquals("A1", model.getAsiento());
        assertEquals(250000.0, model.getTotalPagado());
        assertEquals(LocalDateTime.of(2026,6,1,12,0), model.getFechaCompra());
    }

    @Test
    void toModel_Null() {
        assertNull(mapper.toModel(null));
    }

    @Test
    void toData_MapeaTodosLosCampos() {
        Boleto model = Boleto.builder().id("1").codigoUnico("MST-001")
                .usuarioId("u1").conciertoId("c1").conciertoNombre("C").artista("A")
                .zonaNombre("VIP").zonaPrecio(250000.0).asiento("A1")
                .totalPagado(250000.0).fechaCompra(LocalDateTime.of(2026,6,1,12,0)).build();
        BoletoData data = mapper.toData(model);
        assertEquals("1", data.getId());
        assertEquals("MST-001", data.getCodigoUnico());
        assertEquals("VIP", data.getZonaNombre());
    }

    @Test
    void toData_Null() {
        assertNull(mapper.toData(null));
    }
}
