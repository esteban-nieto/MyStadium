package com.MyStadium.Notifications.pdf;

import com.MyStadium.Notifications.pdf.components.Colors;
import com.MyStadium.Notifications.pdf.components.Spacing;
import com.MyStadium.Notifications.pdf.components.Typography;
import com.MyStadium.Notifications.pdf.dto.TicketPdfData;
import com.MyStadium.Notifications.pdf.dto.TicketEntry;
import com.MyStadium.Notifications.pdf.renderers.*;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;

@Service
public class TicketPdfGenerator {

    private final BackgroundRenderer backgroundRenderer;
    private final BannerRenderer bannerRenderer;
    private final HeaderRenderer headerRenderer;
    private final EventInfoRenderer eventInfoRenderer;
    private final QrRenderer qrRenderer;
    private final BarcodeRenderer barcodeRenderer;
    private final FooterRenderer footerRenderer;
    private final AntiFraudRenderer antiFraudRenderer;

    public TicketPdfGenerator(BackgroundRenderer backgroundRenderer,
                              BannerRenderer bannerRenderer,
                              HeaderRenderer headerRenderer,
                              EventInfoRenderer eventInfoRenderer,
                              QrRenderer qrRenderer,
                              BarcodeRenderer barcodeRenderer,
                              FooterRenderer footerRenderer,
                              AntiFraudRenderer antiFraudRenderer) {
        this.backgroundRenderer = backgroundRenderer;
        this.bannerRenderer = bannerRenderer;
        this.headerRenderer = headerRenderer;
        this.eventInfoRenderer = eventInfoRenderer;
        this.qrRenderer = qrRenderer;
        this.barcodeRenderer = barcodeRenderer;
        this.footerRenderer = footerRenderer;
        this.antiFraudRenderer = antiFraudRenderer;
    }

    public byte[] generate(TicketPdfData data) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(new com.lowagie.text.Rectangle(Spacing.PAGE_WIDTH, Spacing.PAGE_HEIGHT));
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        Typography.registerFonts();

        int total = data.getTickets().size();
        for (int i = 0; i < total; i++) {
            if (i > 0) document.newPage();

            TicketEntry entry = data.getTickets().get(i);
            PdfContentByte cb = writer.getDirectContent();
            TicketCanvas canvas = new TicketCanvas(cb, Spacing.PAGE_WIDTH, Spacing.PAGE_HEIGHT);

            // 1. Layer 0: Background solid dark
            backgroundRenderer.render(canvas, data, i, total);

            // 2. Layer 1: Hero banner image (full-width, top 42%)
            bannerRenderer.render(canvas, data);

            // 3. Layer 2: Text on banner (logo, artist, event name)
            headerRenderer.render(canvas, data);

            // 4. Layer 3: Event info below banner
            eventInfoRenderer.render(canvas, data, entry);

            // 5. Layer 4: QR + barcode (right stub)
            qrRenderer.render(canvas, data, entry);
            barcodeRenderer.render(canvas, data, entry);

            // 6. Layer 5: Footer
            footerRenderer.render(canvas, data, entry);

            // 7. Layer 6: Anti-fraud noise texture (subtle, on top)
            antiFraudRenderer.render(canvas, data);

            // 8. Layer 7: Edge glow (on top of everything)
            drawEdgeGlow(canvas);

            // 9. Layer 8: Diagonal watermark (on top of everything)
            canvas.drawDiagonalWatermark("MYSTADIUM", Colors.TEXT_MUTED, 0.03f);
        }

        document.close();
        return baos.toByteArray();
    }

    private void drawEdgeGlow(TicketCanvas canvas) {
        PdfContentByte cb = canvas.getCb();
        cb.saveState();
        cb.setGState(new PdfGState() {{ setFillOpacity(0.12f); }});
        cb.setRGBColorStroke(Colors.PRIMARY.getRed(), Colors.PRIMARY.getGreen(), Colors.PRIMARY.getBlue());
        cb.setLineWidth(3f);
        cb.roundRectangle(Spacing.MARGIN - 1, Spacing.MARGIN - 1,
                Spacing.PAGE_WIDTH - 2 * Spacing.MARGIN + 2,
                Spacing.PAGE_HEIGHT - 2 * Spacing.MARGIN + 2, Spacing.CORNER_RADIUS);
        cb.stroke();
        cb.restoreState();
    }
}
