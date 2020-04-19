package com.qube.tests.base;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import com.jayway.restassured.response.Response;
import com.qube.RestApi.RestApiTest;
import com.qube.RestApi.RestApiTestCase;
import com.qube.RestApi.RestApiTestCase.RequestMethod;
import com.qube.util.JsonUtils;
import com.qube.util.QubeUtil;
import com.qube.util.RestApiTestCaseClient;

public class BaseTest extends RestApiTest{
	protected static RestApiTestCaseClient restApiTestCaseClient = new RestApiTestCaseClient();;
	protected static final String TEST_ENV_CONFIG_FILE = "src/test/resources/config/qube.properties";
	protected static String baseUrl;
	protected static Logger logger = LoggerFactory.getLogger(BaseTest.class);
	protected static String user1Token;
	protected static String user2Token;
	protected static String user1Email;
	protected static String user2Email;
	protected String fileId = null;
	protected File file;
	
	@BeforeMethod
	public void setupMethod(Method method) throws Exception {
		logger.info(String.format("*** TEST_METHOD *** %s()", method.getName()));
	}

	@BeforeSuite
	@Parameters({"testEnv"})
	public void setupSuite(String testEnv) 
	{	
		try
		{
			restApiTestCaseClient = new RestApiTestCaseClient();
			readPropertiesFile(testEnv);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void readPropertiesFile(String testEnv) throws MalformedURLException 
	{
        Properties properties = new Properties();
        try 
        {
            InputStream inputStream = new FileInputStream(TEST_ENV_CONFIG_FILE);
            properties.load(inputStream);
            baseUrl = properties.getProperty(String.format("BASE_URL.%s", testEnv.toUpperCase()));
            user1Token = properties.getProperty(String.format("USER1_TOKEN.%s", testEnv.toUpperCase()));
            user2Token = properties.getProperty(String.format("USER2_TOKEN.%s", testEnv.toUpperCase()));
            user1Email = properties.getProperty(String.format("USER1_EMAIL.%s", testEnv.toUpperCase()));
            user2Email = properties.getProperty(String.format("USER2_EMAIL.%s", testEnv.toUpperCase()));
        } 
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
            Assert.fail(String.format("Could not find config file: %s", TEST_ENV_CONFIG_FILE));
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
            Assert.fail(String.format("Could not read config file: %s", TEST_ENV_CONFIG_FILE));
        }
    }
	
	protected void updateBaseUrlAndUserToken(RestApiTestCase testCase)
	{
		testCase.setBaseUrl(baseUrl);
		if(testCase.getParameters().get("token").equals("#user1Token"))
			testCase.addParameter("token", user1Token);
		else if(testCase.getParameters().get("token").equals("#user2Token"))
			testCase.addParameter("token", user2Token);
	}
	
	protected void validateExpectedFields(RestApiTestCase testCase, String actualResponse) throws JSONException
	{
		Map<String,String> expectedFields = testCase.getExpectedFields();
		for (Map.Entry<String, String> entry : expectedFields.entrySet()) 
		{
			String key = entry.getKey();
			String actualValue = JsonUtils.getValue(actualResponse, key);
			if(entry.getValue().equals("UUID"))
			{
				Assert.assertTrue(QubeUtil.isUUID(actualValue),
						"Error: "+ actualValue +" is not in UUID format");
			}
			else if(entry.getValue().equals("DATETIME"))
			{
				Assert.assertTrue(QubeUtil.isDateTime(actualValue),
						"Error: "+entry.getKey()+" is not in valid Date time format");
			}
			else if(entry.getValue().equals("NOT_NULL"))
			{
				Assert.assertTrue(!actualValue.isEmpty(),
						"Error: "+entry.getKey()+" is empty");
			}
			else
			{
				Assert.assertEquals(actualValue, entry.getValue(), 
						"Error: expected values not matching. Expected - " 
						+ entry.getValue()
						+ " Actual value -" 
						+ actualValue);
			}
		}
	}
	
	public void genericTestExecution(RestApiTestCase testCase)
	{
		try
		{
			logger.info(testCase.getDescription());
			logger.info(testCase.getBaseUrl() + testCase.getUrl());
			
			updateBaseUrlAndUserToken(testCase);
			
			//Updating runtime data before sending the request
			if(testCase.getRequestMethod().equals(RequestMethod.GET))
				testCase.addParameter("fileId", fileId);
			else if(testCase.getRequestMethod().equals(RequestMethod.PUT))
			{
				testCase.addFormData("fileId", fileId);
				if(testCase.getUrl().contains("/upload"))
					testCase.addFormData("bytesCompleted", Long.toString(file.length()));
			}
			else if(testCase.getRequestMethod().equals(RequestMethod.POST) &&
					testCase.getUrl().contains("/files"))
			{
				testCase.addFormData("fileId", fileId);
				if(testCase.getFormData().get("shareTo").equals("#user2Email"))
					testCase.addFormData("shareTo", user2Email);
					
			}
			
			//making api request and capturing the response
			Response response = restApiTestCaseClient.call(testCase);
			String stringResponse = response.asString();
			logger.info("Response = " + stringResponse);
			
			//Fetching fileId to use in following requests
			if(testCase.getRequestMethod().equals(RequestMethod.POST) && 
					testCase.getUrl().contains("/upload"))
			{
				fileId = JsonUtils.fetchFileId(stringResponse);
				file = new File(testCase.getMultipartFormData());
				logger.info("File id = " + fileId);
			}
			
			//****** VALIDATION SECTION **********
			
			//Validating status code with expected status code
			Assert.assertTrue(testCase.getExpectedStatusCode() == response.getStatusCode(),
					String.format("Error: Expected response code =%1$s and Actual response code =%2$s",
							testCase.getExpectedStatusCode(),response.getStatusCode()));
			
			
			//Validating Expected fields
			if(!testCase.getExpectedFields().isEmpty())
				validateExpectedFields(testCase, stringResponse);
			
			// verification
			if(!testCase.getExpectedResponse().isEmpty())
			{
				String expectedResponse = testCase.getExpectedResponse();
				expectedResponse = expectedResponse.replace("#fileName", file.getName());
				expectedResponse = expectedResponse.replace("#fileHash", QubeUtil.calculateSHA256(file));
				expectedResponse = expectedResponse.replace("#fileSize", Long.toString(file.length()));
				expectedResponse = expectedResponse.replace("#fileId", fileId);
				testCase.setExpectedResponse(expectedResponse);
				
				assertResponse(testCase, response.getStatusCode(), stringResponse,
					JSONCompareMode.LENIENT);
			}
		}
		catch(Exception e)
		{
			logger.error("Error executing testcase - " + testCase.getDescription());
			e.printStackTrace();
		}
	}
	
}
