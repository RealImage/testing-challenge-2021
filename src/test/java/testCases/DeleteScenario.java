package testCases;

import java.io.IOException;

import org.testng.SkipException;
import org.testng.annotations.Test;

import httpRequests.DeleteRequest;
import httpRequests.GetRequest;
import httpRequests.PostRequest;
import httpRequests.PutRequest;
import io.restassured.response.Response;
import pages.HomePage;
import utilities.CommonMethods;
import utilities.Hooks;
import utilities.Reports;
import utilities.Reports.results;

public class DeleteScenario extends Hooks{
	@Test
	public void deleteFile() throws IOException {
		String testCaseName = "Delete any file from your account";
		Reports report = new Reports(testCaseName);
		PostRequest post = new PostRequest(baseURI, primarySessionID, report);
		GetRequest get = new GetRequest(baseURI, primarySessionID, report);
		PutRequest put = new PutRequest(baseURI, primarySessionID, report);
		DeleteRequest delete = new DeleteRequest(baseURI, primarySessionID, report);

		try {

			// preparation
			testData = method.returnDataFromExcel(testDatafile, property.getProperty("uploadPost"), testCaseName);
			if (testData.isEmpty()) {
				report.addStep(Reports.inBold("Info:") + " Test Data not available",
						Reports.results.SKIPPED.toString());
				throw new SkipException("Test Data not available");
			} else {
				testData = method.optimiseData(testData);
			}

			// post request to add new file
			testData.put(CommonMethods.bodyAttributes.fileId.toString(),
					post.makePostRequestandReturnFileID(testData, resources.upload.toString()));

			// put request
			put.makePutRequest(testData.get(CommonMethods.bodyAttributes.fileId.toString()),
					resources.upload.toString(), testData.get(CommonMethods.bodyAttributes.size.toString()));

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
			
			//delete
			delete.makeDeleteRequest(testData.get(CommonMethods.bodyAttributes.fileId.toString()));
			
			getResponse = get.makeGetRequest(testData.get(CommonMethods.bodyAttributes.fileId.toString()),
					resources.upload.toString());
			
			if (method.validateStatusCode(getResponse, 200)) {
				report.addStep(Reports.inBold("File Not deleted. File retrieved in Get call: ") + getResponse.asString(), results.FAILED.toString());
			} else {
				report.addStep(Reports.inBold("Get request Failed as expected: ") + getResponse.asString(), results.PASSED.toString());
			}
			home.navigateToDashboard();
			if (home.validateFilePresent(testData.get(CommonMethods.bodyAttributes.fileId.toString()))) {
				report.addStepWithScreenshot(Reports.inBold("File Present in Dashboard"), results.FAILED.toString(),
						primaryDriver);
			} else {
				report.addStepWithScreenshot(Reports.inBold("File NOT Present in Dashboard"), results.PASSED.toString(),
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
