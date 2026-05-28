package com.MyStadium.Notifications.pdf.renderers;

import com.MyStadium.Notifications.pdf.TicketCanvas;
import com.MyStadium.Notifications.pdf.components.Gradients;
import com.MyStadium.Notifications.pdf.components.Spacing;
import com.MyStadium.Notifications.pdf.dto.TicketPdfData;
import com.lowagie.text.Image;
import org.springframework.stereotype.Component;

@Component
public class BannerRenderer {

    public void render(TicketCanvas canvas, TicketPdfData data) {
        float bw = Spacing.PAGE_WIDTH;
        float bx = 0;
        float by = Spacing.PAGE_HEIGHT - Spacing.BANNER_HEIGHT;

        if (data.getEventBanner() != null && data.getEventBanner().length > 0) {
            try {
                Image banner = Image.getInstance(data.getEventBanner());
                banner.scaleAbsolute(bw, Spacing.BANNER_HEIGHT);
                banner.setAbsolutePosition(bx, by);
                canvas.getCb().addImage(banner);
                System.out.println("[BANNER] Image inserted at " + bx + "," + by + " size=" + bw + "x" + Spacing.BANNER_HEIGHT);
            } catch (Exception e) {
                System.err.println("[BANNER] Error inserting image: " + e.getMessage());
            }
        } else {
            System.out.println("[BANNER] No banner data (null or empty)");
        }

        Gradients.applyCinematicOverlay(canvas.getCb(), bx, by, bw, Spacing.BANNER_HEIGHT);
    }
}
