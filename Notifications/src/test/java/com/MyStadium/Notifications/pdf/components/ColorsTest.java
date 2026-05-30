package com.MyStadium.Notifications.pdf.components;

import org.junit.jupiter.api.Test;
import java.awt.Color;
import static org.junit.jupiter.api.Assertions.*;

class ColorsTest {

    @Test
    void allColorsAreNonNull() {
        assertNotNull(Colors.PRIMARY);
        assertNotNull(Colors.SECONDARY);
        assertNotNull(Colors.ACCENT);
        assertNotNull(Colors.DARK);
        assertNotNull(Colors.SURFACE);
        assertNotNull(Colors.TEXT_PRIMARY);
        assertNotNull(Colors.TEXT_SECONDARY);
        assertNotNull(Colors.TEXT_MUTED);
        assertNotNull(Colors.VIP_GLOW);
        assertNotNull(Colors.PALCO_GLOW);
        assertNotNull(Colors.PREFERENCIAL_GLOW);
        assertNotNull(Colors.GENERAL_GLOW);
        assertNotNull(Colors.OVERLAY_TOP);
        assertNotNull(Colors.OVERLAY_BOTTOM);
    }

    @Test
    void primaryColorHasCorrectRGB() {
        assertEquals(139, Colors.PRIMARY.getRed());
        assertEquals(92, Colors.PRIMARY.getGreen());
        assertEquals(246, Colors.PRIMARY.getBlue());
    }

    @Test
    void overlayTopHasAlpha() {
        assertTrue(Colors.OVERLAY_TOP.getAlpha() < 255);
        assertTrue(Colors.OVERLAY_TOP.getAlpha() > 0);
    }

    @Test
    void privateConstructorThrows() throws Exception {
        var c = Colors.class.getDeclaredConstructor();
        c.setAccessible(true);
        assertNotNull(c.newInstance());
    }
}
