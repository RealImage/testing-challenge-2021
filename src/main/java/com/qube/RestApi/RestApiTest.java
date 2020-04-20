package com.qube.RestApi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.fail;

//import org.apache.sling.commons.json.JSONObject;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.json.JSONArray;
import org.skyscreamer.jsonassert.JSONParser;
import org.testng.Assert;

/**
 * This class provides support for assert comparison of json and
 * for building error messages
 */
public class RestApiTest 
{
	private static final String LINE_BREAK = "\n";
	public static final String TEST_DATA_FILE_PATH = "src/test/resources/data";

	protected void assertResponse(RestApiTestCase restApiTestCase,
			int actualResponseStatusCode, String actualResponse,
			JSONCompareMode jsonCompareMode) throws Exception 
	{
		JSONCompareResult result;
		try 
		{
			if(actualResponse.equals("[]"))
			{
				 Assert.assertTrue(restApiTestCase.getExpectedResponse().trim().
						 			equals(actualResponse),
						 			"JSON Array Response didn't match. "
											+ LINE_BREAK
											+ String.format("Description: %s",
													restApiTestCase.getDescription())
											+ LINE_BREAK
											+ LINE_BREAK
											+ buildExpectedResponseMessage(restApiTestCase)
											+ LINE_BREAK
											+ buildActualResponseMessageInJson(
													actualResponseStatusCode, actualResponse));
			}
			else 
			{
				if(actualResponse.startsWith("["))
				{
					JSONArray expectedResponse = (JSONArray) JSONParser
							.parseJSON(restApiTestCase.getExpectedResponse());
					JSONArray actualResponseJson = (JSONArray) JSONParser
							.parseJSON(actualResponse);
					result = JSONCompare.compareJSON(
							expectedResponse, actualResponseJson, jsonCompareMode);
				}
				else	
				 result = JSONCompare.compareJSON(
						restApiTestCase.getExpectedResponse(), actualResponse,
						jsonCompareMode);
				
				assertThat("JSON Response didn't match. "
								+ result.getMessage()
								+ LINE_BREAK
								+ String.format("Description: %s",
										restApiTestCase.getDescription())
								+ LINE_BREAK
								+ LINE_BREAK
								+ buildExpectedResponseMessage(restApiTestCase)
								+ LINE_BREAK
								+ buildActualResponseMessageInJson(
										actualResponseStatusCode, actualResponse),
						result.failed(), is(false));
			}
		}
		catch (JSONException e) 
		{
			fail(String.format(
					"Failure on '%s. Could not parse Response into JSON"
							+ LINE_BREAK
							+ LINE_BREAK
							+ buildExpectedResponseMessage(restApiTestCase),
					restApiTestCase.getDescription())
					+ LINE_BREAK
					+ buildActualResponseMessage(actualResponseStatusCode,
							actualResponse));
		}
	}	

	private String buildExpectedResponseMessage(RestApiTestCase restApiTestCase) {
		return String.format("Expected Response: %s Response Code \n  %s",
				restApiTestCase.getExpectedStatusCode(),
				restApiTestCase.getExpectedResponse());
	}

	private String buildActualResponseMessage(int actualResponseCode,
			String actualResponse)
	{
		return String.format("Actual Response: %s Response Code \n  %s",
				actualResponseCode, actualResponse);
	}

	private String buildActualResponseMessageInJson(
			int actualResponseCode, String actualResponse) throws JSONException
	{
		JSONObject json = new JSONObject(actualResponse);
		return String.format("Actual Response: %s Response Code \n  %s",
				actualResponseCode, json.toString(4));

	}

}
