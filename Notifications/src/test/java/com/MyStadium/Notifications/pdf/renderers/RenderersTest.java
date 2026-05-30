package com.MyStadium.Notifications.pdf.renderers;

import com.MyStadium.Notifications.pdf.TicketCanvas;
import com.MyStadium.Notifications.pdf.components.Spacing;
import com.MyStadium.Notifications.pdf.components.Typography;
import com.MyStadium.Notifications.pdf.dto.TicketEntry;
import com.MyStadium.Notifications.pdf.dto.TicketPdfData;
import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RenderersTest {
    private TicketCanvas canvas;
    private TicketPdfData data;
    private TicketEntry entry;

    @BeforeAll
    static void initFonts() {
        Typography.registerFonts();
    }

    static class CanvasHelper {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final Document document = new Document(new Rectangle(Spacing.PAGE_WIDTH, Spacing.PAGE_HEIGHT));
        final TicketCanvas canvas;

        CanvasHelper() {
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            canvas = new TicketCanvas(cb, Spacing.PAGE_WIDTH, Spacing.PAGE_HEIGHT);
        }
    }

    @BeforeEach
    void setUp() {
        CanvasHelper h = new CanvasHelper();
        canvas = h.canvas;
        data = TicketPdfData.builder()
                .eventName("Concierto Test")
                .artist("Artista Test")
                .stadium("Estadio Test")
                .fechaEvento(LocalDateTime.now())
                .orderCode("ORD-001")
                .userName("user-123")
                .tickets(List.of())
                .totalPaid(0)
                .build();

        entry = TicketEntry.builder()
                .zona("VIP").asiento("A1").codigoUnico("MST-001")
                .qrUrl("https://mystadium.co/ticket/MST-001")
                .precio(BigDecimal.valueOf(250000)).tipoEntrada("VIP")
                .index(0).total(1).build();
    }

    @Test
    void backgroundRenderer_RendersWithoutError() {
        BackgroundRenderer renderer = new BackgroundRenderer();
        assertDoesNotThrow(() -> renderer.render(canvas, data, 0, 1));
    }

    @Test
    void bannerRenderer_WithoutBanner_RendersWithoutError() {
        BannerRenderer renderer = new BannerRenderer();
        assertDoesNotThrow(() -> renderer.render(canvas, data));
    }

    @Test
    void bannerRenderer_WithBanner_RendersWithoutError() {
        BannerRenderer renderer = new BannerRenderer();
        data.setEventBanner(new byte[]{0x01, 0x02, 0x03});
        assertDoesNotThrow(() -> renderer.render(canvas, data));
    }

    @Test
    void headerRenderer_RendersWithoutError() {
        HeaderRenderer renderer = new HeaderRenderer();
        assertDoesNotThrow(() -> renderer.render(canvas, data));
    }

    @Test
    void eventInfoRenderer_WithStadium_RendersWithoutError() {
        EventInfoRenderer renderer = new EventInfoRenderer();
        assertDoesNotThrow(() -> renderer.render(canvas, data, entry));
    }

    @Test
    void eventInfoRenderer_WithoutStadium_RendersWithoutError() {
        EventInfoRenderer renderer = new EventInfoRenderer();
        data.setStadium(null);
        assertDoesNotThrow(() -> renderer.render(canvas, data, entry));
    }

    @Test
    void qrRenderer_RendersWithoutError() {
        QrRenderer renderer = new QrRenderer();
        assertDoesNotThrow(() -> renderer.render(canvas, data, entry));
    }

    @Test
    void barcodeRenderer_RendersWithoutError() {
        BarcodeRenderer renderer = new BarcodeRenderer();
        assertDoesNotThrow(() -> renderer.render(canvas, data, entry));
    }

    @Test
    void footerRenderer_RendersWithoutError() {
        FooterRenderer renderer = new FooterRenderer();
        assertDoesNotThrow(() -> renderer.render(canvas, data, entry));
    }

    @Test
    void antiFraudRenderer_RendersWithoutError() {
        AntiFraudRenderer renderer = new AntiFraudRenderer();
        assertDoesNotThrow(() -> renderer.render(canvas, data));
    }
}
