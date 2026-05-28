package com.MyStadium.Notifications.pdf;

import com.MyStadium.Notifications.pdf.components.Colors;
import com.MyStadium.Notifications.pdf.components.LayoutUtils;
import com.MyStadium.Notifications.pdf.components.Spacing;
import com.MyStadium.Notifications.pdf.components.Typography;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import lombok.Getter;
import java.awt.Color;

public class TicketCanvas {
    @Getter
    private final PdfContentByte cb;
    private final float pageWidth;
    private final float pageHeight;

    public TicketCanvas(PdfContentByte cb, float pageWidth, float pageHeight) {
        this.cb = cb;
        this.pageWidth = pageWidth;
        this.pageHeight = pageHeight;
    }

    public void drawBackground() {
        cb.saveState();
        cb.setRGBColorFill(Colors.DARK.getRed(), Colors.DARK.getGreen(), Colors.DARK.getBlue());
        cb.rectangle(0, 0, pageWidth, pageHeight);
        cb.fill();
        cb.restoreState();
    }

    public void drawSurfaceCard(float x, float y, float w, float h) {
        LayoutUtils.fillRoundedRect(cb, x, y, w, h, Spacing.CORNER_RADIUS, Colors.SURFACE);
    }

    public void drawText(String text, float x, float y, Font font) {
        cb.saveState();
        cb.beginText();
        cb.setFontAndSize(font.getBaseFont(), font.getSize());
        Color c = font.getColor();
        if (c != null) cb.setRGBColorFill(c.getRed(), c.getGreen(), c.getBlue());
        cb.setTextMatrix(x, y);
        cb.showText(text);
        cb.endText();
        cb.restoreState();
    }

    public void drawTextOpacity(String text, float x, float y, Font font, float opacity) {
        cb.saveState();
        cb.setGState(new PdfGState() {{ setFillOpacity(opacity); }});
        cb.beginText();
        cb.setFontAndSize(font.getBaseFont(), font.getSize());
        Color c = font.getColor();
        if (c != null) cb.setRGBColorFill(c.getRed(), c.getGreen(), c.getBlue());
        cb.setTextMatrix(x, y);
        cb.showText(text);
        cb.endText();
        cb.restoreState();
    }

    public void drawImage(com.lowagie.text.Image img, float x, float y, float w, float h) {
        img.scaleToFit(w, h);
        img.setAbsolutePosition(x, y);
        cb.addImage(img);
    }

    public void drawImageAbsolute(com.lowagie.text.Image img, float x, float y, float w, float h) {
        img.scaleAbsolute(w, h);
        img.setAbsolutePosition(x, y);
        cb.addImage(img);
    }

    public void drawCircle(float cx, float cy, float r, Color color) {
        cb.saveState();
        cb.setRGBColorFill(color.getRed(), color.getGreen(), color.getBlue());
        cb.circle(cx, cy, r);
        cb.fill();
        cb.restoreState();
    }

    public void drawLine(float x1, float y1, float x2, float y2, Color color, float width) {
        cb.saveState();
        cb.setRGBColorStroke(color.getRed(), color.getGreen(), color.getBlue());
        cb.setLineWidth(width);
        cb.moveTo(x1, y1);
        cb.lineTo(x2, y2);
        cb.stroke();
        cb.restoreState();
    }

    public void clipRect(float x, float y, float w, float h) {
        cb.rectangle(x, y, w, h);
        cb.clip();
        cb.newPath();
    }

    public void drawDiagonalWatermark(String text, Color color, float opacity) {
        cb.saveState();
        cb.setGState(new PdfGState() {{ setFillOpacity(opacity); }});
        cb.beginText();
        try {
            cb.setFontAndSize(com.lowagie.text.FontFactory.getFont(
                    com.lowagie.text.FontFactory.HELVETICA_BOLD, 36).getBaseFont(), 36);
        } catch (Exception e) {
            cb.endText();
            cb.restoreState();
            return;
        }
        cb.setRGBColorFill(color.getRed(), color.getGreen(), color.getBlue());
        float cx = pageWidth / 2;
        float cy = pageHeight / 2;
        double angle = Math.toRadians(-30);
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        cb.setTextMatrix(cos, sin, -sin, cos, cx - 80, cy);
        cb.showText(text);
        cb.endText();
        cb.restoreState();
    }
}
