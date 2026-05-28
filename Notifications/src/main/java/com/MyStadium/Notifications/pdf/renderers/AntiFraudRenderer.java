package com.MyStadium.Notifications.pdf.renderers;

import com.MyStadium.Notifications.pdf.TicketCanvas;
import com.MyStadium.Notifications.pdf.components.Spacing;
import com.MyStadium.Notifications.pdf.dto.TicketPdfData;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfGState;
import org.springframework.stereotype.Component;

@Component
public class AntiFraudRenderer {

    public void render(TicketCanvas canvas, TicketPdfData data) {
        float w = Spacing.PAGE_WIDTH;
        float h = Spacing.PAGE_HEIGHT;

        try {
            java.io.InputStream is = getClass().getResourceAsStream("/tickets/textures/noise.png");
            if (is != null) {
                byte[] bytes = is.readAllBytes();
                Image noise = Image.getInstance(bytes);
                noise.scaleAbsolute(w, h);
                noise.setAbsolutePosition(0, 0);
                canvas.getCb().saveState();
                canvas.getCb().setGState(new PdfGState() {{ setFillOpacity(0.04f); }});
                canvas.getCb().addImage(noise);
                canvas.getCb().restoreState();
            }
        } catch (Exception e) {
            // Silently skip if noise texture not available
        }
    }
}
