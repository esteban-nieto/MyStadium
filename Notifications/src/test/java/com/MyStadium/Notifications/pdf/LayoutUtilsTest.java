package com.MyStadium.Notifications.pdf;

import com.MyStadium.Notifications.pdf.components.LayoutUtils;
import com.MyStadium.Notifications.pdf.components.Colors;
import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import static org.junit.jupiter.api.Assertions.*;

class LayoutUtilsTest {

    static class PdfTestHelper {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final Document document = new Document(new Rectangle(400, 300));
        final PdfWriter writer;
        final PdfContentByte cb;

        PdfTestHelper() {
            writer = PdfWriter.getInstance(document, baos);
            document.open();
            cb = writer.getDirectContent();
        }
    }

    @Test
    void drawRoundedRect_RunsWithoutError() {
        PdfTestHelper h = new PdfTestHelper();
        assertDoesNotThrow(() -> LayoutUtils.drawRoundedRect(h.cb, 10, 10, 100, 50, 8));
        h.document.close();
    }

    @Test
    void fillRoundedRect_RunsWithoutError() {
        PdfTestHelper h = new PdfTestHelper();
        assertDoesNotThrow(() -> LayoutUtils.fillRoundedRect(h.cb, 10, 10, 100, 50, 8, Colors.SURFACE));
        h.document.close();
    }

    @Test
    void drawDashedLine_RunsWithoutError() {
        PdfTestHelper h = new PdfTestHelper();
        assertDoesNotThrow(() -> LayoutUtils.drawDashedLine(h.cb, 10, 10, 200, 10));
        h.document.close();
    }

    @Test
    void drawDottedLine_RunsWithoutError() {
        PdfTestHelper h = new PdfTestHelper();
        assertDoesNotThrow(() -> LayoutUtils.drawDottedLine(h.cb, 10, 20, 200, 20));
        h.document.close();
    }

    @Test
    void drawSoftShadow_RunsWithoutError() {
        PdfTestHelper h = new PdfTestHelper();
        assertDoesNotThrow(() -> LayoutUtils.drawSoftShadow(h.cb, 10, 10, 100, 50, 8));
        h.document.close();
    }
}
