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

public class FilesShareScenario extends Hooks {

	@Test
	public void approveShare() throws IOException {
		String testCaseName = "Approve the shared file from the receiver account";
		Reports report = new Reports(testCaseName);
		PostRequest postPrimary = new PostRequest(baseURI, primarySessionID, report);
		PutRequest putPrimary = new PutRequest(baseURI, primarySessionID, report);
		PostRequest post = new PostRequest(baseURI, secondarySessionID, report);
		GetRequest get = new GetRequest(baseURI, secondarySessionID, report);
		PutRequest put = new PutRequest(baseURI, secondarySessionID, report);

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
					postPrimary.makePostRequestandReturnFileID(testData, resources.upload.toString()));

			// put request
			putPrimary.makePutRequest(testData.get(CommonMethods.bodyAttributes.fileId.toString()),
					resources.upload.toString(), testData.get(CommonMethods.bodyAttributes.size.toString()));

			// scenario start
			// post to share file
			postPrimary.fileSharePostRequest(testData.get(CommonMethods.bodyAttributes.fileId.toString()),
					property.getProperty("secId"));

			// get files
			Response getResponse = get.makeGetRequest(testData.get(CommonMethods.bodyAttributes.fileId.toString()),
					resources.files.toString());
			if (get.validateGetFiles(getResponse, testData)) {
				report.addStep(Reports.inBold("Get request returned with correct body"), results.PASSED.toString());
			} else {
				report.addStep(Reports.inBold("Get request returned with INCORRECT body"), results.FAILED.toString());
			}

			HomePage home = new HomePage(secondaryDriver, method);
			home.navigateToInbox();
			if (home.validateFilePresent(testData.get(CommonMethods.bodyAttributes.fileId.toString()))) {
				report.addStepWithScreenshot(Reports.inBold("File Present in Inbox"), results.PASSED.toString(),
						secondaryDriver);
			} else {
				report.addStepWithScreenshot(Reports.inBold("File NOT Present in Inbox"), results.FAILED.toString(),
						secondaryDriver);
			}

			// put request to accept
			put.makePutRequest(testData.get(CommonMethods.bodyAttributes.fileId.toString()),
					resources.files.toString(), "true");
			
			home.navigateToDashboard();
			if (home.validateFilePresent(testData.get(CommonMethods.bodyAttributes.fileId.toString()))) {
				report.addStepWithScreenshot(Reports.inBold("File Present in Dashboard"), results.PASSED.toString(),
						secondaryDriver);
			} else {
				report.addStepWithScreenshot(Reports.inBold("File NOT Present in Dashboard"), results.FAILED.toString(),
						secondaryDriver);
			}
			

		} catch (Exception e) {
			report.addStep(Reports.inBold("TESTCASE FAILED: ") + e, results.FAILED.toString());
		} finally {
			report.closeReport();
			report.addMainStep();
		}

	}

	@Test
	public void rejectShare() throws IOException {
		String testCaseName = "Reject the shared file from the receiver account";
		Reports report = new Reports(testCaseName);
		PostRequest postPrimary = new PostRequest(baseURI, primarySessionID, report);
		PutRequest putPrimary = new PutRequest(baseURI, primarySessionID, report);
		PostRequest post = new PostRequest(baseURI, secondarySessionID, report);
		GetRequest get = new GetRequest(baseURI, secondarySessionID, report);
		PutRequest put = new PutRequest(baseURI, secondarySessionID, report);

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
					postPrimary.makePostRequestandReturnFileID(testData, resources.upload.toString()));

			// put request
			putPrimary.makePutRequest(testData.get(CommonMethods.bodyAttributes.fileId.toString()),
					resources.upload.toString(), testData.get(CommonMethods.bodyAttributes.size.toString()));

			// scenario start
			// post to share file
			postPrimary.fileSharePostRequest(testData.get(CommonMethods.bodyAttributes.fileId.toString()),
					property.getProperty("secId"));

			// get files
			Response getResponse = get.makeGetRequest(testData.get(CommonMethods.bodyAttributes.fileId.toString()),
					resources.files.toString());
			if (get.validateGetFiles(getResponse, testData)) {
				report.addStep(Reports.inBold("Get request returned with correct body"), results.PASSED.toString());
			} else {
				report.addStep(Reports.inBold("Get request returned with INCORRECT body"), results.FAILED.toString());
			}

			HomePage home = new HomePage(secondaryDriver, method);
			home.navigateToInbox();
			if (home.validateFilePresent(testData.get(CommonMethods.bodyAttributes.fileId.toString()))) {
				report.addStepWithScreenshot(Reports.inBold("File Present in Inbox"), results.PASSED.toString(),
						secondaryDriver);
			} else {
				report.addStepWithScreenshot(Reports.inBold("File NOT Present in Inbox"), results.FAILED.toString(),
						secondaryDriver);
			}

			// put request to reject
			put.makePutRequest(testData.get(CommonMethods.bodyAttributes.fileId.toString()),
					resources.files.toString(), "false");
			
			home.navigateToDashboard();
			if (home.validateFilePresent(testData.get(CommonMethods.bodyAttributes.fileId.toString()))) {
				report.addStepWithScreenshot(Reports.inBold("File Present in Dashboard"), results.FAILED.toString(),
						secondaryDriver);
			} else {
				report.addStepWithScreenshot(Reports.inBold("File NOT Present in Dashboard"), results.PASSED.toString(),
						secondaryDriver);
			}
			

		} catch (Exception e) {
			report.addStep(Reports.inBold("TESTCASE FAILED: ") + e, results.FAILED.toString());
		} finally {
			report.closeReport();
			report.addMainStep();
		}

	}

}
