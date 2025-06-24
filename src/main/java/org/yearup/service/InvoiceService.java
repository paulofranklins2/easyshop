package org.yearup.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import org.yearup.model.Order;
import org.yearup.model.OrderLineItem;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

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
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Paragraph title = new Paragraph("Invoice #" + order.getOrderId(), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("Date: " + order.getDate().toLocalDate()));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{5, 2, 1, 2});
            table.addCell("Product");
            table.addCell("Price");
            table.addCell("Qty");
            table.addCell("Subtotal");

            for (OrderLineItem item : order.getItems()) {
                table.addCell(item.getProduct().getName());
                table.addCell("$" + item.getSalesPrice());
                table.addCell(String.valueOf(item.getQuantity()));
                table.addCell("$" + item.getLineTotal());
            }
            document.add(table);

            if (order.getPromoCode() != null) {
                BigDecimal percent = order.getDiscountPercent().multiply(new BigDecimal("100"));
                document.add(new Paragraph("Promo Code: " + order.getPromoCode() + " (" + percent.stripTrailingZeros().toPlainString() + "% off)"));
                document.add(new Paragraph("Discount: -$" + order.getDiscountTotal()));
            }
            document.add(new Paragraph("Total: $" + order.getTotal()));
        } catch (DocumentException ex) {
            throw new RuntimeException("Failed to generate invoice", ex);
        } finally {
            document.close();
        }
        return out.toByteArray();
    }
}
