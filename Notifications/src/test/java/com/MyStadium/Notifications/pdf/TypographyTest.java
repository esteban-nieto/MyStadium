package com.MyStadium.Notifications.pdf;

import com.MyStadium.Notifications.pdf.components.Typography;
import com.lowagie.text.Font;
import org.junit.jupiter.api.Test;
import java.awt.Color;
import static org.junit.jupiter.api.Assertions.*;

class TypographyTest {

    @Test
    void registerFonts_RunsWithoutError() {
        Typography.registerFonts();
        assertDoesNotThrow(() -> Typography.registerFonts());
    }

    @Test
    void regular_CreatesFont() {
        Font f = Typography.regular(10f);
        assertNotNull(f);
        assertEquals(10f, f.getSize(), 0.001);
    }

    @Test
    void medium_CreatesFont() {
        Font f = Typography.medium(12f);
        assertNotNull(f);
        assertEquals(12f, f.getSize(), 0.001);
    }

    @Test
    void semibold_CreatesFont() {
        Font f = Typography.semibold(14f);
        assertNotNull(f);
        assertEquals(14f, f.getSize(), 0.001);
    }

    @Test
    void bold_CreatesFont() {
        Font f = Typography.bold(16f);
        assertNotNull(f);
        assertEquals(16f, f.getSize(), 0.001);
    }

    @Test
    void regular_WithColor() {
        Font f = Typography.regular(10f, Color.RED);
        assertNotNull(f);
        assertEquals(Color.RED, f.getColor());
    }

    @Test
    void medium_WithColor() {
        Font f = Typography.medium(12f, Color.BLUE);
        assertNotNull(f);
        assertEquals(Color.BLUE, f.getColor());
    }

    @Test
    void semibold_WithColor() {
        Font f = Typography.semibold(14f, Color.GREEN);
        assertNotNull(f);
        assertEquals(Color.GREEN, f.getColor());
    }

    @Test
    void bold_WithColor() {
        Font f = Typography.bold(16f, Color.GRAY);
        assertNotNull(f);
        assertEquals(Color.GRAY, f.getColor());
    }
}
