package by.dziashko.frm.backend.service.utilities;

import by.dziashko.frm.backend.entity.productionOrder.ProductionOrder;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;


@Service
public class PdfReportService {

    private static final Logger logger = LoggerFactory.getLogger(PdfReportService.class);
    private BaseFont bf;
    private ProductionOrder productionOrder;

    public PdfReportService() throws DocumentException, IOException {
        createFont();
    }

    public ByteArrayInputStream generatePdfReport(ProductionOrder productionOrder) {
        this.productionOrder = productionOrder;

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(document, out);
            document.open();
            addMetaData(document);
            addCabinPage(document);
            if (!productionOrder.getAspiratorType().equals("")) {
                addAspiratorPage(document);
            }

            document.close();

        } catch (DocumentException | IOException ex) {
            logger.error("Error occurred: {0}", ex);
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    // iText allows to add metadata to the PDF which can be viewed in your Adobe
    // Reader
    // under File -> Properties
    private static void addMetaData(Document document) {
        document.addTitle("PDF");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("AD");
        document.addCreator("AD");
    }

    private void createFont() throws DocumentException, IOException {
        bf = BaseFont.createFont("FallingSky-JKwK.otf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    }

    private void addCabinPage(Document document) throws DocumentException, IOException {

        PdfPTable tableOuter = new PdfPTable(1);
        tableOuter.setWidthPercentage(100);
        tableOuter.addCell(createOuterPdfCell("Odbiorca:", productionOrder.getClient(), 50, 60));
        if (productionOrder.getProjectNumber().equals("")) {
            tableOuter.addCell(createOuterPdfCell("Numer zamówienia:", productionOrder.getOrderNumber(), 40, 50));
        } else {
            tableOuter.addCell(createOuterPdfCell("Numer zamówienia:", productionOrder.getProjectNumber(), 40, 50));
        }
        tableOuter.addCell(createOuterPdfCell("Termin:", productionOrder.getOrderDeadLine(), 50, 60));
        tableOuter.addCell(createOuterPdfCell("Kabina:", productionOrder.getCabinType(), 30, 40));
        tableOuter.addCell(createOuterPdfCellNoCentering("Informacje dodatkowe:", productionOrder.getAdditionalOptions(), 20, 100));
        tableOuter.addCell(createOuterPdfCell("Uwagi:", "", 25, 300));

        document.add(tableOuter);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();

        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);
        preface.add(new Paragraph(
                "Raport wygenerował użytkownik: " + username + ".  Data: " + new Date(),
                new Font(bf, 10)));

        document.add(preface);
        document.newPage();
    }

    private void addAspiratorPage(Document document) throws DocumentException, IOException {

        PdfPTable tableOuter = new PdfPTable(1);
        tableOuter.setWidthPercentage(100);
        tableOuter.addCell(createOuterPdfCell("Odbiorca:", productionOrder.getClient(), 50, 60));
        if (productionOrder.getProjectNumber().equals("")) {
            tableOuter.addCell(createOuterPdfCell("Numer zamówienia:", productionOrder.getOrderNumber(), 40, 50));
        } else {
            tableOuter.addCell(createOuterPdfCell("Numer zamówienia:", productionOrder.getProjectNumber(), 40, 50));
        }
        tableOuter.addCell(createOuterPdfCell("Termin:", productionOrder.getOrderDeadLine(), 50, 60));
        tableOuter.addCell(createOuterPdfCell("Odciąg:", productionOrder.getAspiratorType(), 30, 40));
        tableOuter.addCell(createOuterPdfCell("Uwagi:", "", 25, 400));

        document.add(tableOuter);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();

        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);
        preface.add(new Paragraph(
                "Raport wygenerował użytkownik: " + username + ".  Data: " + new Date(),
                new Font(bf, 10)));

        document.add(preface);
        document.newPage();
    }

    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private PdfPCell createOuterPdfCell(String phrase1, String phrase2, int fontSize, int cellHeight) {

        Phrase ph1 = new Phrase(phrase1, new Font(bf, 8));
        Phrase ph2 = new Phrase(phrase2, new Font(bf, fontSize));

        PdfPCell innerCell1 = new PdfPCell();
        innerCell1.setPhrase(ph1);
        innerCell1.setBorder(Rectangle.NO_BORDER);
        innerCell1.setVerticalAlignment(Element.ALIGN_TOP);
        innerCell1.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell innerCell2 = new PdfPCell();
        innerCell2.setPhrase(ph2);
        innerCell2.setBorder(Rectangle.NO_BORDER);
        innerCell2.setVerticalAlignment(Element.ALIGN_BOTTOM);
        innerCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        innerCell2.setFixedHeight(cellHeight);

        PdfPTable tableInner = new PdfPTable(1);

        tableInner.addCell(innerCell1);
        tableInner.addCell(innerCell2);

        PdfPCell outerCell = new PdfPCell(tableInner);

        return outerCell;
    }

    private PdfPCell createOuterPdfCellNoCentering(String phrase1, String phrase2, int size, int height) {

        Phrase ph1 = new Phrase(phrase1, new Font(bf, 8));
        Phrase ph2 = new Phrase(phrase2, new Font(bf, size));

        PdfPCell innerCell1 = new PdfPCell();
        innerCell1.setPhrase(ph1);
        innerCell1.setBorder(Rectangle.NO_BORDER);
        innerCell1.setVerticalAlignment(Element.ALIGN_TOP);
        innerCell1.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell innerCell2 = new PdfPCell();
        innerCell2.setPhrase(ph2);
        innerCell2.setBorder(Rectangle.NO_BORDER);
        innerCell2.setVerticalAlignment(Element.ALIGN_BASELINE);
        //innerCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        innerCell2.setFixedHeight(height);

        PdfPTable tableInner = new PdfPTable(1);

        tableInner.addCell(innerCell1);
        tableInner.addCell(innerCell2);

        PdfPCell outerCell = new PdfPCell(tableInner);

        return outerCell;
    }

}
