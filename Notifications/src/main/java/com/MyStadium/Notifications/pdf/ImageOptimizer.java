package com.MyStadium.Notifications.pdf;

import org.springframework.stereotype.Component;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Component
public class ImageOptimizer {

    private static final int MAX_WIDTH = 1200;
    private static final float QUALITY = 0.75f;

    public byte[] optimize(byte[] input) {
        try {
            BufferedImage original = ImageIO.read(new ByteArrayInputStream(input));
            if (original == null) return input;

            BufferedImage resized = resize(original);
            return compress(resized);
        } catch (Exception e) {
            return input;
        }
    }

    private BufferedImage resize(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        if (w <= MAX_WIDTH) return img;

        float ratio = (float) MAX_WIDTH / w;
        int newW = MAX_WIDTH;
        int newH = Math.round(h * ratio);

        BufferedImage resized = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(img, 0, 0, newW, newH, null);
        g2d.dispose();
        return resized;
    }

    private byte[] compress(BufferedImage img) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        javax.imageio.plugins.jpeg.JPEGImageWriteParam jpegParams = new javax.imageio.plugins.jpeg.JPEGImageWriteParam(null);
        jpegParams.setCompressionMode(javax.imageio.plugins.jpeg.JPEGImageWriteParam.MODE_EXPLICIT);
        jpegParams.setCompressionQuality(QUALITY);

        javax.imageio.ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        writer.setOutput(ImageIO.createImageOutputStream(baos));
        writer.write(null, new javax.imageio.IIOImage(img, null, null), jpegParams);
        writer.dispose();
        return baos.toByteArray();
    }
}
