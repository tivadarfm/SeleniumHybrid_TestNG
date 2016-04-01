package com.assignment.excelReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelReader {

	public Sheet readExcel(String filePath, String fileName, String sheetName)
			throws IOException {

		// Create a object of File class to open xlsx file
		File file = new File(filePath + "\\" + fileName);

		// Create an object of FileInputStream class to read excel file
		FileInputStream inputStream = new FileInputStream(file);
		Workbook ptcHackerankWorkbook = null;

		// Find the file extension by spliting file name in substing and getting
		// only extension name
		String fileExtensionName = fileName.substring(fileName.indexOf("."));

		// checking if its xls , I have not created it for xlsx as it requires
		// importing all apache POI jars
		// Which are not feasible to mail
		if (fileExtensionName.equals(".xls")) {
			// If it is xls file then create object of XSSFWorkbook class
			ptcHackerankWorkbook = new HSSFWorkbook(inputStream);
		}
		
		
		// Read sheet inside the workbook by its name
		Sheet ptcHackerankSheet = ptcHackerankWorkbook.getSheet(sheetName);
		return ptcHackerankSheet;
	}
	
	
	
}
