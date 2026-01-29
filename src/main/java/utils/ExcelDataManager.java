package utils;


import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileInputStream;


public class ExcelDataManager {
    DataFormatter formatter=new DataFormatter();
    public Object[][] dataFetch() throws Exception {
        FileInputStream file=new FileInputStream(System.getProperty("user.dir")+"/TestData/TestData Seperated.xlsx");
        XSSFWorkbook wb=new XSSFWorkbook(file);
        XSSFSheet sheet=wb.getSheet("Sheet1");
        int rows=sheet.getLastRowNum();
        int cols=sheet.getRow(0).getLastCellNum(); //5
        Object data[][]=new Object[rows][cols];
        for(int i=1;i<=rows;i++){
            XSSFRow row= sheet.getRow(i);
            for(int j=0;j<cols;j++){
                XSSFCell cell=row.getCell(j);
                data[i-1][j]=formatter.formatCellValue(cell);
            }
        }
        return data;
    }
}
