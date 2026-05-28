package com.MyStadium.MapaDeCalor.infrastructure.adapter.output.jpa.mapper;

import com.MyStadium.MapaDeCalor.domain.entity.Zona;
import com.MyStadium.MapaDeCalor.infrastructure.adapter.output.jpa.ZonaData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ZonaMapperTest {
    private final ZonaMapper mapper = new ZonaMapper();

    @Test
    void toModel_MapeaTodosLosCampos() {
        ZonaData data = ZonaData.builder().id("z1").conciertoId("c1").nombre("vip")
                .capacidad(100).ocupados(30).precio(250000.0).build();
        Zona model = mapper.toModel(data);
        assertEquals("z1", model.getId());
        assertEquals("c1", model.getConciertoId());
        assertEquals("vip", model.getNombre());
        assertEquals(100, model.getCapacidad());
        assertEquals(30, model.getOcupados());
        assertEquals(250000.0, model.getPrecio());
    }

    @Test
    void toModel_Null() {
        assertNull(mapper.toModel(null));
    }

    @Test
    void toData_MapeaTodosLosCampos() {
        Zona model = Zona.builder().id("z1").conciertoId("c1").nombre("vip")
                .capacidad(100).ocupados(30).precio(250000.0).build();
        ZonaData data = mapper.toData(model);
        assertEquals("z1", data.getId());
        assertEquals("c1", data.getConciertoId());
        assertEquals("vip", data.getNombre());
        assertEquals(100, data.getCapacidad());
        assertEquals(30, data.getOcupados());
        assertEquals(250000.0, data.getPrecio());
    }

    @Test
    void toData_Null() {
        assertNull(mapper.toData(null));
    }

    @Test
    void toModel_CamposDefault() {
        Zona model = mapper.toModel(new ZonaData());
        assertEquals(0, model.getCapacidad());
        assertEquals(0.0, model.getPrecio());
    }
}
