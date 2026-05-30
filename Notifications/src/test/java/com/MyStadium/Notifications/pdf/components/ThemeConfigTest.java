package com.MyStadium.Notifications.pdf.components;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ThemeConfigTest {

    @Test
    void defaultThemeReturnsNonNull() {
        ThemeConfig theme = ThemeConfig.defaultTheme();
        assertNotNull(theme);
    }

    @Test
    void defaultThemeHasCorrectColors() {
        ThemeConfig theme = ThemeConfig.defaultTheme();
        assertEquals(Colors.PRIMARY, theme.getPrimary());
        assertEquals(Colors.SECONDARY, theme.getSecondary());
        assertEquals(Colors.ACCENT, theme.getAccent());
        assertEquals(Colors.SURFACE, theme.getSurface());
        assertEquals(Colors.TEXT_PRIMARY, theme.getTextPrimary());
        assertEquals(Colors.TEXT_SECONDARY, theme.getTextSecondary());
        assertEquals(Colors.DARK, theme.getDark());
        assertEquals(Colors.OVERLAY_TOP, theme.getOverlayTop());
        assertEquals(Colors.OVERLAY_BOTTOM, theme.getOverlayBottom());
    }

    @Test
    void builderAndSetters() {
        ThemeConfig theme = ThemeConfig.builder().primary(Colors.ACCENT).dark(Colors.SURFACE).build();
        assertEquals(Colors.ACCENT, theme.getPrimary());
        assertEquals(Colors.SURFACE, theme.getDark());
    }

    @Test
    void toStringContainsClassName() {
        String s = ThemeConfig.defaultTheme().toString();
        assertTrue(s.contains("ThemeConfig"));
    }
}
