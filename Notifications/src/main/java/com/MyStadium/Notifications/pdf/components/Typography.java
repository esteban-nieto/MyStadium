package com.MyStadium.Notifications.pdf.components;

import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;
import java.awt.Color;

public final class Typography {
    private static boolean fontsRegistered = false;

    public static synchronized void registerFonts() {
        if (fontsRegistered) return;
        try {
            FontFactory.register("/tickets/fonts/Inter-Regular.ttf", "Inter");
            FontFactory.register("/tickets/fonts/Inter-Medium.ttf", "Inter-Medium");
            FontFactory.register("/tickets/fonts/Inter-SemiBold.ttf", "Inter-SemiBold");
            FontFactory.register("/tickets/fonts/Inter-Bold.ttf", "Inter-Bold");
        } catch (Exception e) {
            // Fallback to Helvetica if fonts not loaded
        }
        fontsRegistered = true;
    }

    public static Font regular(float size) {
        return getFont("Inter", Font.NORMAL, size);
    }

    public static Font medium(float size) {
        return getFont("Inter-Medium", Font.NORMAL, size);
    }

    public static Font semibold(float size) {
        return getFont("Inter-SemiBold", Font.NORMAL, size);
    }

    public static Font bold(float size) {
        return getFont("Inter-Bold", Font.BOLD, size);
    }

    public static Font regular(float size, Color color) {
        Font f = regular(size);
        f.setColor(color);
        return f;
    }

    public static Font medium(float size, Color color) {
        Font f = medium(size);
        f.setColor(color);
        return f;
    }

    public static Font semibold(float size, Color color) {
        Font f = semibold(size);
        f.setColor(color);
        return f;
    }

    public static Font bold(float size, Color color) {
        Font f = bold(size);
        f.setColor(color);
        return f;
    }

    private static Font getFont(String name, int style, float size) {
        if (FontFactory.isRegistered(name)) {
            return FontFactory.getFont(name, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, size, style);
        }
        return FontFactory.getFont(FontFactory.HELVETICA, size, style);
    }

    private Typography() {}
}
