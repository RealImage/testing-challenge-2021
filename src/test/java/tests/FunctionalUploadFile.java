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
import utilities.CommonScenarios.resources;
import utilities.Reports.results;


public class FunctionalUploadFile extends CommonScenarios{

	
	//upload file happy scenario
	@Test
	public void uploadFileSizeZero() throws Exception{
		String testCase = "Zero size file upload";
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
			GetRequest get = new GetRequest(baseURI, primarySessionID);
			
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
	        
			//1st get request
			getResponse = get.makeGetRequest(validation.get("fileID")
					, resources.upload.toString());
	        validation = get.validateStatusRetrieveDataGetRequest(getResponse
	        		, validation);
	        testPass = get.assertFirstGet(validation);
	        if(testPass){
	        	report.addStep(inBold("GET RESUEST to validate status of file with zero size upload: ") + getResponse.asString(), results.PASSED.toString());
	        }else{
	        	report.addStep(inBold("GET RESUEST to validate status of file with zero size upload: ") + getResponse.asString(), results.FAILED.toString());
	        }
        
		}catch(Exception e){
			report.addStep(inBold("TESTCASE FAILED: ") + e, results.FAILED.toString());
		}finally{
			report.closeReport();
			addMainStep(testCase);
		}
	}
	
	
	@Test
	public void reduceBytesAfterComplete() throws Exception{
		String testCase = "Reduce BytesCompleted after successful upload";
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
			PutRequest put = new PutRequest(baseURI, primarySessionID);
			
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
	        
			//1st get request
			getResponse = get.makeGetRequest(validation.get("fileID")
					, resources.upload.toString());
	        validation = get.validateStatusRetrieveDataGetRequest(getResponse
	        		, validation);
	        testPass = get.assertFirstGet(validation);
	        if(testPass){
	        	report.addStep(inBold("GET RESUEST after post: ") + getResponse.asString(), results.PASSED.toString());
	        }else{
	        	report.addStep(inBold("GET RESUEST after post: ") + getResponse.asString(), results.FAILED.toString());
	        }
	        testPass = true;
	        //put request to upload complete file
	        putResponse = put.makePutRequest(validation.get("fileID")
	        		, resources.upload.toString()
	        		, validation.get("fileSize"));
	        put.assertStatusPutRequest(putResponse);
	        report.addStep(inBold("PUT RESUEST to upload file completely: ") + putResponse.asString(), results.PASSED.toString());
	        
	        //get request after complete file upload
	        getResponse = get.makeGetRequest(validation.get("fileID")
	        		, resources.upload.toString());
	        
	        validation = get.validateStatusRetrieveDataGetRequest(getResponse
	        		, validation);
	        
	        testPass = get.assertGetAfterPut(validation,validation.get("fileSize"), "Completed" );
	        if(testPass){
	        	report.addStep(inBold("GET RESUEST to validate successful file upload: ") + getResponse.asString(), results.PASSED.toString());
	        }else{
	        	report.addStep(inBold("GET RESUEST to validate successful file upload: ") + getResponse.asString(), results.FAILED.toString());
	        }
	        
	        Integer.parseInt(validation.get("fileSize"));
	        
	        putResponse = put.makePutRequest(validation.get("fileID")
	        		, resources.upload.toString()
	        		, String.valueOf(Integer.parseInt(validation.get("fileSize")) - 1));
	        
	        if(putResponse.statusCode() == 200){
	        	report.addStep(inBold("PUT RESUEST to validate put is not accepted after successful upload: ") + putResponse.asString(), results.FAILED.toString());
	        }else{
	        	report.addStep(inBold("PUT RESUEST to validate put is not accepted after successful upload: ") + putResponse.asString(), results.PASSED.toString());
	        }
        
        }catch(Exception e){
        	report.addStep(inBold("TESTCASE FAILED: ") + e, results.FAILED.toString());
		}finally{
			report.closeReport();
			addMainStep(testCase);
		}
	}
}
