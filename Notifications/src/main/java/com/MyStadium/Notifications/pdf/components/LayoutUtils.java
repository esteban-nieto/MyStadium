package com.MyStadium.Notifications.pdf.components;

import com.lowagie.text.pdf.PdfContentByte;
import java.awt.Color;

public final class LayoutUtils {

    public static void drawRoundedRect(PdfContentByte cb, float x, float y, float w, float h, float r) {
        cb.roundRectangle(x, y, w, h, r);
    }

    public static void fillRoundedRect(PdfContentByte cb, float x, float y, float w, float h, float r, Color color) {
        cb.saveState();
        cb.setRGBColorFill(color.getRed(), color.getGreen(), color.getBlue());
        cb.roundRectangle(x, y, w, h, r);
        cb.fill();
        cb.restoreState();
    }

    public static void drawDashedLine(PdfContentByte cb, float x1, float y1, float x2, float y2) {
        cb.saveState();
        cb.setLineDash(3f, 3f);
        cb.moveTo(x1, y1);
        cb.lineTo(x2, y2);
        cb.stroke();
        cb.restoreState();
    }

    public static void drawDottedLine(PdfContentByte cb, float x1, float y1, float x2, float y2) {
        cb.saveState();
        cb.setLineDash(1f, 3f);
        cb.moveTo(x1, y1);
        cb.lineTo(x2, y2);
        cb.stroke();
        cb.restoreState();
    }

    public static void drawSoftShadow(PdfContentByte cb, float x, float y, float w, float h, float r) {
        cb.saveState();
        cb.setRGBColorStroke(0, 0, 0);
        cb.setLineWidth(2f);
        cb.setGState(new com.lowagie.text.pdf.PdfGState() {{
            setFillOpacity(0.08f);
        }});
        cb.roundRectangle(x + 2, y - 2, w, h, r);
        cb.stroke();
        cb.restoreState();
    }

    private LayoutUtils() {}
}
