package hrms.hrms.core.utilities.excelHelper;

import java.util.List;
import java.io.IOException;

import hrms.hrms.entities.concretes.Employer;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public class EmployerListExcelHelper {

    static String[] HEADERs = { "First Name", "Last Name", "Company Name", "Web Site", "Web Site Mail", "Phone Number", "Password" , "Job Position"};

    static String SHEET = "Employers";

    private Workbook workbook;

    private Sheet sheet;

    private List<Employer> employers;

    public EmployerListExcelHelper(List<Employer> employers) {
        this.employers = employers;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet(SHEET);
    }

    public void export(HttpServletResponse response) throws IOException {

        Row headerRow = sheet.createRow(0);

        CellStyle headerCellStyle = workbook.createCellStyle();
        XSSFFont headerFontStyle = (XSSFFont) workbook.createFont();
        headerFontStyle.setBold(true);
        headerFontStyle.setFontHeight(16);
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
        rowFontStyle.setColor(IndexedColors.BLUE.getIndex());
        rowCellStyle.setFont(rowFontStyle);

        for (Employer employee : employers) {
            Row dataRow = sheet.createRow(rowIdx++);

            Cell cell = dataRow.createCell(0);
            cell.setCellValue(employee.getPerson().getFirstName());
            sheet.autoSizeColumn(0);
            cell.setCellStyle(rowCellStyle);

            cell = dataRow.createCell(1);
            cell.setCellValue(employee.getPerson().getLastName());
            sheet.autoSizeColumn(1);
            cell.setCellStyle(rowCellStyle);

            cell = dataRow.createCell(2);
            cell.setCellValue(employee.getCompanyName());
            sheet.autoSizeColumn(2);
            cell.setCellStyle(rowCellStyle);

            cell = dataRow.createCell(3);
            cell.setCellValue(employee.getWebsite());
            sheet.autoSizeColumn(3);
            cell.setCellStyle(rowCellStyle);

            cell = dataRow.createCell(4);
            cell.setCellValue(employee.getWebsiteMail());
            sheet.autoSizeColumn(4);
            cell.setCellStyle(rowCellStyle);

            cell = dataRow.createCell(5);
            cell.setCellValue(employee.getPhoneNo());
            sheet.autoSizeColumn(5);
            cell.setCellStyle(rowCellStyle);

            cell = dataRow.createCell(6);
            cell.setCellValue(employee.getPassword());
            sheet.autoSizeColumn(6);
            cell.setCellStyle(rowCellStyle);

            cell = dataRow.createCell(7);
            cell.setCellValue(employee.getSystemPersonnel().getJobposition());
            sheet.autoSizeColumn(7);
            cell.setCellStyle(rowCellStyle);
        }

        ServletOutputStream outputStream =  response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }
}
