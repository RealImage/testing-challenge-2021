package httpRequests;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utilities.CommonMethods;
import utilities.Reports;
import utilities.CommonMethods.bodyAttributes;

public class PostRequest extends CommonMethods {

	String baseURI;
	String sessionID;
	Reports report;
	
	public PostRequest(String baseURI, String sessionID,Reports report) {
		this.baseURI = baseURI;
		this.sessionID = sessionID;
		this.report = report;
	}
	
	public String makePostRequestandReturnFileID(LinkedHashMap<String,String> map, String resource) throws Exception{
		
		RestAssured.baseURI = baseURI;
        RequestSpecification httpRequest = RestAssured.given().relaxedHTTPSValidation();
        
        //query param
        httpRequest.queryParam("token", sessionID);
        
        //header
        
        httpRequest.header("Content-Type","multipart/form-data")
        	.header("Accept","application/json");
        
        //body
        
        for (Entry<String, String> entry : map.entrySet()) {
        	System.out.println(entry.getKey()+" "+ entry.getValue());
        	httpRequest.multiPart(entry.getKey(), entry.getValue());
        }
        
        Response response =  httpRequest.post("//" + resource);
        
        if(!validateStatusCode(response, 200)){
        	report.addStep(Reports.inBold("Post Request failed") + " with status code "+ response.statusCode(), Reports.results.FAILED.toString());
        	throw new Exception("Post Request failed");
        }else{
        	report.addStep(Reports.inBold("Post Request success: ") + response.asString(), Reports.results.PASSED.toString());
        }
        
        return returnValueFromResponse(response,bodyAttributes.fileId.toString());
        
	}
	
	public void fileSharePostRequest(String fileId, String shareTo) throws Exception{
		RestAssured.baseURI = baseURI;
        RequestSpecification httpRequest = RestAssured.given().relaxedHTTPSValidation();
        
        //query param
        httpRequest.queryParam("token", sessionID);
        
        //header
        
        httpRequest.header("Content-Type","multipart/form-data")
        	.header("Accept","application/json");
        
        //body
        httpRequest.multiPart("fileId", fileId);
        httpRequest.multiPart("shareTo", shareTo);
        
        Response response =  httpRequest.post("//files");
        
        if(!validateStatusCode(response, 200)){
        	report.addStep(Reports.inBold("Post Request failed") + " with status code "+ response.statusCode(), Reports.results.FAILED.toString());
        	throw new Exception("Post Request failed");
        }else{
        	report.addStep(Reports.inBold("Post Request success: ") + response.asString(), Reports.results.PASSED.toString());
        }
        
	}
	
}
