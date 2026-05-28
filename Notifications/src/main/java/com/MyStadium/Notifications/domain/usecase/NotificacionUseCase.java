package com.MyStadium.Notifications.domain.usecase;

import com.MyStadium.Notifications.domain.entity.Notificacion;
import com.MyStadium.Notifications.domain.entity.ReciboCompra;
import com.MyStadium.Notifications.domain.gateway.EnvioEmailGateway;
import com.MyStadium.Notifications.domain.gateway.EnvioPushGateway;
import com.MyStadium.Notifications.pdf.TicketPdfGenerator;
import com.MyStadium.Notifications.pdf.dto.TicketEntry;
import com.MyStadium.Notifications.pdf.dto.TicketPdfData;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.MediaTracker;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.IIOImage;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.util.Iterator;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@RequiredArgsConstructor
public class NotificacionUseCase {
    private final EnvioEmailGateway envioEmailGateway;
    private final EnvioPushGateway envioPushGateway;
    private final JavaMailSender mailSender;
    private final RestTemplate restTemplate;
    private final String mapaCalorUrl;
    private final String catalogoUrl;
    private final TicketPdfGenerator ticketPdfGenerator;
    private final String imagesBasePath;
    private final List<Notificacion> historialNotificaciones = new CopyOnWriteArrayList<>();

    public Notificacion enviarEmailVerificacion(String email, String usuarioId) {
        if (email == null || usuarioId == null)
            throw new IllegalArgumentException("Email y usuarioId son obligatorios");
        boolean enviado = envioEmailGateway.enviarVerificacionEmail(email, usuarioId);
        Notificacion notificacion = Notificacion.builder().usuarioId(usuarioId).tipo("VERIFICATION")
                .mensaje("Email de verificación enviado a " + email).estado(enviado ? "SENT" : "FAILED")
                .fechaCreacion(LocalDateTime.now()).build();
        historialNotificaciones.add(notificacion);
        return notificacion;
    }

    public Notificacion enviarEmailRecuperacion(String email, String enlaceRecuperacion) {
        if (email == null || enlaceRecuperacion == null)
            throw new IllegalArgumentException("Email y enlace de recuperación son obligatorios");
        boolean enviado = envioEmailGateway.enviarRecuperacionEmail(email, enlaceRecuperacion);
        Notificacion notificacion = Notificacion.builder().tipo("PASSWORD_RECOVERY")
                .mensaje("Email de recuperación enviado a " + email).estado(enviado ? "SENT" : "FAILED")
                .fechaCreacion(LocalDateTime.now()).build();
        historialNotificaciones.add(notificacion);
        return notificacion;
    }

    public ReciboCompra enviarReciboCompra(ReciboCompra recibo) {
        if (recibo.getEmailUsuario() == null || recibo.getCodigoOrden() == null)
            throw new IllegalArgumentException("Email y código de orden son obligatorios");
        recibo.setFechaEnvio(LocalDateTime.now());
        boolean emailEnviado = envioEmailGateway.enviarReciboCompra(recibo);
        envioPushGateway.enviarPush(recibo.getUsuarioId(), "Compra confirmada",
                "Tu compra para " + recibo.getNombreConcierto() + " ha sido confirmada. Orden: " + recibo.getCodigoOrden());
        Notificacion notificacion = Notificacion.builder().usuarioId(recibo.getUsuarioId()).tipo("PURCHASE_CONFIRMATION")
                .mensaje("Recibo de compra enviado para orden " + recibo.getCodigoOrden())
                .estado(emailEnviado ? "SENT" : "FAILED").fechaCreacion(LocalDateTime.now()).build();
        historialNotificaciones.add(notificacion);
        return recibo;
    }

