package com.qube.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import com.qube.RestApi.RestApiTestCase;
import com.qube.tests.base.BaseTest;
import com.qube.util.dataprovider.DataProviderUtil;
import com.qube.util.dataprovider.TestsDataProvider;


public class FileShareTests extends BaseTest
{
	protected static Logger logger = LoggerFactory
			.getLogger(FileShareTests.class);
	private static final String file_share_accept_flow = "/file-share-accept-flow.xml";
	private static final String file_share_reject_flow = "/file-share-reject-flow.xml";
	private static final String file_share_pending_file_flow = "/share-pending-file.xml";
	
	@Test(description = "File share accept flow", dataProvider = TestsDataProvider.CONVERT_XML_TO_TEST_CASE_METHOD_NAME, dataProviderClass = TestsDataProvider.class)
	@DataProviderUtil.DataProviderArguments("filePath=" + file_share_accept_flow)
	public void fileShareAcceptFlowTests(RestApiTestCase testCase) 
	{
		genericTestExecution(testCase);
		
	}
	
	@Test(description = "file share reject flow", dataProvider = TestsDataProvider.CONVERT_XML_TO_TEST_CASE_METHOD_NAME, dataProviderClass = TestsDataProvider.class)
	@DataProviderUtil.DataProviderArguments("filePath=" + file_share_reject_flow)
	public void fileShareRejectFlowTests(RestApiTestCase testCase) 
	{
		genericTestExecution(testCase);
		
	}
	
	@Test(description = "share pending file flow", dataProvider = TestsDataProvider.CONVERT_XML_TO_TEST_CASE_METHOD_NAME, dataProviderClass = TestsDataProvider.class)
	@DataProviderUtil.DataProviderArguments("filePath=" + file_share_pending_file_flow)
	public void sharePendingFileTest(RestApiTestCase testCase) 
	{
		genericTestExecution(testCase);
		
	}
	
}
