package com.nullterminators.project.util.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.nullterminators.project.model.Payroll;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("pdfGenerator")
public class PdfGenerator {

  private static final Font COURIER = new Font(Font.FontFamily.COURIER, 20, Font.BOLD);
  private static final Font COURIER_SMALL = new Font(Font.FontFamily.COURIER, 16, Font.BOLD);
  private static final Font COURIER_SMALL_FOOTER = new Font(Font.FontFamily.COURIER, 12, Font.BOLD);

  public void generatePdfReport(Payroll payroll) {
    Document document = new Document();

    try {
      PdfWriter.getInstance(document, new FileOutputStream(getPdfName(payroll)));
      document.open();
      addDocTitle(document, payroll);
      createTable(document, payroll);
      addFooter(document);
      document.close();

    } catch (FileNotFoundException | DocumentException e) {
      e.printStackTrace();
    }
  }

  private void addDocTitle(Document document, Payroll payroll) throws DocumentException {
    final String paymentDate = payroll.getPaymentDate().format(
            DateTimeFormatter.ofPattern("dd MMMM yyyy"));
    Paragraph p1 = new Paragraph();
    leaveEmptyLine(p1, 1);
    p1.add(new Paragraph("Payroll Report", COURIER));
    p1.setAlignment(Element.ALIGN_CENTER);
    leaveEmptyLine(p1, 1);
    p1.add(new Paragraph("Report generated on " + paymentDate, COURIER_SMALL));

    document.add(p1);

  }

  private void createTable(Document document, Payroll payroll) throws DocumentException {
    Paragraph paragraph = new Paragraph();
    leaveEmptyLine(paragraph, 3);
    document.add(paragraph);
    PdfPTable table = new PdfPTable(4);
    List<String> columnNames = new ArrayList<>(Arrays.asList("Employee Id", "Salary",
            "Tax", "Net Salary"));

    for (int i = 0; i < 4; i++) {
      PdfPCell cell = new PdfPCell(new Phrase(columnNames.get(i)));
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setBackgroundColor(BaseColor.CYAN);
      table.addCell(cell);
    }

    table.setHeaderRows(1);
    getDbData(table, payroll);
    document.add(table);
  }

  private void getDbData(PdfPTable table, Payroll payroll) {
    table.setWidthPercentage(100);
    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
    table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

    table.addCell(payroll.getEmployeeId().toString());
    table.addCell("$" + payroll.getSalary().toString());
    table.addCell("$" + payroll.getTax().toString());
    table.addCell("$" + (payroll.getSalary() - payroll.getTax()));
  }

  private void addFooter(Document document) throws DocumentException {
    Paragraph p2 = new Paragraph();
    leaveEmptyLine(p2, 3);
    p2.setAlignment(Element.ALIGN_MIDDLE);
    p2.add(new Paragraph(
        "------------------------ End Of Payroll Report ------------------------",
        COURIER_SMALL_FOOTER));

    document.add(p2);
  }

  private static void leaveEmptyLine(Paragraph paragraph, int number) {
    for (int i = 0; i < number; i++) {
      paragraph.add(new Paragraph(" "));
    }
  }

  private String getPdfName(Payroll payroll) {
    return payroll.getEmployeeId() + "_"
        + payroll.getPaymentDate().getMonthValue() + "_"
        + payroll.getPaymentDate().getYear() + ".pdf";
  }

}
