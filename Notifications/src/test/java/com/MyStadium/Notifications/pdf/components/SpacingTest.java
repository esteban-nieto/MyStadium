package com.MyStadium.Notifications.pdf.components;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SpacingTest {

    @Test
    void pageDimensionsArePositive() {
        assertTrue(Spacing.PAGE_WIDTH > 0);
        assertTrue(Spacing.PAGE_HEIGHT > 0);
    }

    @Test
    void marginIsPositive() {
        assertTrue(Spacing.MARGIN > 0);
    }

    @Test
    void bannerHeightIsLessThanPageHeight() {
        assertTrue(Spacing.BANNER_HEIGHT < Spacing.PAGE_HEIGHT);
    }

    @Test
    void qrSizeIsPositive() {
        assertTrue(Spacing.QR_SIZE > 0);
    }

    @Test
    void cornerRadiusIsPositive() {
        assertTrue(Spacing.CORNER_RADIUS > 0);
    }

    @Test
    void stubRightIsWithinPage() {
        assertTrue(Spacing.STUB_RIGHT > Spacing.MARGIN);
        assertTrue(Spacing.STUB_RIGHT < Spacing.PAGE_WIDTH);
    }
}
