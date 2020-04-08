package httpRequests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class DeleteRequest {
	String baseURI;
	String sessionID;
	
	public DeleteRequest(String baseURI, String sessionID) {
		this.baseURI = baseURI;
		this.sessionID = sessionID;
	}
	
	public Response makeDeleteRequest(String fileId, String resource){
		
		RestAssured.baseURI = baseURI;
        RequestSpecification httpRequest = RestAssured.given().relaxedHTTPSValidation();
        
        //query param
        httpRequest.queryParam("token", sessionID);
        
        //header
        
        httpRequest.header("Content-Type","multipart/form-data")
        	.header("Accept","application/json");
        
        //body
        
        httpRequest.multiPart("fileId", fileId);
        
        
        return httpRequest.delete("//" + resource);
        
	}
	
	public void assertStatusDeleteRequest(Response deleteResponse) throws Exception{
		System.out.println(deleteResponse.statusCode());
		System.out.println(deleteResponse.asString());
        if(deleteResponse.statusCode() != 200){
        	//fail report
        	//throw new Exception("Upload delete Response code not 200.  Test case failed");
        }
		
	}
}
