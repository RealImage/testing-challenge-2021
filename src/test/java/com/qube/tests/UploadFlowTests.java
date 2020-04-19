package com.qube.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import com.qube.RestApi.RestApiTestCase;
import com.qube.tests.base.BaseTest;
import com.qube.util.dataprovider.TestsDataProvider;
import com.qube.util.dataprovider.DataProviderUtil;


public class UploadFlowTests extends BaseTest
{
	protected static Logger logger = LoggerFactory
			.getLogger(UploadFlowTests.class);
	private static final String TEST_DATA_FILE_NAME = "/text-file-upload-completion-flow.xml";
	private static final String IMAGE_FILE_UPLOAD_TEST_DATA = "/image-file-upload-completion-flow.xml";

	
	@Test(description = "file upload flow", dataProvider = TestsDataProvider.CONVERT_XML_TO_TEST_CASE_METHOD_NAME, dataProviderClass = TestsDataProvider.class)
	@DataProviderUtil.DataProviderArguments("filePath=" + TEST_DATA_FILE_NAME)
	public void uploadFlowTests(RestApiTestCase testCase) throws Exception 
	{
		genericTestExecution(testCase);
		
	}
	
	@Test(description = "file upload flow", dataProvider = TestsDataProvider.CONVERT_XML_TO_TEST_CASE_METHOD_NAME, dataProviderClass = TestsDataProvider.class)
	@DataProviderUtil.DataProviderArguments("filePath=" + IMAGE_FILE_UPLOAD_TEST_DATA)
	public void imageUploadFlowTests(RestApiTestCase testCase) throws Exception 
	{
		genericTestExecution(testCase);
	}
	
}
