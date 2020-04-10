package tests;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.testng.SkipException;
import org.testng.annotations.Test;

import httpRequests.GetRequest;
import httpRequests.PostRequest;
import httpRequests.PutRequest;
import io.restassured.response.Response;
import utilities.CommonScenarios;
import utilities.Reports;



public class FunctionalUploadToken extends CommonScenarios{
	
	String invalidSessionID = "e26619f5-7c8f-b5eb-b6dc232a410c";
	
	@Test
	public void uploadInvalidTokenPost() throws Exception{
		String testCase = "Invalid Token for Post Request";
		Reports report = new Reports();
		try{
		
			boolean testPass = true;
			report.openReport(testCase);
			Response postResponse;
						
			//things to validate in test
			HashMap<String, String> validation = 
					new HashMap<String, String>(); 
			validation.put("fileID", null);
			validation.put("fileName", null);
			validation.put("fileSize", null);
			validation.put("bytesCompleted", null);
			validation.put("status", null);
			
			
			//essentials
			File file = new File(projectPath 
					+ property.getProperty("testDataSheet"));
			LinkedHashMap<String, String> map = 
					new LinkedHashMap<String, String>();
			PostRequest post = new PostRequest(baseURI, invalidSessionID);
			
			map = method.returnDataFromExcel(file
					, property.getProperty("uploadPost"), testCase);
			if(map.isEmpty()){
				report.addStep(inBold("Info:") +" Test Data not available", results.SKIPPED.toString());
				throw new SkipException("Test Data not available");
			}
			map = method.optimiseData(map);
			validation.put("fileSize", map.get("size")) ;
			
			
			//1st post request
			postResponse = post.makePostRequest(map
					, resources.upload.toString());
			
			if(postResponse.statusCode() == 200){
				report.addStep(inBold("POST RESUEST with invalid session ID: ") + postResponse.asString(), results.FAILED.toString());
	        }else{
	        	report.addStep(inBold("POST RESUEST with invalid session ID: ") + postResponse.asString(), results.PASSED.toString());
	        }
        
		}catch(Exception e){
			report.addStep(inBold("TESTCASE FAILED: ") + e, results.FAILED.toString());
		}finally{
			report.closeReport();
			addMainStep(testCase);
		}
	}
	
	
	@Test
	public void uploadInvalidTokenGet() throws Exception{
		String testCase = "Invalid Token for Get Request";
		Reports report = new Reports();
		try{
		
			boolean testPass = true;
			report.openReport(testCase);
			Response postResponse;
			Response getResponse;
			
			//things to validate in test
			HashMap<String, String> validation = 
					new HashMap<String, String>(); 
			validation.put("fileID", null);
			validation.put("fileName", null);
			validation.put("fileSize", null);
			validation.put("bytesCompleted", null);
			validation.put("status", null);
			
			
			//essentials
			File file = new File(projectPath 
					+ property.getProperty("testDataSheet"));
			LinkedHashMap<String, String> map = 
					new LinkedHashMap<String, String>();
			PostRequest post = new PostRequest(baseURI, primarySessionID);
			GetRequest get = new GetRequest(baseURI, invalidSessionID);
			
			map = method.returnDataFromExcel(file
					, property.getProperty("uploadPost"), testCase);
			if(map.isEmpty()){
				report.addStep(inBold("Info:") +" Test Data not available", results.SKIPPED.toString());
				throw new SkipException("Test Data not available");
			}
			map = method.optimiseData(map);
			validation.put("fileSize", map.get("size")) ;
			
			
			//1st post request
			postResponse = post.makePostRequest(map
					, resources.upload.toString());
			
			validation = post.validateStatusRetrieveDataPostRequest(postResponse
					, validation);
			report.addStep(inBold("POST RESUEST to upload file: ") + postResponse.asString(), results.PASSED.toString());
	        
	        
	        //get request after complete file upload
	        getResponse = get.makeGetRequest(validation.get("fileID")
	        		, resources.upload.toString());
	        
	        if(getResponse.statusCode() == 200){
	        	report.addStep(inBold("GET RESUEST with invalid session ID: ") + getResponse.asString(), results.FAILED.toString());
	        }else{
	        	report.addStep(inBold("GET RESUEST with invalid session ID: ") + getResponse.asString(), results.PASSED.toString());
	        }
	        
        
		}catch(Exception e){
			report.addStep(inBold("TESTCASE FAILED: ") + e, results.FAILED.toString());
		}finally{
			report.closeReport();
			addMainStep(testCase);
		}
	}
	
	@Test
	public void uploadInvalidTokenPut() throws Exception{
		String testCase = "Invalid Token for Put Request";
		Reports report = new Reports();
		try{
		
			boolean testPass = true;
			report.openReport(testCase);
			Response postResponse;
			Response getResponse;
			Response putResponse;
			
			//things to validate in test
			HashMap<String, String> validation = 
					new HashMap<String, String>(); 
			validation.put("fileID", null);
			validation.put("fileName", null);
			validation.put("fileSize", null);
			validation.put("bytesCompleted", null);
			validation.put("status", null);
			
			
			//essentials
			File file = new File(projectPath 
					+ property.getProperty("testDataSheet"));
			LinkedHashMap<String, String> map = 
					new LinkedHashMap<String, String>();
			PostRequest post = new PostRequest(baseURI, primarySessionID);
			GetRequest get = new GetRequest(baseURI, primarySessionID);
			PutRequest put = new PutRequest(baseURI, invalidSessionID);
			
			map = method.returnDataFromExcel(file
					, property.getProperty("uploadPost"), testCase);
			if(map.isEmpty()){
				report.addStep(inBold("Info:") +" Test Data not available", results.SKIPPED.toString());
				throw new SkipException("Test Data not available");
			}
			map = method.optimiseData(map);
			validation.put("fileSize", map.get("size")) ;
			
			
			//1st post request
			postResponse = post.makePostRequest(map
					, resources.upload.toString());
			
			validation = post.validateStatusRetrieveDataPostRequest(postResponse
					, validation);
			report.addStep(inBold("POST RESUEST to upload file: ") + postResponse.asString(), results.PASSED.toString());
	        
	        //put
			
			putResponse = put.makePutRequest(validation.get("fileID")
	        		, resources.upload.toString()
	        		, validation.get("fileSize"));
			
			if(putResponse.statusCode() == 200){
				report.addStep(inBold("PUT RESUEST with invalid session ID: ") + putResponse.asString(), results.FAILED.toString());
	        }else{
	        	report.addStep(inBold("PUT RESUEST with invalid session ID: ") + putResponse.asString(), results.PASSED.toString());
	        }
			
	        //get request after complete file upload
	        getResponse = get.makeGetRequest(validation.get("fileID")
	        		, resources.upload.toString());
	        
	        validation = get.validateStatusRetrieveDataGetRequest(getResponse
	        		, validation);
	        testPass = get.assertFirstGet(validation);
	        
	        if(testPass){
	        	report.addStep(inBold("GET RESUEST after invalid put: ") + getResponse.asString(), results.PASSED.toString());
	        }else{
	        	report.addStep(inBold("GET RESUEST after invalid put: ") + getResponse.asString(), results.FAILED.toString());
	        }
        
		}catch(Exception e){
			report.addStep(inBold("TESTCASE FAILED: ") + e, results.FAILED.toString());
		}finally{
			report.closeReport();
			addMainStep(testCase);
		}
	}
	
	
}
