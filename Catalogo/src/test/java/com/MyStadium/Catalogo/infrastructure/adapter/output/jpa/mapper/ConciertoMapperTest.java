package com.MyStadium.Catalogo.infrastructure.adapter.output.jpa.mapper;

import com.MyStadium.Catalogo.domain.entity.Concierto;
import com.MyStadium.Catalogo.infrastructure.adapter.output.jpa.ConciertoData;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ConciertoMapperTest {
    private final ConciertoMapper mapper = new ConciertoMapper();

    @Test
    void toModel_MapeaTodosLosCampos() {
        ConciertoData data = ConciertoData.builder().id("1").nombre("Tour").artista("Artista")
                .estadio("Campin").fecha(LocalDate.of(2026,6,1))
                .precioBase(50000.0).imagenUrl("img.jpg").build();
        Concierto model = mapper.toModel(data);
        assertEquals("1", model.getId());
        assertEquals("Tour", model.getNombre());
        assertEquals("Artista", model.getArtista());
        assertEquals("Campin", model.getEstadio());
        assertEquals(LocalDate.of(2026,6,1), model.getFecha());
        assertEquals(50000.0, model.getPrecioBase());
        assertEquals("img.jpg", model.getImagenUrl());
    }

    @Test
    void toModel_Null() {
        assertNull(mapper.toModel(null));
    }

    @Test
    void toData_MapeaTodosLosCampos() {
        Concierto model = Concierto.builder().id("1").nombre("Tour").artista("Artista")
                .estadio("Campin").fecha(LocalDate.of(2026,6,1))
                .precioBase(50000.0).imagenUrl("img.jpg").build();
        ConciertoData data = mapper.toData(model);
        assertEquals("1", data.getId());
        assertEquals("Tour", data.getNombre());
        assertEquals("Artista", data.getArtista());
        assertEquals("Campin", data.getEstadio());
        assertEquals(LocalDate.of(2026,6,1), data.getFecha());
        assertEquals(50000.0, data.getPrecioBase());
        assertEquals("img.jpg", data.getImagenUrl());
    }

    @Test
    void toData_Null() {
        assertNull(mapper.toData(null));
    }

    @Test
    void toModel_CamposNulos() {
        Concierto model = mapper.toModel(new ConciertoData());
        assertNull(model.getId());
        assertNull(model.getNombre());
    }
}
