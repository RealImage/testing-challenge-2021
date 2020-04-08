package httpRequests;


import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class GetRequest {
	String baseURI;
	String sessionID;
	
	public GetRequest(String baseURI, String sessionID) {
		this.baseURI = baseURI;
		this.sessionID = sessionID;
	}
	
	
	public Response makeGetRequest(String file, String resource){
		
		RestAssured.baseURI = baseURI;
        RequestSpecification httpRequest = RestAssured.given().relaxedHTTPSValidation();
        
        //query param
        httpRequest.queryParam("token", sessionID);
        
        if(resource.equals("files")){
        	httpRequest.queryParam("getSharedFiles", file);
        }else if(resource.equals("upload")){
        	httpRequest.queryParam("fileId", file);
        }
        
        return httpRequest.get("//" + resource);
        
	}
	
	
	public HashMap<String, String> validateStatusRetrieveDataGetRequest(Response getResponse,HashMap<String, String> map) throws Exception{
		
		JsonPath path = new JsonPath(getResponse.asString());
        
        System.out.println(getResponse.statusCode());
        if(getResponse.statusCode() != 200){
        	//fail report
        	throw new Exception("Upload get Response code not 200.  Test case failed");
        }
        
        System.out.println(getResponse.asString());
        
        map.put("bytesCompleted",  path.getString("bytesCompleted"));
                System.out.println(map.get("bytesCompleted"));
        
        
                
        map.put("status",  path.getString("status"));
        System.out.println(map.get("status"));
        
      
        return map;
	}
	
	public boolean assertFirstGet(HashMap<String, String> validation){
		boolean testPass = true;
		 if(!validation.get("bytesCompleted").equals("0")){
	        	testPass = false;
	        	//report
	        }
	        
	      if(!validation.get("status").equals("Pending")){
	        	testPass = false;
	        	//report
	        }
	        
	    //to check file name hash can be included
		
		return testPass;
		
	}
	
	public boolean assertGetAfterPut(HashMap<String, String> validation, String uploaded, String status){
		boolean testPass = true;
		 if(!validation.get("bytesCompleted").equals(uploaded)){
	        	testPass = false;
	        	//report
	        }
		 
		 if(!validation.get("status").equals(status)){
	        	testPass = false;
	        	//report
	        }
		
		return testPass;
	}
	
	
}
