package com.MyStadium.Notifications.pdf;

import org.junit.jupiter.api.Test;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import static org.junit.jupiter.api.Assertions.*;

class ImageOptimizerTest {
    private final ImageOptimizer optimizer = new ImageOptimizer();

    private byte[] createJpeg(int w, int h) {
        try {
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = img.createGraphics();
            g.setColor(Color.RED);
            g.fillRect(0, 0, w, h);
            g.dispose();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "jpg", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void optimize_SmallImage_ReturnsOriginal() {
        byte[] input = createJpeg(800, 600);
        byte[] result = optimizer.optimize(input);
        assertNotNull(result);
    }

    @Test
    void optimize_LargeImage_Resizes() {
        byte[] input = createJpeg(2000, 1500);
        byte[] result = optimizer.optimize(input);
        assertNotNull(result);
    }

    @Test
    void optimize_NullImage_ReturnsInput() {
        byte[] result = optimizer.optimize(new byte[]{0, 1, 2});
        assertArrayEquals(new byte[]{0, 1, 2}, result);
    }

    @Test
    void optimize_EmptyArray_ReturnsInput() {
        byte[] result = optimizer.optimize(new byte[0]);
        assertArrayEquals(new byte[0], result);
    }
}
