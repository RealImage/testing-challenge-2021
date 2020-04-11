package httpRequests;

import java.util.LinkedHashMap;
import java.util.List;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojos.Get;
import pojos.GetSharedFiles;
import utilities.CommonMethods;
import utilities.Reports;

public class GetRequest extends CommonMethods {

	String baseURI;
	String sessionID;
	Reports report;

	public GetRequest(String baseURI, String sessionID, Reports report) {
		this.baseURI = baseURI;
		this.sessionID = sessionID;
		this.report = report;
	}

	public Response makeGetRequest(String file, String resource) {

		RestAssured.baseURI = baseURI;
		RequestSpecification httpRequest = RestAssured.given().relaxedHTTPSValidation();

		// query param
		httpRequest.queryParam("token", sessionID);

		if (resource.equals("files")) {
			httpRequest.queryParam("getSharedFiles", file);
		} else if (resource.equals("upload")) {
			httpRequest.queryParam("fileId", file);
		}

		httpRequest.expect().defaultParser(Parser.JSON);

		return httpRequest.get("//" + resource);

	}

	public boolean validateGet(Response response, LinkedHashMap<String, String> testData) throws Exception {

		if (!validateStatusCode(response, 200)) {
			report.addStep(Reports.inBold("Get Request failed") + " with status code " + response.statusCode(),
					Reports.results.FAILED.toString());
			throw new Exception("Post Request failed");
		} else {
			report.addStep(Reports.inBold("Get Request success: ") + response.asString(),
					Reports.results.PASSED.toString());
		}

		Get get = response.as(Get.class);
		return validateUploadGet(get, testData);
	}

	public boolean validateUploadGet(Get get, LinkedHashMap<String, String> testData) {
		if ((get.getName().equals(testData.get("name")) && get.getFileId().equals(testData.get("fileId"))
				&& get.getStatus().equals("Pending") && get.getBytesCompleted().equals("0"))) {
			return true;
		} else if ((get.getName().equals(testData.get("name")) && get.getFileId().equals(testData.get("fileId"))
				&& get.getStatus().equals("Completed") && get.getBytesCompleted().equals(testData.get("size")))) {
			return true;
		} else {
			return false;
		}
	}

	public boolean validateGetFiles(Response response, LinkedHashMap<String, String> testData) throws Exception {

		if (!validateStatusCode(response, 200)) {
			report.addStep(Reports.inBold("Get Request failed") + " with status code " + response.statusCode(),
					Reports.results.FAILED.toString());
			throw new Exception("Post Request failed");
		} else {
			report.addStep(Reports.inBold("Get Request success: ") + response.asString(),
					Reports.results.PASSED.toString());
		}

		Get[] get = response.as(Get[].class);
		return validateFilesGet(get, testData);
	}

	public boolean validateFilesGet(Get[] getFiles, LinkedHashMap<String, String> testData) {
		boolean flag = false;
		
		for(Get get : getFiles) {
			//Get get = gets.getGet();
			if ((get.getName().equals(testData.get("name")) && get.getFileId().equals(testData.get("fileId"))
					)) {
				flag = true;
			} else if ((get.getName().equals(testData.get("name")) && get.getFileId().equals(testData.get("fileId"))
					)) {
				flag = true;
			}

		}
		return flag;
	}

}
