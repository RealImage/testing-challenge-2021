package utilities;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebElement;

public class CommonMethods {
	
	public void sendText(WebElement element, String text){
		element.clear();
		element.sendKeys(text);
	}
	
	public void clickElement(WebElement element){
		element.click();
	}
	
	public String getText(WebElement element){
		return element.getText();
	}
	
	public String getAttribute(WebElement element, String Attribute){
		return element.getAttribute(Attribute);
	}

	public LinkedHashMap<String,String> returnDataFromExcel(File file, String sheetName, String Testcase) throws IOException{
		
		FileInputStream fileStream = new FileInputStream(file);
		XSSFWorkbook book = new XSSFWorkbook(fileStream);
		XSSFSheet sheet = book.getSheet(sheetName);

		int totalRow = sheet.getPhysicalNumberOfRows();
		int totalColumn = sheet.getRow(0).getLastCellNum();
		
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		
		for(int row = 1; row <totalRow; row++){
			System.out.println(sheet.getRow(row).getCell(0).getStringCellValue());
			if(sheet.getRow(row).getCell(0).getStringCellValue().equals(Testcase)){
				for(int column = 1; column < totalColumn; column++){
					map.put(
							sheet.getRow(0).getCell(column).getStringCellValue().replace("'", ""), 
							sheet.getRow(row).getCell(column).getStringCellValue().replace("'", ""));
				}
			}
		}
		
		return map;
	}

	
}
