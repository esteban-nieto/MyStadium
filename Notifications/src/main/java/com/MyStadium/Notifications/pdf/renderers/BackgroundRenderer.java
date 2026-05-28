package com.MyStadium.Notifications.pdf.renderers;

import com.MyStadium.Notifications.pdf.TicketCanvas;
import com.MyStadium.Notifications.pdf.components.Colors;
import com.MyStadium.Notifications.pdf.components.Gradients;
import com.MyStadium.Notifications.pdf.components.Spacing;
import com.MyStadium.Notifications.pdf.dto.TicketPdfData;
import org.springframework.stereotype.Component;

@Component
public class BackgroundRenderer {

    public void render(TicketCanvas canvas, TicketPdfData data, int page, int totalPages) {
        float w = Spacing.PAGE_WIDTH;
        float h = Spacing.PAGE_HEIGHT;

        canvas.getCb().saveState();
        canvas.getCb().setRGBColorFill(Colors.DARK.getRed(), Colors.DARK.getGreen(), Colors.DARK.getBlue());
        canvas.getCb().rectangle(0, 0, w, h);
        canvas.getCb().fill();
        canvas.getCb().restoreState();
    }
}
