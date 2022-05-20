package utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Optional;


public final class ZipPdfGenerator {

    private final Paragraph spacing = new Paragraph();

    private static final Font HEADER = new Font(Font.FontFamily.COURIER, 16, Font.BOLD);
    private static final Font SUBJECT = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLDITALIC);
    private static final DottedLineSeparator LINE_SEPARATOR = new DottedLineSeparator();
    private static final String DEST = "target/Zip codes.pdf";
    private static final String DATE = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());


    public void createPdf(Map<String, String> zipCodes) throws FileNotFoundException, DocumentException {
        final Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(DEST));
        document.open();

        final Chunk headerText = new Chunk("Test run : " + DATE, HEADER);
        document.add(headerText);
        spacing.setSpacingAfter(15f);
        document.add(spacing);
        document.add(new Paragraph("Zip code fields length values", SUBJECT));
        document.add(spacing);
        document.add(LINE_SEPARATOR);
        composeSuiteSection(document, zipCodes);
        document.close();
    }

    private void composeSuiteSection(Document document, Map<String, String> zipCodes) {

        zipCodes.forEach((k, v) -> {
                    final String channelName = Optional.ofNullable(k).orElseGet(() -> "Channel name is not provided");
                    final String value = Optional.ofNullable(v).orElseGet(() -> "Zip code field value is not provided");
                    try {
                        document.add(new Paragraph("Channel   : " + channelName));
                        document.add(new Paragraph("Zip value : " + value));
                        document.add(spacing);
                        document.add(LINE_SEPARATOR);
                    }
                    catch (DocumentException e){e.printStackTrace();}
                }
        );
    }
}
