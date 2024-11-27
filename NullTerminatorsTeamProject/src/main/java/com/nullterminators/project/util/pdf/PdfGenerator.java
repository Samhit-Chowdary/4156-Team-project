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
import com.nullterminators.project.model.EmployeeProfile;
import com.nullterminators.project.model.Payroll;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Pdf Generator class.
 */
@Component("pdfGenerator")
public class PdfGenerator {

  private static final Font COURIER = new Font(Font.FontFamily.COURIER, 20, Font.BOLD);
  private static final Font COURIER_SMALL = new Font(Font.FontFamily.COURIER, 16, Font.BOLD);
  private static final Font COURIER_SMALL_FOOTER = new Font(Font.FontFamily.COURIER, 12, Font.BOLD);

  /**
   * Generate Pdf Report based on the payroll details.
   *
   * @param payroll (Payroll) : Payroll details
   */
  public void generatePdfReport(Payroll payroll, EmployeeProfile employee, Integer leaveCount) {
    Document document = new Document();

    try {
      PdfWriter.getInstance(document, new FileOutputStream(getPdfName(payroll)));
      document.open();
      addDocTitle(document, payroll, employee, leaveCount);
      createTable(document, payroll);
      addFooter(document);
      document.close();

    } catch (FileNotFoundException | DocumentException e) {
      e.printStackTrace();
    }
  }

  private void addDocTitle(Document document, Payroll payroll, EmployeeProfile employee,
                           Integer leaveCount) throws DocumentException {
    final String paymentDate = payroll.getPaymentDate().format(
            DateTimeFormatter.ofPattern("dd MMMM yyyy"));
    PdfPTable table = new PdfPTable(1);
    table.setWidthPercentage(100);
    PdfPCell cell = new PdfPCell();
    cell.setBorderWidth(2);
    cell.setPadding(10);

    Paragraph p1Title = new Paragraph("Payroll Report", COURIER);
    p1Title.setAlignment(Element.ALIGN_CENTER);
    leaveEmptyLine(p1Title, 1);
    cell.addElement(p1Title);
    table.addCell(cell);
    document.add(table);

    Paragraph p1Space = new Paragraph();
    leaveEmptyLine(p1Space, 3);
    document.add(p1Space);

    table = new PdfPTable(1);
    table.setWidthPercentage(100);
    cell = new PdfPCell();
    cell.setBorderWidth(2);
    cell.setPadding(10);

    Paragraph p1 = new Paragraph();
    leaveEmptyLine(p1, 1);
    p1.add(new Paragraph("Report generated on " + paymentDate, COURIER_SMALL));
    leaveEmptyLine(p1, 2);
    p1.add(new Paragraph("Name: " + employee.getName(), COURIER_SMALL_FOOTER));
    leaveEmptyLine(p1, 1);
    p1.add(new Paragraph("Phone Number: " + employee.getPhoneNumber(), COURIER_SMALL_FOOTER));
    leaveEmptyLine(p1, 1);
    p1.add(new Paragraph("Designation: " + employee.getDesignation(), COURIER_SMALL_FOOTER));
    leaveEmptyLine(p1, 1);
    p1.add(new Paragraph("Email: " + employee.getEmail(), COURIER_SMALL_FOOTER));
    leaveEmptyLine(p1, 1);
    p1.add(new Paragraph("Emergency Contact: " + employee.getEmergencyContactNumber(),
            COURIER_SMALL_FOOTER));
    leaveEmptyLine(p1, 1);
    p1.add(new Paragraph("Base Salary: " + employee.getBaseSalary(), COURIER_SMALL_FOOTER));
    leaveEmptyLine(p1, 1);
    p1.add(new Paragraph("Leave Count: " + leaveCount, COURIER_SMALL_FOOTER));
    leaveEmptyLine(p1, 1);
    cell.addElement(p1);
    table.addCell(cell);
    document.add(table);
  }

  private void createTable(Document document, Payroll payroll) throws DocumentException {
    Paragraph paragraph = new Paragraph();
    leaveEmptyLine(paragraph, 3);
    document.add(paragraph);
    PdfPTable table = new PdfPTable(4);
    List<String> columnNames = new ArrayList<>(Arrays.asList("Employee ID", "Current Month Salary",
            "Tax", "Net Salary"));

    for (int i = 0; i < 4; i++) {
      PdfPCell cell = new PdfPCell(new Phrase(columnNames.get(i)));
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setBackgroundColor(BaseColor.YELLOW);
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

  /**
   * Get the name of the pdf file from the payroll.
   *
   * @param payroll Payroll
   * @return String
   */
  public String getPdfName(Payroll payroll) {
    return payroll.getEmployeeId() + "_"
        + payroll.getPaymentDate().getMonthValue() + "_"
        + payroll.getPaymentDate().getYear() + ".pdf";
  }

}
