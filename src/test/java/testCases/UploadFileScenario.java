package testCases;

import java.io.IOException;

import org.testng.SkipException;
import org.testng.annotations.Test;

import httpRequests.GetRequest;
import httpRequests.PostRequest;
import httpRequests.PutRequest;
import io.restassured.response.Response;
import pages.HomePage;
import utilities.CommonMethods;
import utilities.Hooks;
import utilities.Reports;
import utilities.Reports.results;

public class UploadFileScenario extends Hooks {

	@Test
	public void uploadFile() throws IOException {
		String testCaseName = "Upload file and validate in account";
		Reports report = new Reports(testCaseName);
		PostRequest post = new PostRequest(baseURI, primarySessionID, report);
		GetRequest get = new GetRequest(baseURI, primarySessionID, report);
		PutRequest put = new PutRequest(baseURI, primarySessionID, report);
		
		
		try {
			
			// preparation
			testData = method.returnDataFromExcel(testDatafile, property.getProperty("uploadPost"), testCaseName);
			if (testData.isEmpty()) {
				report.addStep(Reports.inBold("Info:") + " Test Data not available", Reports.results.SKIPPED.toString());
				throw new SkipException("Test Data not available");
			} else {
				testData = method.optimiseData(testData);
			}
		
			// post request
			testData.put(CommonMethods.bodyAttributes.fileId.toString(),
					post.makePostRequestandReturnFileID(testData, resources.upload.toString()));

			// get request
			Response getResponse = get.makeGetRequest(testData.get(CommonMethods.bodyAttributes.fileId.toString()),
					resources.upload.toString());
			testData.put(CommonMethods.bodyAttributes.status.toString(),
					method.returnValueFromResponse(getResponse, CommonMethods.bodyAttributes.status.toString()));
			if (get.validateGet(getResponse, testData)) {
				report.addStep(Reports.inBold("Get request returned with correct body"), results.PASSED.toString());
			} else {
				report.addStep(Reports.inBold("Get request returned with INCORRECT body"), results.FAILED.toString());
			}

			HomePage home = new HomePage(primaryDriver, method);
			home.navigateToDashboard();
			if (home.validateFilePresentwithStatus(testData.get(CommonMethods.bodyAttributes.fileId.toString()),
					testData.get(CommonMethods.bodyAttributes.status.toString()))) {
				report.addStepWithScreenshot(Reports.inBold("File Present in Dashboard"), results.PASSED.toString(),
						primaryDriver);
			} else {
				report.addStepWithScreenshot(Reports.inBold("File NOT Present in Dashboard"), results.FAILED.toString(),
						primaryDriver);
			}

			// put request
			put.makePutRequest(testData.get(CommonMethods.bodyAttributes.fileId.toString()),
					resources.upload.toString(), testData.get(CommonMethods.bodyAttributes.size.toString()));

			// get after complete upload

			getResponse = get.makeGetRequest(testData.get(CommonMethods.bodyAttributes.fileId.toString()),
					resources.upload.toString());
			testData.put(CommonMethods.bodyAttributes.status.toString(),
					method.returnValueFromResponse(getResponse, CommonMethods.bodyAttributes.status.toString()));
			if (get.validateGet(getResponse, testData)) {
				report.addStep(Reports.inBold("Get request returned with correct body"), results.PASSED.toString());
			} else {
				report.addStep(Reports.inBold("Get request returned with INCORRECT body"), results.FAILED.toString());
			}
			
			home.navigateToDashboard();
			if (home.validateFilePresentwithStatus(testData.get(CommonMethods.bodyAttributes.fileId.toString()),
					testData.get(CommonMethods.bodyAttributes.status.toString()))) {
				report.addStepWithScreenshot(Reports.inBold("File Present in Dashboard"), results.PASSED.toString(),
						primaryDriver);
			} else {
				report.addStepWithScreenshot(Reports.inBold("File NOT Present in Dashboard"), results.FAILED.toString(),
						primaryDriver);
			}

		} catch (Exception e) {
			report.addStep(Reports.inBold("TESTCASE FAILED: ") + e, results.FAILED.toString());
		} finally {
			report.closeReport();
			report.addMainStep();
		}

	}

}
