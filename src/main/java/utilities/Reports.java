package utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;


public class Reports extends TestBase {
	//static for main report
	public static File mainFile;
	public static FileWriter mainWriter;
	public static enum results {
		PASSED, FAILED, SKIPPED
	}
	
	public static void openMainReport() throws IOException {

		String reportName = property.getProperty("mainReportName") ;
		mainFile = new File(projectPath + property.getProperty("mainReportPath") + reportName+ ".html");

		if (!mainFile.createNewFile()) {
			mainFile.delete();
			mainFile.createNewFile();
		}

		mainWriter = new FileWriter(mainFile, true);

		mainWriter.write("<html><head><title>"+reportName+"</title><style> " + ".wholeBody{ " + "	width: 100%; "
				+ "	margin: auto; " + "	border: 1px solid #bdc3c7; " + "} " + ".header { " + "  padding: 10px; "
				+ "  text-align: center; " + "  color: #2c3e50; " + "  font-size: 20px; " + "} " + "table{ "
				+ "table-layout:fixed; width:90px; word-break:break-all; text-align: center; " + "} "
				+ "tr:nth-child(even) { " + "  background-color: #f2f2f2; " + "} " + "td{ " + "  width: 90px; " + "} "
				+ "</style></head><body><div class=\"header\"><h1>Test report</h1></Div> "
				+ "<TABLE class='wholeBody'> " + "<tr><th>Test Case Name</th> " + "<th>Result</th> " + "</tr> ");
		
		

	}

	public static void closeMainReport() throws IOException {

		mainWriter.write("</TABLE>\r\n" + "</body>\r\n" + "</html>");
		mainWriter.flush();
	}
	
	public static String inBold(String str) {
		return "<b>" + str + "</B>";
	}
	
	//nonstatic for test Reports
	public File file;
	public FileWriter writer;
	private String testCaseName;
	private String testCaseResult;
	
	public Reports(String testCaseName) throws IOException{
		this.testCaseName = testCaseName;
		this.testCaseResult = results.PASSED.toString();
		String reportName = this.testCaseName + ".html";
		file = new File(projectPath + property.getProperty("reportPath") + reportName);

		if (!file.createNewFile()) {
			file.delete();
			file.createNewFile();
		}

		writer = new FileWriter(file, true);

		writer.write("<html><head><title>"+this.testCaseName +"</title><style> " + ".wholeBody{ " + "	width: 100%; "
				+ "	margin: auto; " + "	border: 1px solid #bdc3c7; " + "} " + ".header { " + "  padding: 10px; "
				+ "  text-align: center; " + "  color: #2c3e50; " + "  font-size: 20px; " + "} " + "table{ "
				+ "table-layout:fixed; width:90px; word-break:break-all; text-align: center; " + "} "
				+ "tr:nth-child(even) { " + "  background-color: #f2f2f2; " + "} " + "td{ " + "  width: 90px; " + "} "
				+ "</style></head><body><div class=\"header\"><h1>"+ this.testCaseName +"</h1></Div> "
				+ "<TABLE class='wholeBody'> " +  "<th>Request hit</th> "
				+ "<th>Result</th> " + "</tr> ");
	}
	

	
	
	public void addMainStep() throws IOException {
		
		mainWriter.write("<tr><td>" + this.testCaseName + "</td>" + "<td><a href=\"" + 
				projectPath + property.getProperty("reportPath") + this.testCaseName + ".html"
				+ "\">" + this.testCaseResult + "</a></td>" + // add response log here
				"</tr>");

	}

	public void addStepWithScreenshot(String stepName, String result, WebDriver driver) throws IOException {
		if (result.equals(results.FAILED.toString())){
			this.testCaseResult = results.FAILED.toString();
		}
		writer.write("<tr>" + "<td>" + stepName + "</td>" + "<td><a href=\"" + takeScreenshots(this.testCaseName, driver)
				+ "\">" + result + "</a></td>" + // add response log here
				"</tr>");

	}
	
	public void addStep(String stepName, String result) throws IOException {
		if (result.equals(results.FAILED.toString())){
			this.testCaseResult = results.FAILED.toString();
		}
		writer.write("<tr>" + "<td>" + stepName + "</td>" + "<td>"
				+ result + "</td>" + // add response log here
				"</tr>");

	}

	public void closeReport() throws IOException {

		writer.write("</TABLE>\r\n" + "</body>\r\n" + "</html>");
		writer.flush();
	}

	
	
	public String takeScreenshots(String tc, WebDriver driver) throws IOException{
		String path = projectPath + property.getProperty("screenshotsPath") + tc + ".png";
		File screenshotPath = new File(path);
		if(screenshotPath.exists()){
			screenshotPath.delete();
		}
		File scr = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scr, new File(path));
		
		return path;
		
	}

}
