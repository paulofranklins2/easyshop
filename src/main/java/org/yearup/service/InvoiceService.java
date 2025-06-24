package org.yearup.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.yearup.model.Order;
import org.yearup.model.OrderLineItem;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Generates PDF invoices for orders.
 */
@Service
public class InvoiceService {
    /**
     * Create a PDF invoice document for the provided order.
     *
     * @param order the order to render
     * @return PDF bytes
     */
    public byte[] generateInvoice(Order order) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            // --- Background matching Mario theme ---
            PdfContentByte canvas = writer.getDirectContentUnder();
            Rectangle pageSize = document.getPageSize();
            canvas.setColorFill(new Color(0x5c, 0x94, 0xfc));
            canvas.rectangle(0, 0, pageSize.getWidth(), pageSize.getHeight());
            canvas.fill();

            String tileBase64 = "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQAQMAAAAlPW0iAAAAIGNIUk0AAHomAACAhAAA+gAAAIDoAAB1MAAA6mAAADqYAAAXcJy6UTwAAAAGUExURVyU/P///zajf54AAAABYktHRAH/Ai3eAAAAB3RJTUUH6QYXFRwXwFjDbQAAABNJREFUCNdjYEAC8jCEADUwBAUAHrQB0oWr4tkAAAAASUVORK5CYII=";
            byte[] tileBytes = Base64.getDecoder().decode(tileBase64);
            Image tileImg = Image.getInstance(tileBytes);
            tileImg.scaleAbsolute(16, 16);
            for (float x = 0; x < pageSize.getWidth(); x += 16) {
                for (float y = 0; y < pageSize.getHeight(); y += 16) {
                    canvas.addImage(tileImg, 16, 0, 0, 16, x, y);
                }
            }

            // --- Fonts ---
            BaseFont pressStart = BaseFont.createFont(new ClassPathResource("fonts/PressStart2P-Regular.ttf").getPath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            BaseFont vt323 = BaseFont.createFont(new ClassPathResource("fonts/VT323-Regular.ttf").getPath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font headerFont = new Font(pressStart, 12);
            Font bodyFont = new Font(vt323, 12);

            // --- Banner header ---
            ClassPathResource bannerResource = new ClassPathResource("invoice.txt");
            String banner = new String(bannerResource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            Paragraph bannerParagraph = new Paragraph(banner, bodyFont);
            bannerParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(bannerParagraph);

            Paragraph title = new Paragraph("Invoice #" + order.getOrderId(), headerFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("Date: " + order.getDate().toLocalDate(), bodyFont));
            document.add(new Paragraph(" ", bodyFont));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{5, 2, 1, 2});
            table.addCell(new Phrase("Product", headerFont));
            table.addCell(new Phrase("Price", headerFont));
            table.addCell(new Phrase("Qty", headerFont));
            table.addCell(new Phrase("Subtotal", headerFont));

            for (OrderLineItem item : order.getItems()) {
                table.addCell(new Phrase(item.getProduct().getName(), bodyFont));
                table.addCell(new Phrase("$" + item.getSalesPrice(), bodyFont));
                table.addCell(new Phrase(String.valueOf(item.getQuantity()), bodyFont));
                table.addCell(new Phrase("$" + item.getLineTotal(), bodyFont));
            }
            document.add(table);

            if (order.getPromoCode() != null) {
                BigDecimal percent = order.getDiscountPercent().multiply(new BigDecimal("100"));
                document.add(new Paragraph("Promo Code: " + order.getPromoCode() + " (" + percent.stripTrailingZeros().toPlainString() + "% off)", bodyFont));
                document.add(new Paragraph("Discount: -$" + order.getDiscountTotal(), bodyFont));
            }
            document.add(new Paragraph("Total: $" + order.getTotal(), headerFont));
        } catch (DocumentException | IOException ex) {
            throw new RuntimeException("Failed to generate invoice", ex);
        } finally {
            document.close();
        }
        return out.toByteArray();
    }
}