    public Map<String, Object> enviarReciboConPdf(List<String> codigos, String usuarioId, String email) {
        try {
            if (codigos == null || codigos.isEmpty())
                throw new RuntimeException("No se proporcionaron códigos de boleto");

            List<Map<String, Object>> boletos = new ArrayList<>();
            for (String codigo : codigos) {
                String url = mapaCalorUrl + "/api/mapa-calor/boleto/" + codigo;
                Map<String, Object> boleto = restTemplate.getForObject(url, Map.class);
                if (boleto == null) throw new RuntimeException("Boleto no encontrado: " + codigo);
                boletos.add(boleto);
            }

            TicketPdfData pdfData = buildTicketPdfData(boletos, usuarioId);

            byte[] pdfBytes = ticketPdfGenerator.generate(pdfData);

            String codigosStr = String.join(", ", codigos);
            enviarEmailConPdf(email, pdfData.getEventName(), pdfData.getArtist(), codigosStr, pdfBytes);

            Notificacion notificacion = Notificacion.builder().usuarioId(usuarioId)
                    .tipo("RECIBO_COMPRA").mensaje("Recibo enviado a " + email + " - " + codigosStr)
                    .estado("SENT").fechaCreacion(LocalDateTime.now())
                    .pdfBytes(pdfBytes).codigoBoleto(codigosStr).build();
            historialNotificaciones.add(notificacion);

            envioPushGateway.enviarPush(usuarioId, "Compra confirmada",
                    "Tu compra para " + pdfData.getEventName() + " ha sido confirmada. " + boletos.size() + " boletos.");

            Map<String, Object> resultado = new HashMap<>();
            resultado.put("mensaje", "Recibo enviado a " + email + " con " + boletos.size() + " boletos");
            resultado.put("codigos", codigos);
            return resultado;
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error al enviar recibo: " + e.getMessage());
            return error;
        }
    }

    public byte[] obtenerPdf(String codigoBoleto) {
        for (Notificacion n : historialNotificaciones) {
            if (n.getPdfBytes() != null && (codigoBoleto.equals(n.getCodigoBoleto())
                    || (n.getCodigoBoleto() != null && n.getCodigoBoleto().contains(codigoBoleto))))
                return n.getPdfBytes();
        }
        try {
            String url = mapaCalorUrl + "/api/mapa-calor/boleto/" + codigoBoleto;
            Map<String, Object> boleto = restTemplate.getForObject(url, Map.class);
            if (boleto == null) return null;
            List<Map<String, Object>> boletos = new ArrayList<>();
            boletos.add(boleto);
            TicketPdfData pdfData = buildTicketPdfData(boletos, (String) boleto.get("usuarioId"));
            return ticketPdfGenerator.generate(pdfData);
        } catch (Exception e) {
            return null;
        }
    }

    public byte[] obtenerPdfMultiple(List<String> codigos) {
        try {
            List<Map<String, Object>> boletos = new ArrayList<>();
            for (String codigo : codigos) {
                String url = mapaCalorUrl + "/api/mapa-calor/boleto/" + codigo;
                Map<String, Object> boleto = restTemplate.getForObject(url, Map.class);
                if (boleto == null) continue;
                boletos.add(boleto);
            }
            if (boletos.isEmpty()) return null;
            TicketPdfData pdfData = buildTicketPdfData(boletos, (String) boletos.get(0).get("usuarioId"));
            return ticketPdfGenerator.generate(pdfData);
        } catch (Exception e) {
            return null;
        }
    }

    private TicketPdfData buildTicketPdfData(List<Map<String, Object>> boletos, String usuarioId) {
        Map<String, Object> first = boletos.get(0);

        String conciertoNombre = (String) first.getOrDefault("conciertoNombre", "Concierto");
        String artista = (String) first.getOrDefault("artista", "Artista");
        String conciertoId = (String) first.getOrDefault("conciertoId", "");

        String orderCode = "ORD-" + boletos.get(0).get("codigoUnico");
        if (orderCode.length() > 15) orderCode = orderCode.substring(0, 15);

        String firstFecha = (String) first.getOrDefault("fechaCompra", "");
        LocalDateTime fechaEvento;
        try {
            fechaEvento = LocalDateTime.parse(firstFecha.length() > 10 ? firstFecha : firstFecha + "T20:00:00");
        } catch (Exception e) {
            fechaEvento = LocalDateTime.of(LocalDate.now(), LocalTime.of(20, 0));
        }

        byte[] banner = fetchBanner(artista);

        double totalCompra = 0;
        List<TicketEntry> tickets = new ArrayList<>();
        for (int i = 0; i < boletos.size(); i++) {
            Map<String, Object> b = boletos.get(i);
            String codigo = (String) b.getOrDefault("codigoUnico", "");
            double precio = (Double) b.getOrDefault("totalPagado", 0.0);
            totalCompra += precio;

            tickets.add(TicketEntry.builder()
                    .zona((String) b.getOrDefault("zonaNombre", "Zona"))
                    .asiento((String) b.getOrDefault("asiento", "N/A"))
                    .codigoUnico(codigo)
                    .qrUrl("https://mystadium.co/ticket/" + codigo)
                    .precio(BigDecimal.valueOf(precio))
                    .tipoEntrada("GENERAL")
                    .index(i)
                    .total(boletos.size())
                    .build());
        }

        return TicketPdfData.builder()
                .eventName(conciertoNombre)
                .artist(artista)
                .stadium("")
                .fechaEvento(fechaEvento)
                .orderCode(orderCode)
                .userName(usuarioId != null ? usuarioId : "")
                .eventBanner(banner)
                .tickets(tickets)
                .totalPaid(totalCompra)
                .build();
    }

