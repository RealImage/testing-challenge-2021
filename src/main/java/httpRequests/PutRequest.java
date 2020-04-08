package httpRequests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PutRequest {
	String baseURI;
	String sessionID;
	
	public PutRequest(String baseURI, String sessionID) {
		this.baseURI = baseURI;
		this.sessionID = sessionID;
	}
	
	
	public Response makePutRequest(String fileId, String resource, String value){
		
		RestAssured.baseURI = baseURI;
        RequestSpecification httpRequest = RestAssured.given().relaxedHTTPSValidation();
        
        //query param
        httpRequest.queryParam("token", sessionID);
        
        //header
        
        httpRequest.header("Content-Type","multipart/form-data")
        	.header("Accept","application/json");
        
        //body
        
        httpRequest.multiPart("fileId", fileId);
        
        if(resource.equals("files")){
        	httpRequest.multiPart("isAccepted", value);
        }else if(resource.equals("upload")){
        	httpRequest.queryParam("bytesCompleted", value);
        }
        
        return httpRequest.put("//" + resource);
        
	}
	
	public void assertStatusPutRequest(Response putResponse) throws Exception{
		System.out.println(putResponse.statusCode());
		System.out.println(putResponse.asString());
        if(putResponse.statusCode() != 200){
        	//fail report
        	throw new Exception("Upload put Response code not 200.  Test case failed");
        }
		
	}
}
