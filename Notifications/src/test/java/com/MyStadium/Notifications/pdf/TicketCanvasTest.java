package com.MyStadium.Notifications.pdf;

import com.MyStadium.Notifications.pdf.components.Spacing;
import com.MyStadium.Notifications.pdf.components.Typography;
import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import static org.junit.jupiter.api.Assertions.*;

class TicketCanvasTest {

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

    @Test
    void drawBackground_RunsWithoutError() {
        CanvasHelper h = new CanvasHelper();
        assertDoesNotThrow(() -> h.canvas.drawBackground());
        h.document.close();
    }

    @Test
    void drawSurfaceCard_RunsWithoutError() {
        CanvasHelper h = new CanvasHelper();
        assertDoesNotThrow(() -> h.canvas.drawSurfaceCard(10, 10, 100, 50));
        h.document.close();
    }

    @Test
    void drawText_RunsWithoutError() {
        CanvasHelper h = new CanvasHelper();
        assertDoesNotThrow(() -> h.canvas.drawText("Test", 10, 10, Typography.regular(10f)));
        h.document.close();
    }

    @Test
    void drawTextOpacity_RunsWithoutError() {
        CanvasHelper h = new CanvasHelper();
        assertDoesNotThrow(() -> h.canvas.drawTextOpacity("Test", 10, 10, Typography.regular(10f), 0.5f));
        h.document.close();
    }

    @Test
    void drawCircle_RunsWithoutError() {
        CanvasHelper h = new CanvasHelper();
        assertDoesNotThrow(() -> h.canvas.drawCircle(50, 50, 10, Color.RED));
        h.document.close();
    }

    @Test
    void drawLine_RunsWithoutError() {
        CanvasHelper h = new CanvasHelper();
        assertDoesNotThrow(() -> h.canvas.drawLine(10, 10, 100, 10, Color.WHITE, 1f));
        h.document.close();
    }

    @Test
    void clipRect_RunsWithoutError() {
        CanvasHelper h = new CanvasHelper();
        assertDoesNotThrow(() -> h.canvas.clipRect(10, 10, 100, 50));
        h.document.close();
    }

    @Test
    void drawDiagonalWatermark_RunsWithoutError() {
        CanvasHelper h = new CanvasHelper();
        assertDoesNotThrow(() -> h.canvas.drawDiagonalWatermark("MYSTADIUM", Color.GRAY, 0.03f));
        h.document.close();
    }

    @Test
    void getCb_ReturnsNonNull() {
        CanvasHelper h = new CanvasHelper();
        assertNotNull(h.canvas.getCb());
        h.canvas.drawLine(0, 0, 1, 1, Color.WHITE, 1f);
        h.document.close();
    }
}
