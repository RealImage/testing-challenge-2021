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
import utilities.Reports.results;

public class E2EFiles extends CommonScenarios{
	@Test
	public void filesShareComplete() throws Exception{
		String testCase = "File share happy path";
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
					, property.getProperty("filesPost"), testCase);
			if(map.isEmpty()){
				report.addStep(inBold("Info:") +" Test Data not available", results.SKIPPED.toString());
				throw new SkipException("Test Data not available");
			}
			map = method.optimiseData(map);
			validation.put("fileSize", map.get("size")) ;
			
		}catch(Exception e){
			
		}finally{
			
		}
	}
}
