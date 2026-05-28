package com.MyStadium.Notifications.pdf.components;

import com.lowagie.text.pdf.PdfContentByte;
import java.awt.Color;

public final class Gradients {

    public static void applyVerticalGradient(PdfContentByte cb, float x, float y, float w, float h,
                                              Color topColor, Color bottomColor) {
        for (int i = 0; i < (int) h; i++) {
            float ratio = (float) i / h;
            Color c = lerpColor(topColor, bottomColor, ratio);
            cb.setRGBColorFill(c.getRed(), c.getGreen(), c.getBlue());
            cb.setRGBColorStroke(c.getRed(), c.getGreen(), c.getBlue());
            cb.rectangle(x, y + i, w, 1);
            cb.fill();
        }
    }

    public static void applyVerticalAlphaGradient(PdfContentByte cb, float x, float y, float w, float h,
                                                   Color topColor, int topAlpha,
                                                   Color bottomColor, int bottomAlpha) {
        for (int i = 0; i < (int) h; i++) {
            float ratio = (float) i / h;
            int alpha = (int) (topAlpha + (bottomAlpha - topAlpha) * ratio);
            Color c = lerpColor(topColor, bottomColor, ratio);
            cb.saveState();
            cb.setRGBColorFill(c.getRed(), c.getGreen(), c.getBlue());
            if (alpha < 255) {
                cb.setGState(new com.lowagie.text.pdf.PdfGState() {{
                    setFillOpacity(alpha / 255f);
                }});
            }
            cb.rectangle(x, y + i, w, 1);
            cb.fill();
            cb.restoreState();
        }
    }

    public static void applyCinematicOverlay(PdfContentByte cb, float x, float y, float w, float h) {
        applyVerticalAlphaGradient(cb, x, y, w, h,
                Colors.OVERLAY_TOP, Colors.OVERLAY_TOP.getAlpha(),
                Colors.OVERLAY_BOTTOM, Colors.OVERLAY_BOTTOM.getAlpha());
    }

    private static Color lerpColor(Color a, Color b, float t) {
        int r = (int) (a.getRed() + (b.getRed() - a.getRed()) * t);
        int g = (int) (a.getGreen() + (b.getGreen() - a.getGreen()) * t);
        int bl = (int) (a.getBlue() + (b.getBlue() - a.getBlue()) * t);
        return new Color(clamp(r), clamp(g), clamp(bl));
    }

    private static int clamp(int v) {
        return Math.max(0, Math.min(255, v));
    }

    private Gradients() {}
}
