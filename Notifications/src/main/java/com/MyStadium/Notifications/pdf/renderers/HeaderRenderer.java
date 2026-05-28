package com.MyStadium.Notifications.pdf.renderers;

import com.MyStadium.Notifications.pdf.TicketCanvas;
import com.MyStadium.Notifications.pdf.components.Colors;
import com.MyStadium.Notifications.pdf.components.Spacing;
import com.MyStadium.Notifications.pdf.components.Typography;
import com.MyStadium.Notifications.pdf.dto.TicketPdfData;
import org.springframework.stereotype.Component;

@Component
public class HeaderRenderer {

    public void render(TicketCanvas canvas, TicketPdfData data) {
        float x = 28;
        float y = Spacing.BANNER_TOP - 18;

        canvas.drawText("MYSTADIUM", x, y, Typography.bold(8f, Colors.PRIMARY));

        float orderX = Spacing.STUB_RIGHT + 20;
        canvas.drawText("ORDEN " + data.getOrderCode(), orderX, y, Typography.medium(7f, Colors.TEXT_MUTED));

        float artistY = Spacing.BANNER_TOP - 46;
        canvas.drawText(data.getArtist(), x, artistY, Typography.bold(18f, Colors.TEXT_PRIMARY));

        float nameY = artistY - 20;
        canvas.drawText(data.getEventName(), x, nameY, Typography.medium(11f, Colors.TEXT_SECONDARY));
    }
}
