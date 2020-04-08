package httpRequests;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PostRequest {
	
	
	String baseURI;
	String sessionID;
	
	public PostRequest(String baseURI, String sessionID) {
		this.baseURI = baseURI;
		this.sessionID = sessionID;
	}
	
	public Response makePostRequest(LinkedHashMap<String,String> map, String resource){
				
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
        
        return httpRequest.post("//" + resource);
        
	}
	
	
	public HashMap<String, String> validateStatusRetrieveDataPostRequest(Response postResponse, HashMap<String, String> map) throws Exception{
		
		JsonPath path = new JsonPath(postResponse.asString());
        
        System.out.println(postResponse.statusCode());
        if(postResponse.statusCode() != 200){
        	//fail report
        	throw new Exception("Upload Post Response code not 200.  Test case failed");
        }
        
        //can implement going to web UI and validating file gets added in dashboard if required
        
        System.out.println(postResponse.asString());
        map.put("fileID", path.getString("fileId"))  ;
        System.out.println(map.get("fileID"));
		
		return map;
	}
	
	
	
	
}
