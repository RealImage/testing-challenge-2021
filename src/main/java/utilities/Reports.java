package utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import io.restassured.response.Response;


public class Reports extends TestBase{
	public File file;
	FileWriter writer;

	public enum results {
		PASSED, FAILED, SKIPPED
	}

	
	public void openReport () throws IOException{
		
		String reportName = "ShareAPI-Testreport" + ".html";
		file = new File(projectPath + property.getProperty("reportPath") + reportName);
		
		if(!file.createNewFile()) {
			file.delete();
			file.createNewFile();
		}
		
		writer = new FileWriter(file);
		
		writer.write("<html><head><title>Test Report</title><style> " +
					".wholeBody{ " +
					"	width: 100%; " +
					"	margin: auto; " +
					"	border: 1px solid #bdc3c7; " +
					"} " +
					".header { " +
					"  padding: 10px; " +
					"  text-align: center; " +
					"  color: #2c3e50; " +
					"  font-size: 20px; " +
					"} " +
					"table{ " +
					"text-align: center; " +
					"} " +
					"tr:nth-child(even) { " +
					"  background-color: #f2f2f2; " +
					"} " +
					"td{ " +
					"  width: 90px; " +
					"} " +
					"</style></head><body><div class=\"header\"><h1>Test report</h1></Div> " +
					"<TABLE class='wholeBody'> " +
					"<tr><th>Test Case Name</th> " +
					"<th>Request hit</th> " +
					"<th>Result</th> " +
					"</tr> ");
		
	}
	
	
	public void addStep(String testCaseName, String requestHit, String result) throws IOException {
		
		
		writer.write("<tr><td>" + testCaseName + "</td>"
				+ "<td>" + requestHit + "</td>" + 
				"<td><a href=\"" + "   " + "\">" + result + "</a></td>" + //add response log here 
				"</tr>");
		
	}
	
	
	public void closeReport() throws IOException {
				
		writer.write("</TABLE>\r\n" + 
				"</body>\r\n" + 
				"</html>");
		writer.flush();
	}
	
	
	

}
