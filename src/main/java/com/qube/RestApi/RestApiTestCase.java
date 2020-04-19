package com.qube.RestApi;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import org.apache.commons.lang.StringUtils;


/* 
 * JAXB unmarshaller unmarshals the testcase segment from the testdata xml file 
 * and maps it to a object of this class.
 * This class object will be sent as input for the test by dataprovider.
 */
@SuppressWarnings("restriction")
@XmlAccessorType(XmlAccessType.FIELD)
public class RestApiTestCase 
{
	private String url;
	private String baseUrl = StringUtils.EMPTY;
	private String description;
	private RequestMethod requestMethod;
	private Map<String, String> formData = new HashMap<String, String>();
	private String multipartFormData = StringUtils.EMPTY;
	private int expectedStatusCode;
	private String expectedResponse = StringUtils.EMPTY;
	private Map<String, String> headers = new HashMap<String, String>();
	private Map<String, String> parameters = new HashMap<String, String>();
	private Map<String, String> expectedFields = new HashMap<String, String>();

	/**
	 * Header values that need to be looked up with begin with "{" and end with
	 * "}"
	 */
	public static boolean isHeaderValueVariable(String headerValue) {
		if (StringUtils.isBlank(headerValue)) {
			return false;
		}
		return headerValue.startsWith("{") && headerValue.endsWith("}");
	}

	public static String getTrimmedHeaderValue(String headerValue) {
		if (StringUtils.isBlank(headerValue)) {
			return StringUtils.EMPTY;
		}

		if (isHeaderValueVariable(headerValue)) {
			return headerValue.substring(1, headerValue.length() - 1);
		}
		return headerValue;
	}

	public String getMultipartFormData() {
		return multipartFormData;
	}

	public void setMultipartFormData(String multipartFormData) {
		this.multipartFormData = multipartFormData;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public RequestMethod getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(RequestMethod requestMethod) {
		this.requestMethod = requestMethod;
	}

	public Map<String, String> getFormData() {
		return formData;
	}

	public void setFormData(Map<String, String> formData) {
		this.formData = formData;
	}
	
	public void addFormData(String key, String value)
	{
		this.formData.put(key, value);
	}
	
	public void addParameter(String key, String value)
	{
		this.parameters.put(key, value);
	}

	public int getExpectedStatusCode() {
		return expectedStatusCode;
	}

	public void setExpectedStatusCode(int expectedStatusCode) {
		this.expectedStatusCode = expectedStatusCode;
	}

	public String getExpectedResponse() {
		return expectedResponse;
	}

	public void setExpectedResponse(String expectedResponse) {
		this.expectedResponse = expectedResponse;
	}

	public Map<String, String> getExpectedFields() {
		return expectedFields;
	}

	public void setExpectedFields(Map<String, String> expectedFields) {
		this.expectedFields = expectedFields;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	
	public String getFullUrl() {
		return getBaseUrl() + getUrl();
	}
	
	@Override
	public String toString() {
		return description;
	}

	@XmlEnum
	public enum RequestMethod {
		@XmlEnumValue("GET")
		GET, @XmlEnumValue("POST")
		POST, @XmlEnumValue("PUT")
		PUT, @XmlEnumValue("DELETE")
		DELETE;
	}
}
