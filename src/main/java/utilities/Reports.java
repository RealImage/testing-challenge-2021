package utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import io.restassured.response.Response;

public class Reports extends TestBase {
	public File file;
	public static File mainFile;
	public FileWriter writer;
	public static FileWriter mainWriter;
	public static String testCaseResult;

	public static enum results {
		PASSED, FAILED, SKIPPED
	}

	public static void openMainReport() throws IOException {

		String reportName = "ShareAPI suite Report" + ".html";
		mainFile = new File(projectPath + property.getProperty("mainReportPath") + reportName);

		if (!mainFile.createNewFile()) {
			mainFile.delete();
			mainFile.createNewFile();
		}

		mainWriter = new FileWriter(mainFile, true);

		mainWriter.write("<html><head><title>ShareAPI suite Report</title><style> " + ".wholeBody{ " + "	width: 100%; "
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
	
	public static void addMainStep(String testCaseName) throws IOException {
		
		mainWriter.write("<tr><td>" + testCaseName + "</td>" + "<td><a href=\"" + 
				projectPath + property.getProperty("reportPath") + testCaseName + ".html"
				+ "\">" + testCaseResult + "</a></td>" + // add response log here
				"</tr>");

	}

	public void openReport(String testCase) throws IOException {
		
		testCaseResult = results.PASSED.toString();
		String reportName = testCase + ".html";
		file = new File(projectPath + property.getProperty("reportPath") + reportName);

		if (!file.createNewFile()) {
			file.delete();
			file.createNewFile();
		}

		writer = new FileWriter(file);

		writer.write("<html><head><title>"+testCase +"</title><style> " + ".wholeBody{ " + "	width: 100%; "
				+ "	margin: auto; " + "	border: 1px solid #bdc3c7; " + "} " + ".header { " + "  padding: 10px; "
				+ "  text-align: center; " + "  color: #2c3e50; " + "  font-size: 20px; " + "} " + "table{ "
				+ "table-layout:fixed; width:90px; word-break:break-all; text-align: center; " + "} "
				+ "tr:nth-child(even) { " + "  background-color: #f2f2f2; " + "} " + "td{ " + "  width: 90px; " + "} "
				+ "</style></head><body><div class=\"header\"><h1>"+testCase+"</h1></Div> "
				+ "<TABLE class='wholeBody'> " +  "<th>Request hit</th> "
				+ "<th>Result</th> " + "</tr> ");

	}

	public void addStep(String requestHit, String result) throws IOException {
		if (result.equals(results.FAILED.toString())){
			testCaseResult = results.FAILED.toString();
		}
		writer.write("<tr>" + "<td>" + requestHit + "</td>" + "<td><a href=\"" + "   "
				+ "\">" + result + "</a></td>" + // add response log here
				"</tr>");

	}

	public void closeReport() throws IOException {

		writer.write("</TABLE>\r\n" + "</body>\r\n" + "</html>");
		writer.flush();
	}

	public String inBold(String str) {
		return "<b>" + str + "</B>";
	}

}
