package com.MyStadium.Notifications.pdf.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class TicketEntryTest {

    @Test
    void builderAndGetters() {
        TicketEntry e = TicketEntry.builder()
                .zona("VIP").asiento("A1").codigoUnico("MST-001")
                .qrUrl("https://mystadium.co/ticket/MST-001")
                .precio(BigDecimal.valueOf(250000)).tipoEntrada("VIP")
                .index(0).total(1).build();
        assertEquals("VIP", e.getZona());
        assertEquals("A1", e.getAsiento());
        assertEquals("MST-001", e.getCodigoUnico());
        assertEquals(250000, e.getPrecio().intValue());
        assertEquals(0, e.getIndex());
        assertEquals(1, e.getTotal());
    }

    @Test
    void builderDefaultsToZero() {
        TicketEntry e = TicketEntry.builder().build();
        assertNull(e.getZona());
        assertEquals(0, e.getIndex());
        assertEquals(0, e.getTotal());
    }

    @Test
    void setters() {
        TicketEntry e = TicketEntry.builder().build();
        e.setZona("GENERAL");
        e.setAsiento("F5");
        assertEquals("GENERAL", e.getZona());
        assertEquals("F5", e.getAsiento());
    }

    @Test
    void equalsAndHashCode() {
        TicketEntry a = TicketEntry.builder().zona("VIP").asiento("A1")
                .codigoUnico("MST-001").qrUrl("url").index(0).total(1).build();
        TicketEntry b = TicketEntry.builder().zona("VIP").asiento("A1")
                .codigoUnico("MST-001").qrUrl("url").index(0).total(1).build();
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
