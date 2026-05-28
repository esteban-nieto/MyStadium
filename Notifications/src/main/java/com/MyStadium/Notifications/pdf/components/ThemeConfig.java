package com.MyStadium.Notifications.pdf.components;

import lombok.Builder;
import lombok.Data;
import java.awt.Color;

@Data
@Builder
public class ThemeConfig {
    private Color primary;
    private Color secondary;
    private Color accent;
    private Color surface;
    private Color textPrimary;
    private Color textSecondary;
    private Color dark;
    private Color overlayTop;
    private Color overlayBottom;

    public static ThemeConfig defaultTheme() {
        return ThemeConfig.builder()
                .primary(Colors.PRIMARY)
                .secondary(Colors.SECONDARY)
                .accent(Colors.ACCENT)
                .surface(Colors.SURFACE)
                .textPrimary(Colors.TEXT_PRIMARY)
                .textSecondary(Colors.TEXT_SECONDARY)
                .dark(Colors.DARK)
                .overlayTop(Colors.OVERLAY_TOP)
                .overlayBottom(Colors.OVERLAY_BOTTOM)
                .build();
    }
}
