package com.MyStadium.Notifications.pdf.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TicketPdfDataTest {

    @Test
    void builderAndGetters() {
        TicketPdfData data = TicketPdfData.builder()
                .eventName("Concierto").artist("Artista")
                .stadium("Estadio").fechaEvento(LocalDateTime.now())
                .orderCode("ORD-001").userName("user")
                .tickets(List.of()).totalPaid(250000)
                .build();
        assertEquals("Concierto", data.getEventName());
        assertEquals("Artista", data.getArtist());
        assertEquals("ORD-001", data.getOrderCode());
        assertEquals(250000, data.getTotalPaid());
    }

    @Test
    void setters() {
        TicketPdfData data = TicketPdfData.builder().build();
        data.setEventName("Test");
        data.setOrderCode("ORD-999");
        assertEquals("Test", data.getEventName());
        assertEquals("ORD-999", data.getOrderCode());
    }

    @Test
    void defaultsAreNull() {
        TicketPdfData data = TicketPdfData.builder().build();
        assertNull(data.getEventName());
    }

    @Test
    void toStringContainsEventName() {
        TicketPdfData data = TicketPdfData.builder().eventName("MiConcierto").build();
        assertTrue(data.toString().contains("MiConcierto"));
    }
}
