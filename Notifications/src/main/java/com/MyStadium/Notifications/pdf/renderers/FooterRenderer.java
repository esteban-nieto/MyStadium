package com.MyStadium.Notifications.pdf.renderers;

import com.MyStadium.Notifications.pdf.TicketCanvas;
import com.MyStadium.Notifications.pdf.components.Colors;
import com.MyStadium.Notifications.pdf.components.Spacing;
import com.MyStadium.Notifications.pdf.components.Typography;
import com.MyStadium.Notifications.pdf.dto.TicketEntry;
import com.MyStadium.Notifications.pdf.dto.TicketPdfData;
import org.springframework.stereotype.Component;

@Component
public class FooterRenderer {

    public void render(TicketCanvas canvas, TicketPdfData data, TicketEntry entry) {
        float y = Spacing.MARGIN + 4;

        canvas.drawLine(28, y + 6, Spacing.PAGE_WIDTH - 28, y + 6, Colors.TEXT_MUTED, 0.3f);

        String ticketInfo = "Ticket " + (entry.getIndex() + 1) + " de " + entry.getTotal()
                + "  |  myStadium.co";
        canvas.drawText(ticketInfo, 28, y, Typography.regular(7f, Colors.TEXT_MUTED));

        String powered = "Powered by MyStadium Ticketing";
        float pw = 7 * powered.length();
        canvas.drawText(powered, Spacing.PAGE_WIDTH - 28 - pw, y, Typography.regular(6f, Colors.TEXT_MUTED));
    }
}
