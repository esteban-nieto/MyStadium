package com.MyStadium.Notifications.pdf;

import org.springframework.beans.factory.annotation.Value;
import com.MyStadium.Notifications.pdf.components.Spacing;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BannerCacheService {

    private final ConcurrentHashMap<String, byte[]> cache = new ConcurrentHashMap<>();
    private final WebClient webClient;
    private final String imagesBasePath;

    public BannerCacheService(ImageOptimizer imageOptimizer,
                              @Value("${app.images.base-path:}") String imagesBasePath) {
        this.webClient = WebClient.create();
        this.imagesBasePath = imagesBasePath;
    }

    public byte[] getBanner(String imagePath) {
        if (imagePath == null || imagePath.isBlank()) return null;

        return cache.computeIfAbsent(imagePath, path -> {
            try {
                String fullPath;
                if (path.startsWith("http://") || path.startsWith("https://")) {
                    byte[] raw = webClient.get().uri(path).retrieve().bodyToMono(byte[].class).block();
                    if (raw == null) return null;
                    return resizeAndCompress(raw);
                }

                String fileName = new File(path).getName();
                fullPath = imagesBasePath + File.separator + fileName;

                File imgFile = new File(fullPath);
                System.out.println("[BANNER DEBUG] imagenUrl=" + path
                        + " | fileName=" + fileName
                        + " | fullPath=" + fullPath
                        + " | exists=" + imgFile.exists());

                if (!imgFile.exists()) {
                    System.out.println("[BANNER] File not found at: " + fullPath);
                    return null;
                }

                BufferedImage img = readImageRobustly(imgFile);
                if (img == null) return null;

                BufferedImage resized = new BufferedImage(555, (int) Spacing.BANNER_HEIGHT, BufferedImage.TYPE_INT_RGB);
                java.awt.Graphics2D g2d = resized.createGraphics();
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION,
                        java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.drawImage(img, 0, 0, 555, (int) Spacing.BANNER_HEIGHT, null);
                g2d.dispose();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                javax.imageio.plugins.jpeg.JPEGImageWriteParam jpegParams =
                        new javax.imageio.plugins.jpeg.JPEGImageWriteParam(null);
                jpegParams.setCompressionMode(javax.imageio.plugins.jpeg.JPEGImageWriteParam.MODE_EXPLICIT);
                jpegParams.setCompressionQuality(0.80f);
                javax.imageio.ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
                writer.setOutput(ImageIO.createImageOutputStream(baos));
                writer.write(null, new javax.imageio.IIOImage(resized, null, null), jpegParams);
                writer.dispose();

                byte[] result = baos.toByteArray();
                System.out.println("[BANNER DEBUG] loaded " + result.length + " bytes");
                return result;
            } catch (Exception e) {
                System.err.println("[BANNER ERROR] " + e.getMessage());
                return null;
            }
        });
    }

    private BufferedImage readImageRobustly(File file) {
        try {
            BufferedImage img = ImageIO.read(file);
            if (img != null) return img;

            byte[] fileBytes = Files.readAllBytes(file.toPath());
            System.out.println("[BANNER] ImageIO.read returned null for " + file.getName()
                    + " (" + fileBytes.length + " bytes), trying fallback readers...");

            try (ByteArrayInputStream bais = new ByteArrayInputStream(fileBytes);
                 ImageInputStream iis = ImageIO.createImageInputStream(bais)) {
                Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
                while (readers.hasNext()) {
                    ImageReader reader = readers.next();
                    try {
                        reader.setInput(iis, false, true);
                        img = reader.read(0);
                        if (img != null) {
                            System.out.println("[BANNER] Loaded via " + reader.getClass().getSimpleName()
                                    + " (" + reader.getFormatName() + ")");
                            return img;
                        }
                    } catch (Exception ignored) {
                    } finally {
                        reader.dispose();
                    }
                }
            }

            try (ByteArrayInputStream bais = new ByteArrayInputStream(fileBytes);
                 ImageInputStream iis = ImageIO.createImageInputStream(bais)) {
                Iterator<ImageReader> readers = ImageIO.getImageReadersBySuffix("jpg");
                while (readers.hasNext()) {
                    ImageReader reader = readers.next();
                    try {
                        reader.setInput(iis, false, true);
                        img = reader.read(0);
                        if (img != null) return img;
                    } catch (Exception ignored) {
                    } finally {
                        reader.dispose();
                    }
                }
            }

            System.err.println("[BANNER] All readers failed for " + file.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("[BANNER] readImageRobustly error: " + e.getMessage());
        }
        return null;
    }

    private byte[] resizeAndCompress(byte[] raw) throws Exception {
        BufferedImage original = ImageIO.read(new java.io.ByteArrayInputStream(raw));
        if (original == null) return raw;
        BufferedImage resized = new BufferedImage(555, (int) Spacing.BANNER_HEIGHT, BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics2D g2d = resized.createGraphics();
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION,
                java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(original, 0, 0, 555, (int) Spacing.BANNER_HEIGHT, null);
        g2d.dispose();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        javax.imageio.plugins.jpeg.JPEGImageWriteParam jpegParams =
                new javax.imageio.plugins.jpeg.JPEGImageWriteParam(null);
        jpegParams.setCompressionMode(javax.imageio.plugins.jpeg.JPEGImageWriteParam.MODE_EXPLICIT);
        jpegParams.setCompressionQuality(0.80f);
        javax.imageio.ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        writer.setOutput(ImageIO.createImageOutputStream(baos));
        writer.write(null, new javax.imageio.IIOImage(resized, null, null), jpegParams);
        writer.dispose();
        return baos.toByteArray();
    }

    public byte[] getBannerByArtist(String artistName) {
        if (artistName == null || artistName.isBlank()) return null;
        File dir = new File(imagesBasePath);
        if (!dir.isDirectory()) return null;
        File[] files = dir.listFiles((d, name) -> {
            String lower = name.toLowerCase();
            String artist = artistName.toLowerCase().replaceAll("\\s+", "");
            String artistNoSpace = artistName.toLowerCase().replaceAll("[^a-z0-9]", "");
            return lower.contains(artist) || lower.contains(artistNoSpace)
                || artist.contains(lower.replaceAll("\\.[^.]+$", ""));
        });
        if (files != null && files.length > 0) {
            return getBanner(files[0].getAbsolutePath());
        }
        return null;
    }

    public void clear() { cache.clear(); }
    public void evict(String imagePath) { cache.remove(imagePath); }
}
