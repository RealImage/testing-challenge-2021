package com.qube.util.dataprovider;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.testng.annotations.DataProvider;
import com.qube.RestApi.RestApiTest;
import com.qube.RestApi.RestApiTestCase;
import com.qube.RestApi.RestApiTestScenarios;



@SuppressWarnings("restriction")
public class TestsDataProvider 
{
	public static final String CONVERT_XML_TO_TEST_CASE_METHOD_NAME = "ConvertXmlToTestCase";
	public static final String FILE_PATH_KEY = "filePath";


	@DataProvider(name = CONVERT_XML_TO_TEST_CASE_METHOD_NAME)
	public static Object[][] convertXmlToTestCase(Method testMethod)
			throws Exception 
	{
		Map<String, String> arguments = DataProviderUtil
				.resolveDataProviderArguments(testMethod);

		JAXBContext jaxbContext = JAXBContext
				.newInstance(RestApiTestScenarios.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		File file = new File(RestApiTest.TEST_DATA_FILE_PATH + arguments.get("filePath"));
		List<RestApiTestCase> toReturnTestCasesList = new ArrayList<RestApiTestCase>();

		RestApiTestScenarios scenarios = (RestApiTestScenarios) jaxbUnmarshaller
				.unmarshal(file);
		Iterator<RestApiTestCase> iterator = scenarios.getTestCases()
				.iterator();
		while (iterator.hasNext()) 
		{
			RestApiTestCase testCase = iterator.next();
			toReturnTestCasesList.add(testCase);
		}

		Object[][] data = new Object[toReturnTestCasesList.size()][1];

		for (int i = 0; i < toReturnTestCasesList.size(); i++) 
		{
			data[i][0] = toReturnTestCasesList.get(i);
		}

		return data;
	}

}
