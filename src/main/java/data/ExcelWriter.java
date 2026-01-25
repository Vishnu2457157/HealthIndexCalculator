
package data;

import models.HealthDataRow;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ExcelWriter {

    public static void writeHealthRows(List<HealthDataRow> rows, String outPath) throws Exception {
        Path path = Path.of(outPath);
        Files.createDirectories(path.getParent());

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("AI-TestData");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("age");
            header.createCell(1).setCellValue("pulseRate");
            header.createCell(2).setCellValue("bloodPressure");

            for (int i = 0; i < rows.size(); i++) {
                HealthDataRow r = rows.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(r.age);
                row.createCell(1).setCellValue(r.pulseRate);
                row.createCell(2).setCellValue(r.bloodPressure);
            }

            for (int c = 0; c < 3; c++) sheet.autoSizeColumn(c);

            try (FileOutputStream fos = new FileOutputStream(outPath)) {
                wb.write(fos);
            }
        }
    }
}
