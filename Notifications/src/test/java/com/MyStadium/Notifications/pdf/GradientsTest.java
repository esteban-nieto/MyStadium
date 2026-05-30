package com.MyStadium.Notifications.pdf;

import com.MyStadium.Notifications.pdf.components.Gradients;
import com.MyStadium.Notifications.pdf.components.Colors;
import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import static org.junit.jupiter.api.Assertions.*;

class GradientsTest {

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
    void applyVerticalGradient_RunsWithoutError() {
        PdfTestHelper h = new PdfTestHelper();
        assertDoesNotThrow(() ->
            Gradients.applyVerticalGradient(h.cb, 10, 10, 100, 50,
                    Colors.PRIMARY, Colors.SURFACE)
        );
        h.document.close();
    }

    @Test
    void applyVerticalAlphaGradient_RunsWithoutError() {
        PdfTestHelper h = new PdfTestHelper();
        assertDoesNotThrow(() ->
            Gradients.applyVerticalAlphaGradient(h.cb, 10, 10, 100, 50,
                    Colors.OVERLAY_TOP, 200, Colors.OVERLAY_BOTTOM, 50)
        );
        h.document.close();
    }

    @Test
    void applyCinematicOverlay_RunsWithoutError() {
        PdfTestHelper h = new PdfTestHelper();
        assertDoesNotThrow(() ->
            Gradients.applyCinematicOverlay(h.cb, 10, 10, 100, 50)
        );
        h.document.close();
    }

    @Test
    void applyVerticalGradient_ZeroHeight_RunsWithoutError() {
        PdfTestHelper h = new PdfTestHelper();
        Gradients.applyVerticalGradient(h.cb, 10, 10, 100, 0,
                Colors.PRIMARY, Colors.SURFACE);
        h.cb.rectangle(0, 0, 1, 1);
        h.cb.fill();
        h.document.close();
    }
}
