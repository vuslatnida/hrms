package hrms.hrms.core.utilities.excelHelper;

import hrms.hrms.entities.concretes.JobSeekers;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JobSeekerListExcelHelper {

    static String[] HEADERs = { "First Name", "Last Name", "Identification Number", "Birth Year", "e - Mail", "Password" , "Job Position"};

    static String SHEET = "JobSeekers";

    private Workbook workbook;

    private Sheet sheet;

    private List<JobSeekers> jobSeekers;

    public JobSeekerListExcelHelper(List<JobSeekers> jobSeekers) {
        this.jobSeekers = jobSeekers;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet(SHEET);
    }

    public void export(HttpServletResponse response) throws IOException {

        Row headerRow = sheet.createRow(0);

        CellStyle headerCellStyle = workbook.createCellStyle();
        XSSFFont headerFontStyle = (XSSFFont) workbook.createFont();
        headerFontStyle.setBold(true);
        headerFontStyle.setFontHeight(15);
        headerFontStyle.setColor(Font.COLOR_RED);
        headerCellStyle.setFont(headerFontStyle);

        for (int col = 0; col < HEADERs.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(HEADERs[col]);
            cell.setCellStyle(headerCellStyle);
        }

        int rowIdx = 1;
        CellStyle rowCellStyle = workbook.createCellStyle();
        XSSFFont rowFontStyle = (XSSFFont) workbook.createFont();
        rowFontStyle.setFontHeight(13);
        rowCellStyle.setFont(rowFontStyle);
        rowFontStyle.setColor(IndexedColors.DARK_YELLOW.getIndex());

        for (JobSeekers jobSeeker : jobSeekers) {
            Row dataRow = sheet.createRow(rowIdx++);

            Cell cell = dataRow.createCell(0);
            cell.setCellValue(jobSeeker.getPerson().getFirstName());
            sheet.autoSizeColumn(0);
            cell.setCellStyle(rowCellStyle);

            cell = dataRow.createCell(1);
            cell.setCellValue(jobSeeker.getPerson().getLastName());
            sheet.autoSizeColumn(1);
            cell.setCellStyle(rowCellStyle);

            cell = dataRow.createCell(2);
            cell.setCellValue(jobSeeker.getIdentificationNo());
            sheet.autoSizeColumn(2);
            cell.setCellStyle(rowCellStyle);

            cell = dataRow.createCell(3);
            cell.setCellValue(jobSeeker.getBirthYear());
            sheet.autoSizeColumn(3);
            cell.setCellStyle(rowCellStyle);

            cell = dataRow.createCell(4);
            cell.setCellValue(jobSeeker.getEmail());
            sheet.autoSizeColumn(4);
            cell.setCellStyle(rowCellStyle);

            cell = dataRow.createCell(5);
            cell.setCellValue(jobSeeker.getPassword());
            sheet.autoSizeColumn(5);
            cell.setCellStyle(rowCellStyle);

            cell = dataRow.createCell(6);
            cell.setCellValue(jobSeeker.getPerson().getJobposition());
            sheet.autoSizeColumn(6);
            cell.setCellStyle(rowCellStyle);
        }

        ServletOutputStream outputStream =  response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
