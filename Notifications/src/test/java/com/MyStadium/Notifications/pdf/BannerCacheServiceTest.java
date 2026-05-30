package com.MyStadium.Notifications.pdf;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import static org.junit.jupiter.api.Assertions.*;

class BannerCacheServiceTest {

    @TempDir
    Path tempDir;

    private BannerCacheService createService(String basePath) {
        return new BannerCacheService(new ImageOptimizer(), basePath);
    }

    private byte[] createTestImage() {
        try {
            BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = img.createGraphics();
            g.setColor(Color.BLUE);
            g.fillRect(0, 0, 100, 100);
            g.dispose();
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            ImageIO.write(img, "jpg", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getBanner_NullPath_ReturnsNull() {
        BannerCacheService service = createService(tempDir.toString());
        assertNull(service.getBanner(null));
    }

    @Test
    void getBanner_BlankPath_ReturnsNull() {
        BannerCacheService service = createService(tempDir.toString());
        assertNull(service.getBanner("  "));
    }

    @Test
    void getBanner_FileNotFound_ReturnsNull() {
        BannerCacheService service = createService(tempDir.toString());
        assertNull(service.getBanner("nonexistent.jpg"));
    }

    @Test
    void getBanner_LocalFileFound_ReturnsImageBytes() throws Exception {
        File imgFile = tempDir.resolve("test.jpg").toFile();
        BufferedImage img = new BufferedImage(555, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, 555, 200);
        g.dispose();
        ImageIO.write(img, "jpg", imgFile);

        BannerCacheService service = createService(tempDir.toString());
        byte[] result = service.getBanner(imgFile.getAbsolutePath());
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    void getBanner_CacheReturnsSameBytes() throws Exception {
        File imgFile = tempDir.resolve("cache-test.jpg").toFile();
        BufferedImage img = new BufferedImage(555, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, 555, 200);
        g.dispose();
        ImageIO.write(img, "jpg", imgFile);

        BannerCacheService service = createService(tempDir.toString());
        byte[] first = service.getBanner(imgFile.getAbsolutePath());
        byte[] second = service.getBanner(imgFile.getAbsolutePath());
        assertNotNull(first);
        assertArrayEquals(first, second);
    }

    @Test
    void clear_EvictsCache() throws Exception {
        File imgFile = tempDir.resolve("clear-test.jpg").toFile();
        BufferedImage img = new BufferedImage(555, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, 555, 200);
        g.dispose();
        ImageIO.write(img, "jpg", imgFile);

        BannerCacheService service = createService(tempDir.toString());
        assertNotNull(service.getBanner(imgFile.getAbsolutePath()));
        service.clear();
        assertNotNull(service.getBanner(imgFile.getAbsolutePath()));
    }

    @Test
    void evict_RemovesSingleEntry() throws Exception {
        File imgFile = tempDir.resolve("evict-test.jpg").toFile();
        BufferedImage img = new BufferedImage(555, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.MAGENTA);
        g.fillRect(0, 0, 555, 200);
        g.dispose();
        ImageIO.write(img, "jpg", imgFile);

        BannerCacheService service = createService(tempDir.toString());
        assertNotNull(service.getBanner(imgFile.getAbsolutePath()));
        service.evict(imgFile.getAbsolutePath());
        assertNotNull(service.getBanner(imgFile.getAbsolutePath()));
    }

    @Test
    void getBannerByArtist_Null_ReturnsNull() {
        BannerCacheService service = createService(tempDir.toString());
        assertNull(service.getBannerByArtist(null));
    }

    @Test
    void getBannerByArtist_Blank_ReturnsNull() {
        BannerCacheService service = createService(tempDir.toString());
        assertNull(service.getBannerByArtist("  "));
    }

    @Test
    void getBannerByArtist_DirNotFound_ReturnsNull() {
        BannerCacheService service = createService("/nonexistent/dir");
        assertNull(service.getBannerByArtist("Artista"));
    }

    @Test
    void getBannerByArtist_FileFound() throws Exception {
        File imgFile = tempDir.resolve("artista.jpg").toFile();
        BufferedImage img = new BufferedImage(555, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.ORANGE);
        g.fillRect(0, 0, 555, 200);
        g.dispose();
        ImageIO.write(img, "jpg", imgFile);

        BannerCacheService service = createService(tempDir.toString());
        byte[] result = service.getBannerByArtist("Artista");
        assertNotNull(result);
    }
}
