package com.qube.RestApi;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("restriction")
@XmlRootElement(name = "testCases")
@XmlAccessorType(XmlAccessType.FIELD)
public class RestApiTestScenarios 
{

	@XmlElement(name = "testCase")
	private List<RestApiTestCase> testCases;
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<RestApiTestCase> getTestCases() {
		return testCases;
	}

	public void setTestCases(List<RestApiTestCase> testCases) {
		this.testCases = testCases;
	}

	@Override
	public String toString() {
		return description;
	}

}
