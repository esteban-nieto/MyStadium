package com.MyStadium.Notifications.pdf;

import com.MyStadium.Notifications.pdf.dto.TicketEntry;
import com.MyStadium.Notifications.pdf.dto.TicketPdfData;
import com.MyStadium.Notifications.pdf.renderers.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TicketPdfGeneratorTest {

    @BeforeAll
    static void initFonts() {
        com.MyStadium.Notifications.pdf.components.Typography.registerFonts();
    }

    private TicketPdfGenerator createGenerator() {
        return new TicketPdfGenerator(
                new BackgroundRenderer(),
                new BannerRenderer(),
                new HeaderRenderer(),
                new EventInfoRenderer(),
                new QrRenderer(),
                new BarcodeRenderer(),
                new FooterRenderer(),
                new AntiFraudRenderer()
        );
    }

    @Test
    void generate_SingleTicket_ReturnsPdf() {
        TicketPdfGenerator generator = createGenerator();
        TicketPdfData data = TicketPdfData.builder()
                .eventName("Concierto Test")
                .artist("Artista Test")
                .stadium("Estadio Test")
                .fechaEvento(LocalDateTime.now())
                .orderCode("ORD-001")
                .userName("user-123")
                .tickets(List.of(TicketEntry.builder()
                        .zona("VIP")
                        .asiento("A1")
                        .codigoUnico("MST-001")
                        .qrUrl("https://mystadium.co/ticket/MST-001")
                        .precio(BigDecimal.valueOf(250000))
                        .tipoEntrada("VIP")
                        .index(0)
                        .total(1)
                        .build()))
                .totalPaid(250000)
                .build();

        byte[] pdf = generator.generate(data);
        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
        assertEquals(0x25, pdf[0] & 0xFF);
        assertEquals(0x50, pdf[1] & 0xFF);
        assertEquals(0x44, pdf[2] & 0xFF);
        assertEquals(0x46, pdf[3] & 0xFF);
    }

    @Test
    void generate_MultipleTickets_ReturnsPdf() {
        TicketPdfGenerator generator = createGenerator();
        TicketPdfData data = TicketPdfData.builder()
                .eventName("Concierto Test")
                .artist("Artista Test")
                .fechaEvento(LocalDateTime.now())
                .orderCode("ORD-002")
                .userName("user-123")
                .tickets(List.of(
                        TicketEntry.builder().zona("VIP").asiento("A1")
                                .codigoUnico("MST-001").qrUrl("https://mystadium.co/ticket/MST-001")
                                .precio(BigDecimal.valueOf(250000)).tipoEntrada("VIP").index(0).total(2).build(),
                        TicketEntry.builder().zona("VIP").asiento("A2")
                                .codigoUnico("MST-002").qrUrl("https://mystadium.co/ticket/MST-002")
                                .precio(BigDecimal.valueOf(250000)).tipoEntrada("VIP").index(1).total(2).build()
                ))
                .totalPaid(500000)
                .build();

        byte[] pdf = generator.generate(data);
        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
        assertTrue(pdf[0] == 0x25 && pdf[1] == 0x50 && pdf[2] == 0x44 && pdf[3] == 0x46);
    }

    @Test
    void generate_WithBanner_ReturnsPdf() {
        TicketPdfGenerator generator = createGenerator();
        byte[] banner = new byte[100];
        java.util.Random rnd = new java.util.Random();
        rnd.nextBytes(banner);

        TicketPdfData data = TicketPdfData.builder()
                .eventName("Concierto")
                .artist("Artista")
                .fechaEvento(LocalDateTime.now())
                .orderCode("ORD-003")
                .userName("user-123")
                .eventBanner(banner)
                .tickets(List.of(TicketEntry.builder()
                        .zona("GENERAL").asiento("B1")
                        .codigoUnico("MST-003").qrUrl("https://mystadium.co/ticket/MST-003")
                        .precio(BigDecimal.valueOf(100000)).tipoEntrada("GENERAL").index(0).total(1).build()))
                .totalPaid(100000)
                .build();

        byte[] pdf = generator.generate(data);
        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
    }

    @Test
    void generate_SingleTicket_NoBanner_ReturnsPdf() {
        TicketPdfGenerator generator = createGenerator();
        TicketPdfData data = TicketPdfData.builder()
                .eventName("Test")
                .artist("Test")
                .fechaEvento(LocalDateTime.now())
                .orderCode("ORD-000")
                .userName("user")
                .tickets(List.of(TicketEntry.builder()
                        .zona("GENERAL").asiento("F1")
                        .codigoUnico("MST-000")
                        .qrUrl("https://mystadium.co/ticket/MST-000")
                        .precio(BigDecimal.valueOf(50000)).tipoEntrada("GENERAL").index(0).total(1).build()))
                .totalPaid(50000)
                .build();

        byte[] pdf = generator.generate(data);
        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
    }
}
