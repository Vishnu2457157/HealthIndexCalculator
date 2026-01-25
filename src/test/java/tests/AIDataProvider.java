package tests;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

import java.io.FileInputStream;

public class AIDataProvider {

    @DataProvider(name = "aiData")
    public Object[][] aiData() throws Exception {
        String path = "src/test/resources/testdata/AI_TestData.xlsx";
        try (FileInputStream fis = new FileInputStream(path);
             Workbook wb = new XSSFWorkbook(fis)) {
            Sheet s = wb.getSheetAt(0);
            int rows = s.getPhysicalNumberOfRows() - 1;
            Object[][] data = new Object[rows][3];
            for (int i = 1; i <= rows; i++) {
                Row r = s.getRow(i);
                data[i-1][0] = (int) r.getCell(0).getNumericCellValue(); // age
                data[i-1][1] = (int) r.getCell(1).getNumericCellValue(); // pulseRate
                data[i-1][2] = (int) r.getCell(2).getNumericCellValue(); // bloodPressure
            }
            return data;
        }
    }
}

