package com.MyStadium.Notifications.pdf.renderers;

import com.MyStadium.Notifications.pdf.TicketCanvas;
import com.MyStadium.Notifications.pdf.components.Colors;
import com.MyStadium.Notifications.pdf.components.Spacing;
import com.MyStadium.Notifications.pdf.components.Typography;
import com.MyStadium.Notifications.pdf.dto.TicketEntry;
import com.MyStadium.Notifications.pdf.dto.TicketPdfData;
import com.lowagie.text.Image;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Component;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;

@Component
public class QrRenderer {

    public void render(TicketCanvas canvas, TicketPdfData data, TicketEntry entry) {
        float stubCenterX = Spacing.STUB_RIGHT + (Spacing.PAGE_WIDTH - Spacing.MARGIN - Spacing.STUB_RIGHT) / 2;
        float qrX = stubCenterX - Spacing.QR_SIZE / 2;
        float qrY = Spacing.BANNER_BOTTOM - 18 - Spacing.QR_SIZE;

        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(entry.getQrUrl(), BarcodeFormat.QR_CODE,
                    180, 180, new HashMap<>() {{
                        put(com.google.zxing.EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
                        put(com.google.zxing.EncodeHintType.MARGIN, 0);
                    }});

            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(matrix);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "png", baos);
            Image qr = Image.getInstance(baos.toByteArray());
            qr.scaleAbsolute(Spacing.QR_SIZE, Spacing.QR_SIZE);
            qr.setAbsolutePosition(qrX, qrY);
            canvas.getCb().addImage(qr);

            canvas.drawText("Escanear", stubCenterX - 15, qrY - 9, Typography.regular(6f, Colors.TEXT_MUTED));
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }
}
