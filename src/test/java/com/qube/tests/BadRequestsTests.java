package com.qube.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.restassured.response.Response;
import com.qube.RestApi.RestApiTestCase;
import com.qube.tests.base.BaseTest;
import com.qube.util.dataprovider.DataProviderUtil;
import com.qube.util.dataprovider.TestsDataProvider;

public class BadRequestsTests extends BaseTest
{
	static Logger logger = LoggerFactory
			.getLogger(BadRequestsTests.class);
	private static final String TEST_DATA_FILE_NAME = "/bad-request-scenarios.xml";
	
	@Test(description = "", dataProvider = TestsDataProvider.CONVERT_XML_TO_TEST_CASE_METHOD_NAME, dataProviderClass = TestsDataProvider.class)
	@DataProviderUtil.DataProviderArguments("filePath=" + TEST_DATA_FILE_NAME)
	public void unauthorisedScenarios(RestApiTestCase testCase) throws Exception 
	{
		logger.info(testCase.getDescription());
		logger.info(testCase.getBaseUrl() + testCase.getUrl());
		
		updateBaseUrlAndUserToken(testCase);
		
		Response response = restApiTestCaseClient.call(testCase);
		String stringResponse = response.asString();
		logger.info("response = " + stringResponse);
		
		//Validating if the response status code is 400
		Assert.assertTrue(testCase.getExpectedStatusCode() == response.getStatusCode(),
				String.format("Expected response code =%1$s and Actual response code =%2$s",
						testCase.getExpectedStatusCode(),response.getStatusCode()));
		
	}
}