    private byte[] fetchBanner(String artista) {
        if (artista == null || artista.isBlank()) return null;
        try {
            File dir = new File(imagesBasePath);
            if (!dir.isDirectory()) {
                System.err.println("[BANNER] Directory not found: " + imagesBasePath);
                return null;
            }
            String searchKey = artista.toLowerCase().replaceAll("\\s+", "");
            File matched = null;
            for (File f : dir.listFiles()) {
                if (f.isFile() && f.getName().toLowerCase().contains(searchKey)) {
                    matched = f;
                    break;
                }
            }
            if (matched == null) {
                System.err.println("[BANNER] No image found for artist: " + artista);
                return null;
            }
            BufferedImage original = readImageFallback(matched);
            if (original == null) {
                System.err.println("[BANNER] Could not decode image: " + matched.getName());
                return null;
            }
            int targetW = 555;
            float targetH = com.MyStadium.Notifications.pdf.components.Spacing.BANNER_HEIGHT;
            BufferedImage resized = new BufferedImage(targetW, (int) targetH, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = resized.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(original, 0, 0, targetW, (int) targetH, null);
            g2d.dispose();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
            writer.setOutput(ImageIO.createImageOutputStream(baos));
            writer.write(null, new IIOImage(resized, null, null), null);
            writer.dispose();
            byte[] result = baos.toByteArray();
            System.out.println("[BANNER] Loaded " + matched.getName() + " -> " + result.length + " bytes");
            return result;
        } catch (Exception e) {
            System.err.println("[BANNER] Error: " + e.getMessage());
        }
        return null;
    }

    private BufferedImage readImageFallback(File file) {
        try {
            BufferedImage img = ImageIO.read(file);
            if (img != null) return img;
        } catch (Exception ignored) {}

        try (ImageInputStream iis = ImageIO.createImageInputStream(file)) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            while (readers.hasNext()) {
                ImageReader reader = readers.next();
                try {
                    reader.setInput(iis, false, true);
                    BufferedImage result = reader.read(0);
                    if (result != null) return result;
                } catch (Exception ignored) {
                } finally {
                    reader.dispose();
                }
            }
        } catch (Exception ignored) {}

        try {
            Image awtImg = Toolkit.getDefaultToolkit().createImage(file.toURI().toURL());
            MediaTracker tracker = new MediaTracker(new java.awt.Canvas());
            tracker.addImage(awtImg, 0);
            tracker.waitForID(0, 5000);
            if (awtImg.getWidth(null) > 0) {
                BufferedImage result = new BufferedImage(
                        awtImg.getWidth(null), awtImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
                Graphics2D g = result.createGraphics();
                g.drawImage(awtImg, 0, 0, null);
                g.dispose();
                return result;
            }
        } catch (Exception ignored) {}

        return null;
    }

    private void enviarEmailConPdf(String to, String concierto, String artista,
                                   String codigosStr, byte[] pdfBytes) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Tu recibo de compra - MyStadium");
            helper.setText("<h2>MyStadium</h2>"
                    + "<p><strong>" + concierto + "</strong> - " + artista + "</p>"
                    + "<p>Códigos: " + codigosStr + "</p>"
                    + "<p>Gracias por tu compra. Adjuntamos el recibo en PDF con todos tus boletos.</p>", true);
            helper.addAttachment("recibo-mystadium.pdf", new ByteArrayResource(pdfBytes));
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error enviando email: " + e.getMessage());
        }
    }

    public List<Notificacion> obtenerHistorialNotificaciones(String usuarioId) {
        List<Notificacion> resultado = new ArrayList<>();
        for (Notificacion n : historialNotificaciones) {
            if (usuarioId.equals(n.getUsuarioId())) resultado.add(n);
        }
        return resultado;
    }
}
