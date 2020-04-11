package httpRequests;

import java.io.IOException;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utilities.CommonMethods;
import utilities.Reports;

public class PutRequest extends CommonMethods {

	String baseURI;
	String sessionID;
	Reports report;

	public PutRequest(String baseURI, String sessionID, Reports report) {
		this.baseURI = baseURI;
		this.sessionID = sessionID;
		this.report = report;
	}

	public void makePutRequest(String fileId, String resource, String value) throws Exception {

		RestAssured.baseURI = baseURI;
		RequestSpecification httpRequest = RestAssured.given().relaxedHTTPSValidation();

		// query param
		httpRequest.queryParam("token", sessionID);

		// header
		httpRequest.header("Content-Type", "multipart/form-data").header("Accept", "application/json");

		// body

		httpRequest.multiPart("fileId", fileId);

		if (resource.equals("files")) {
			httpRequest.multiPart("isAccepted", value);
		} else if (resource.equals("upload")) {
			httpRequest.queryParam("bytesCompleted", value);
		}

		Response response =  httpRequest.put("//" + resource);
		
		if(!validateStatusCode(response, 200)){
        	report.addStep(Reports.inBold("Put Request failed") + " with status code "+ response.statusCode(), Reports.results.FAILED.toString());
        	throw new Exception("Put Request failed");
        }else{
        	report.addStep(Reports.inBold("Put Request success: ") + response.asString(), Reports.results.PASSED.toString());
        }
	}
}
