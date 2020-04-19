package com.qube.util;

import static com.jayway.restassured.RestAssured.given;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.qube.RestApi.RestApiTestCase;


public class RestApiTestCaseClient 
{
	public Response call(RestApiTestCase testCase) throws InterruptedException, FileNotFoundException, IOException 
	{
		Response response = null;

		switch (testCase.getRequestMethod()) {
		case GET:
			response = callGet(testCase);
			break;
		case POST:
			response = callPost(testCase);
			break;
		case PUT:
			response = callPut(testCase);
			break;
		case DELETE:
			response = callDelete(testCase);
			break;
		}
		return response;
	}

	// GET
	private Response callGet(RestApiTestCase testCase)
			throws InterruptedException 
	{	
		RequestSpecification requestSpec = given()
				.relaxedHTTPSValidation()
				.headers(testCase.getHeaders())
				.queryParams(testCase.getParameters());
		return requestSpec.when().get(testCase.getFullUrl());
		
	}

	// POST
	private Response callPost(RestApiTestCase testCase) throws FileNotFoundException, IOException 
	{
		RequestSpecification requestSpec;
		Map<String,String> formData = testCase.getFormData();
		
		if (!testCase.getMultipartFormData().isEmpty()) 
		{
			File file = new File(testCase.getMultipartFormData());
			requestSpec = given().
				relaxedHTTPSValidation().
				headers(testCase.getHeaders()).
				queryParams(testCase.getParameters()).
		        multiPart("file1", file).
		        formParam("name", file.getName()).
		        formParam("size", file.length()).
		        formParam("hash", QubeUtil.calculateSHA256(file));
		} 
		else 
		{			
			requestSpec = given().
				relaxedHTTPSValidation().
				headers(testCase.getHeaders()).
				queryParams(testCase.getParameters());
			for (Map.Entry<String, String> entry : formData.entrySet()) {
				requestSpec = requestSpec.formParam(entry.getKey(),
						entry.getValue());
			}
		}
		return requestSpec.when().post(testCase.getFullUrl());
	}

	// PUT
	private Response callPut(RestApiTestCase testCase) throws FileNotFoundException, IOException 
	{
		RequestSpecification requestSpec;
		Map<String,String> formData = testCase.getFormData();
		
		requestSpec = given().
			relaxedHTTPSValidation().
			headers(testCase.getHeaders()).
			queryParams(testCase.getParameters());
		for (Map.Entry<String, String> entry : formData.entrySet()) {
			requestSpec = requestSpec.formParam(entry.getKey(),
					entry.getValue());
		}
		return requestSpec.when().post(testCase.getFullUrl());
		
	}

	// DELETE
	private Response callDelete(RestApiTestCase testCase) 
	{
		RequestSpecification requestSpec;
		Map<String,String> formData = testCase.getFormData();
		
		requestSpec = given().
				relaxedHTTPSValidation().
				headers(testCase.getHeaders()).
				queryParams(testCase.getParameters());
		for (Map.Entry<String, String> entry : formData.entrySet()) {
			requestSpec = requestSpec.formParam(entry.getKey(),
					entry.getValue());
		}
		
		return requestSpec.when().delete(testCase.getFullUrl());
	}

}
