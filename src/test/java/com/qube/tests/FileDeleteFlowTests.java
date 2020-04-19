package com.qube.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import com.qube.RestApi.RestApiTestCase;
import com.qube.tests.base.BaseTest;
import com.qube.util.dataprovider.DataProviderUtil;
import com.qube.util.dataprovider.TestsDataProvider;

public class FileDeleteFlowTests extends BaseTest
{
	protected static Logger logger = LoggerFactory
			.getLogger(FileShareTests.class);
	private static final String delete_file_flow1 = "/delete-file-flow1.xml";
	private static final String delete_file_flow2 = "/delete-file-flow2.xml";
	private static final String delete_pending_file = "/delete-pending-file.xml";
	private static final String delete_another_user_file = "/delete-another-user-file.xml";

	
	@Test(description = "", dataProvider = TestsDataProvider.CONVERT_XML_TO_TEST_CASE_METHOD_NAME, dataProviderClass = TestsDataProvider.class)
	@DataProviderUtil.DataProviderArguments("filePath=" + delete_file_flow1)
	public void deleteFileFlow1(RestApiTestCase testCase) 
	{
		genericTestExecution(testCase);
		
	}
	
	@Test(description = "", dataProvider = TestsDataProvider.CONVERT_XML_TO_TEST_CASE_METHOD_NAME, dataProviderClass = TestsDataProvider.class)
	@DataProviderUtil.DataProviderArguments("filePath=" + delete_file_flow2)
	public void deleteFileFlow2(RestApiTestCase testCase) 
	{
		genericTestExecution(testCase);
		
	}
	
	@Test(description = "", dataProvider = TestsDataProvider.CONVERT_XML_TO_TEST_CASE_METHOD_NAME, dataProviderClass = TestsDataProvider.class)
	@DataProviderUtil.DataProviderArguments("filePath=" + delete_pending_file)
	public void deletePendingFile(RestApiTestCase testCase) 
	{
		genericTestExecution(testCase);
		
	}
	
	@Test(description = "", dataProvider = TestsDataProvider.CONVERT_XML_TO_TEST_CASE_METHOD_NAME, dataProviderClass = TestsDataProvider.class)
	@DataProviderUtil.DataProviderArguments("filePath=" + delete_another_user_file)
	public void deleteAnotherUserFile(RestApiTestCase testCase) 
	{
		genericTestExecution(testCase);
		
	}

}
