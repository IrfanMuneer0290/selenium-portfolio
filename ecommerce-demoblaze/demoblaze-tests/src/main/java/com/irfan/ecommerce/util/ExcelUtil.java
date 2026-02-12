package com.irfan.ecommerce.util;

import java.io.FileInputStream;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

    public Object[][] readExcelTestData(String sheetname)
    {
        Object[][] data = null;
        FileInputStream fis = null;
        XSSFWorkbook workbook = null;
        try {
             fis = new FileInputStream("null");
             workbook = new XSSFWorkbook(fis);
             XSSFSheet sheet = workbook.getSheet(sheetname);


             int rowCount = sheet.getLastRowNum();
             int cellCount = sheet.getRow(0).getLastCellNum();

             data = new Object[rowCount][cellCount];

             for(int i=0; i< rowCount; i++)
             {
                for(int j=0; j<cellCount; j++)
                {
                    data[i-1][j] = sheet.getRow(i).getCell(j).toString();
                }
                
             }

            
        } catch (Exception e) {
           System.err.println("Error reading Excel: " + e.getMessage());
        } 
        
        finally
        {
           try {
            if (fis != null) fis.close();
            if (workbook != null) workbook.close(); // Use lowercase 'workbook'
        } catch (Exception e) {
            e.printStackTrace();
        }
        }

         return data;
    }
    
}
