package hrms.hrms.core.utilities.pdfHelper;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PatternColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.hrms.entities.concretes.Employer;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class EmployerListPdfHelper {

    static String[] HEADERs = { "First Name", "Last Name", "Company Name", "Web Site", "Web Site Mail", "Phone Number", "Password" , "Job Position" };

    private List<Employer> employerList;

    public EmployerListPdfHelper(List<Employer> employerList) {
        this.employerList = employerList;
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Image image = Image.getInstance((new ClassPathResource("static/image/user-icon.jpg").getFile()).getPath());
        image.setAlignment(Image.ALIGN_CENTER);
        document.add(image);

        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(105);
        table.setWidths(new float[]{ 1.8f, 1.8f, 2f, 2.3f, 3.8f, 2.8f, 1.3f, 2f});
        table.setSpacingBefore(15);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(PatternColor.WHITE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA, 15, BaseColor.RED);

        for (int col = 0; col < HEADERs.length; col++) {
            cell.setPhrase(new Phrase(HEADERs[col], font));
            table.addCell(cell);
        }

        for (Employer employee : employerList) {
            table.addCell(employee.getPerson().getFirstName());
            table.addCell(employee.getPerson().getLastName());
            table.addCell(employee.getCompanyName());
            table.addCell(employee.getWebsite());
            table.addCell(employee.getWebsiteMail());
            table.addCell(employee.getPhoneNo());
            table.addCell(employee.getPassword());
            table.addCell(employee.getPerson().getJobposition());
        }

        document.add(table);
        document.addTitle("Employer PDF işlemleri");

        document.close();

    }
}
