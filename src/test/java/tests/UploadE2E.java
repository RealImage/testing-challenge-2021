package tests;


import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.testng.SkipException;
import org.testng.annotations.Test;

import httpRequests.DeleteRequest;
import httpRequests.GetRequest;
import httpRequests.PostRequest;
import httpRequests.PutRequest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import utilities.CommonScenarios;

public class UploadE2E extends CommonScenarios{
	
	public enum resources{
		files,upload
	}
	
	//upload file happy scenario
	@Test
	public void uploadFiletillComplete() throws Exception{
		String testCase = "File upload happy path";
		boolean testPass = true;
		
		//things to validate in test
		HashMap<String, String> validation = 
				new HashMap<String, String>(); 
		validation.put("fileID", null);
		validation.put("fileName", null);
		validation.put("fileSize", null);
		validation.put("bytesCompleted", null);
		validation.put("status", null);
		
		
		//essentials
		JsonPath path;
		File file = new File(projectPath 
				+ property.getProperty("testDataSheet"));
		LinkedHashMap<String, String> map = 
				new LinkedHashMap<String, String>();
		PostRequest post = new PostRequest(baseURI, primarySessionID);
		GetRequest get = new GetRequest(baseURI, primarySessionID);
		PutRequest put = new PutRequest(baseURI, primarySessionID);
		DeleteRequest delete = new DeleteRequest(baseURI, primarySessionID);
		map = method.returnDataFromExcel(file
				, property.getProperty("uploadPost"), testCase);
		if(map.isEmpty()){
			addStep(testCase, "Info: Test Data not available", results.SKIPPED.toString());
			throw new SkipException("Test Data not available");
		}
		validation.put("fileSize", map.get("size")) ;
		Response postResponse;
		Response getResponse;
		Response putResponse;
		Response deleteResponse;
		
		//1st post request
		postResponse = post.makePostRequest(map
				, resources.upload.toString());
		validation = post.validateStatusRetrieveDataPostRequest(postResponse
				, validation);
		addStep(testCase, "POST RESUEST: " + postResponse.asString(), results.PASSED.toString());
        
		//1st get request
		getResponse = get.makeGetRequest(validation.get("fileID")
				, resources.upload.toString());
        validation = get.validateStatusRetrieveDataGetRequest(getResponse
        		, validation);
        testPass = get.assertFirstGet(validation);
        if(testPass){
        	addStep(testCase, "GET RESUEST: " + getResponse.asString(), results.PASSED.toString());
        }else{
        	addStep(testCase, "GET RESUEST: " + getResponse.asString(), results.FAILED.toString());
        }
        testPass = true;
        //put request to upload complete file
        putResponse = put.makePutRequest(validation.get("fileID")
        		, resources.upload.toString()
        		, validation.get("fileSize"));
        put.assertStatusPutRequest(putResponse);
        
        
        //get request after complete file upload
        getResponse = get.makeGetRequest(validation.get("fileID")
        		, resources.upload.toString());
        
        validation = get.validateStatusRetrieveDataGetRequest(getResponse
        		, validation);
        
        testPass = get.assertGetAfterPut(validation,validation.get("fileSize"), "Completed" );
        if(testPass){
        	addStep(testCase, "GET RESUEST: " + getResponse.asString(), results.PASSED.toString());
        }else{
        	addStep(testCase, "GET RESUEST: " + getResponse.asString(), results.FAILED.toString());
        }
	}
	
}
