package httpRequests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utilities.CommonMethods;
import utilities.Reports;

public class DeleteRequest extends CommonMethods{

	String baseURI;
	String sessionID;
	Reports report;
	
	public DeleteRequest(String baseURI, String sessionID,Reports report) {
		this.baseURI = baseURI;
		this.sessionID = sessionID;
		this.report = report;
	}
	
	public void makeDeleteRequest(String fileId) throws Exception {

		RestAssured.baseURI = baseURI;
		RequestSpecification httpRequest = RestAssured.given().relaxedHTTPSValidation();

		// query param
		httpRequest.queryParam("token", sessionID);

		// header
		httpRequest.header("Content-Type", "multipart/form-data")
			.header("Accept", "application/json");

		// body

		httpRequest.multiPart("fileId", fileId);

		
		Response response =  httpRequest.delete("//files");
		
		if(!validateStatusCode(response, 200)){
        	report.addStep(Reports.inBold("Delete Request failed") + " with status code "+ response.statusCode(), Reports.results.FAILED.toString());
        	throw new Exception("Delete Request failed");
        }else{
        	report.addStep(Reports.inBold("Delete Request success: ") + response.asString(), Reports.results.PASSED.toString());
        }
	}
}
