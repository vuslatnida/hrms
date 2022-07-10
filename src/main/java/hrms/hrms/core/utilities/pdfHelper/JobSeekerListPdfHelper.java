package hrms.hrms.core.utilities.pdfHelper;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PatternColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.hrms.entities.concretes.JobSeekers;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JobSeekerListPdfHelper {

    static String[] HEADER1 = { "First Name", "Last Name", "Identification Number", "Birth Year", "e - Mail", "Password" , "Job Position"};

    static String[] HEADER2 = { "e - Mail", "Password" };

    private List<JobSeekers> jobSeekersList;

    public JobSeekerListPdfHelper(List<JobSeekers> jobSeekersList) {
        this.jobSeekersList = jobSeekersList;
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Image image = Image.getInstance((new ClassPathResource("static/image/user-icon.jpg").getFile()).getPath());
        image.setAlignment(Image.ALIGN_CENTER);
        document.add(image);


        PdfPTable table1 = new PdfPTable(7);
        table1.setWidthPercentage(105);
        table1.setWidths(new float[]{ 1.8f, 1.8f, 2f, 2.3f, 3.8f, 2.8f, 1.3f});
        table1.setSpacingBefore(15);

        PdfPTable table2 = new PdfPTable(2);
        table2.setWidthPercentage(105);
        table2.setWidths(new float[]{ 3f, 3f});
        table2.setSpacingBefore(15);


        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(PatternColor.WHITE);
        cell.setPadding(5);


        Font font1 = FontFactory.getFont(FontFactory.HELVETICA, 15, BaseColor.RED);

        Font font2 = FontFactory.getFont(FontFactory.HELVETICA, 15, BaseColor.BLUE);


        for (int col = 0; col < HEADER1.length; col++) {
            cell.setPhrase(new Phrase(HEADER1[col], font1));
            table1.addCell(cell);
        }

        for (int col = 0; col < HEADER2.length; col++) {
            cell.setPhrase(new Phrase(HEADER2[col], font2));
            table2.addCell(cell);
        }


        for (JobSeekers jobSeeker : jobSeekersList) {
            table1.addCell(jobSeeker.getPerson().getFirstName());
            table1.addCell(jobSeeker.getPerson().getLastName());
            table1.addCell(jobSeeker.getIdentificationNo());
            table1.addCell(jobSeeker.getBirthYear());
            table1.addCell(jobSeeker.getEmail());
            table1.addCell(jobSeeker.getPassword());
            table1.addCell(jobSeeker.getSystemPersonnel().getJobposition());

            table2.addCell(jobSeeker.getEmail());
            table2.addCell(jobSeeker.getPassword());
        }


        document.add(table1);
        document.addTitle("JobSeekers PDF işlemleri");

        document.newPage();

        document.add(table2);

        document.close();
    }
}
