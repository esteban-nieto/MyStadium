package com.MyStadium.Notifications.pdf.renderers;

import com.MyStadium.Notifications.pdf.TicketCanvas;
import com.MyStadium.Notifications.pdf.components.Colors;
import com.MyStadium.Notifications.pdf.components.Spacing;
import com.MyStadium.Notifications.pdf.components.Typography;
import com.MyStadium.Notifications.pdf.dto.TicketEntry;
import com.MyStadium.Notifications.pdf.dto.TicketPdfData;
import com.lowagie.text.Image;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import org.springframework.stereotype.Component;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@Component
public class BarcodeRenderer {

    public void render(TicketCanvas canvas, TicketPdfData data, TicketEntry entry) {
        float stubCenterX = Spacing.STUB_RIGHT + (Spacing.PAGE_WIDTH - Spacing.MARGIN - Spacing.STUB_RIGHT) / 2;
        float barcodeX = stubCenterX - Spacing.BARCODE_WIDTH / 2;
        float qrY = Spacing.BANNER_BOTTOM - 18 - Spacing.QR_SIZE;
        float barcodeY = qrY - Spacing.QR_BARCODE_GAP - Spacing.BARCODE_HEIGHT;

        try {
            Code128Writer writer = new Code128Writer();
            BitMatrix matrix = writer.encode(entry.getCodigoUnico(), BarcodeFormat.CODE_128,
                    (int) Spacing.BARCODE_WIDTH * 2, (int) Spacing.BARCODE_HEIGHT * 2);

            BufferedImage barcodeImage = MatrixToImageWriter.toBufferedImage(matrix);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(barcodeImage, "png", baos);
            Image barcode = Image.getInstance(baos.toByteArray());
            barcode.scaleAbsolute(Spacing.BARCODE_WIDTH, Spacing.BARCODE_HEIGHT);
            barcode.setAbsolutePosition(barcodeX, barcodeY);
            canvas.getCb().addImage(barcode);

            canvas.drawText(entry.getCodigoUnico(), stubCenterX - 28, barcodeY - 10,
                    Typography.regular(6f, Colors.TEXT_MUTED));
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate barcode", e);
        }
    }
}
