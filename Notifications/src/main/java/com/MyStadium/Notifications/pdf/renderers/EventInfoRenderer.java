package com.MyStadium.Notifications.pdf.renderers;

import com.MyStadium.Notifications.pdf.TicketCanvas;
import com.MyStadium.Notifications.pdf.components.Colors;
import com.MyStadium.Notifications.pdf.components.Spacing;
import com.MyStadium.Notifications.pdf.components.Typography;
import com.MyStadium.Notifications.pdf.dto.TicketPdfData;
import com.MyStadium.Notifications.pdf.dto.TicketEntry;
import org.springframework.stereotype.Component;
import java.time.format.DateTimeFormatter;

@Component
public class EventInfoRenderer {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    public void render(TicketCanvas canvas, TicketPdfData data, TicketEntry entry) {
        float x = 28;
        float baseY = Spacing.BANNER_BOTTOM - 18;
        float y = baseY;

        String fecha = data.getFechaEvento().format(DATE_FMT);
        String hora = data.getFechaEvento().format(TIME_FMT);
        canvas.drawText(fecha + "  |  " + hora, x, y, Typography.regular(9f, Colors.TEXT_SECONDARY));
        y -= 16;

        if (data.getStadium() != null && !data.getStadium().isEmpty()) {
            canvas.drawText(data.getStadium(), x, y, Typography.regular(9f, Colors.TEXT_MUTED));
            y -= 18;
        }

        canvas.drawLine(x, y + 2, Spacing.STUB_RIGHT - 8, y + 2, Colors.TEXT_MUTED, 0.3f);
        y -= 14;

        canvas.drawText("Secci\u00f3n:  " + entry.getZona(), x, y, Typography.medium(11f, Colors.TEXT_PRIMARY));
        float asientoX = x + 155;
        canvas.drawText("Asiento:  " + entry.getAsiento(), asientoX, y, Typography.medium(11f, Colors.TEXT_PRIMARY));
        y -= 16;

        String tipo = entry.getTipoEntrada() != null && !entry.getTipoEntrada().isEmpty()
                ? entry.getTipoEntrada() : "General";
        canvas.drawText("Tipo:  " + tipo, x, y, Typography.regular(8f, Colors.TEXT_MUTED));
    }
}
