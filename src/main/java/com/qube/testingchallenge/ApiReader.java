package com.qube.testingchallenge;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.testng.annotations.AfterSuite;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiReader {
	
	public ApiReader(){
	}
	
//	private final static String ConfigFilePath = ApiReader.class.getResource(ConfigConstants.configFileName).getFile();
	private final static File ConfigFile = new File(ConfigConstants.configFilePath);
	public Properties prop = new Properties();
	@AfterSuite
	public void afterTest() throws IOException 
	{
		if(this.prop != null) {
			this.prop = null;
		}
		RestAssured.baseURI = null;
		RestAssured.basePath = null;
	}
	
	public void beforeTest() throws IOException 
	{
		//Load properties file		
//		File file = new File(ConfigFilePath);		  
	    FileInputStream fileInput = null;
		try 
		{
			fileInput = new FileInputStream(ConfigFile);
			this.prop.load(fileInput);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		try {
			setUp();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setDefaultQueryParams(RequestSpecification httpreq) {
		httpreq.queryParam(ConfigConstants.queryParamToken, this.prop.getProperty(ConfigConstants.user1Token));
	}
	public void setUp() throws IOException
	{
//		beforeTest();
		RestAssured.baseURI = System.getProperty(ConfigConstants.domain) != null ? System.getProperty(ConfigConstants.domain) : this.prop.getProperty(ConfigConstants.domain);
		RestAssured.basePath = this.prop.getProperty(ConfigConstants.basePath);
		RestAssured.port=443;
		if(!Boolean.parseBoolean(this.prop.getProperty(ConfigConstants.sslEnabled))) {
			RestAssured.useRelaxedHTTPSValidation();
		}
	}

	public void setDefaultHeaders(RequestSpecification httpreq) {
		httpreq.accept(ContentType.ANY).contentType(ContentType.ANY).urlEncodingEnabled(true).header("Referer", this.prop.getProperty(ConfigConstants.domain));
	}
	public Response get(String path, HashMap<String,Object> headers, HashMap<String,Object> queryParams,HashMap<String,String> pathParams,HashMap<String, Object> params,String sessionId) {
		Response toRet = null;
		try {
			RequestSpecification httpreq = RestAssured.given();
			setDefaultHeaders(httpreq);
			if(!queryParams.containsKey(ConfigConstants.queryParamToken)) {
				setDefaultQueryParams(httpreq);
			}
			buildRequest(headers, queryParams, pathParams, params, sessionId, httpreq,null,null);
			toRet =  httpreq.when().get(path);
		}
		catch(Exception e)
		{
			 System.out.println("Error in generating Response : "+e.getMessage());
			 e.printStackTrace();
		}
		return toRet;
	}

	private void buildRequest(HashMap<String, Object> headers, HashMap<String, Object> queryParams,
			HashMap<String, String> pathParams, HashMap<String, Object> params, String sessionId, RequestSpecification httpreq, Object requestBody, String contenttype) {
		if (headers != null && !headers.isEmpty()) {
			httpreq.headers(headers);
		}
		if (queryParams != null && !queryParams.isEmpty()) {
			httpreq.queryParams(queryParams);
		}
		if(pathParams != null && !pathParams.isEmpty()) {
			httpreq.pathParams(pathParams);
		}
		if(sessionId != null && !sessionId.isEmpty()) {
			httpreq.sessionId(sessionId);
		}
		if(requestBody != null) {
			httpreq.body(requestBody);
		}
		if(contenttype != null && !contenttype.isEmpty()) {
			httpreq.contentType(contenttype);
		}
		if(params != null && !params.isEmpty()) {
			httpreq.params(params);
		}
	}

	public Response post(String path, HashMap<String,Object> headers, HashMap<String,Object> queryParams,HashMap<String,String> pathParams,HashMap<String,Object> params,String sessionId,Object requestBody,String contenttype) {
		Response toRet = null;
		try {
			RequestSpecification httpreq = RestAssured.given();
			setDefaultHeaders(httpreq);
			if(!queryParams.containsKey(ConfigConstants.queryParamToken)) {
				setDefaultQueryParams(httpreq);
			}
			buildRequest(headers, queryParams, pathParams , params, sessionId, httpreq,requestBody,contenttype);
			if(contenttype.equalsIgnoreCase("multipart/form-data")) {
				httpreq.contentType("multipart/form-data; boundary=--MyBoundary");
				httpreq.config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs("multipart/form-data", ContentType.TEXT)));
			}
			toRet =  httpreq.when().post(path);
		}
		catch(Exception e)
		{
			 System.out.println("Error in generating Response : "+e.getMessage());
			 e.printStackTrace();
		}
		return toRet;
	}
	public Response put(String path, HashMap<String,Object> headers, HashMap<String,Object> queryParams,HashMap<String,String> pathParams,HashMap<String, Object> params,String sessionId,Object requestBody,String contenttype) {
		Response toRet = null;
		try {
			RequestSpecification httpreq = RestAssured.given();
			setDefaultHeaders(httpreq);
			if(!queryParams.containsKey(ConfigConstants.queryParamToken)) {
				setDefaultQueryParams(httpreq);
			}
			buildRequest(headers, queryParams, pathParams, params,sessionId, httpreq,requestBody,contenttype);
			if(contenttype.equalsIgnoreCase("multipart/form-data")) {
				httpreq.contentType("multipart/form-data; boundary=--MyBoundary");
				httpreq.config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs("multipart/form-data", ContentType.TEXT)));
			}
			toRet =  httpreq.when().put(path);
		}
		catch(Exception e)
		{
			 System.out.println("Error in generating Response : "+e.getMessage());
			 e.printStackTrace();
		}
		return toRet;
	}
	public Response patch(String path, HashMap<String,Object> headers, HashMap<String,Object> queryParams,HashMap<String,String> pathParams,HashMap<String, Object> params,String sessionId,Object requestBody,String contenttype) {
		Response toRet = null;
		try {
			RequestSpecification httpreq = RestAssured.given();
			setDefaultHeaders(httpreq);
			if(!queryParams.containsKey(ConfigConstants.queryParamToken)) {
				setDefaultQueryParams(httpreq);
			}
			buildRequest(headers, queryParams, pathParams, params, sessionId, httpreq,requestBody,contenttype);
			if(contenttype.equalsIgnoreCase("multipart/form-data")) {
				httpreq.contentType("multipart/form-data; boundary=--MyBoundary");
				httpreq.config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs("multipart/form-data", ContentType.TEXT)));
			}
			toRet =  httpreq.when().patch(path);
		}
		catch(Exception e)
		{
			 System.out.println("Error in generating Response : "+e.getMessage());
			 e.printStackTrace();
		}
		return toRet;
	}
	
	public Response delete(String path, HashMap<String,Object> headers, HashMap<String,Object> queryParams,HashMap<String,String> pathParams,HashMap<String, Object> params,String sessionId,Object requestBody,String contenttype) {
		Response toRet = null;
		try {
			RequestSpecification httpreq = RestAssured.given();
			setDefaultHeaders(httpreq);
			if(!queryParams.containsKey(ConfigConstants.queryParamToken)) {
				setDefaultQueryParams(httpreq);
			}
			buildRequest(headers, queryParams, pathParams, params, sessionId, httpreq,requestBody,contenttype);
			if(contenttype.equalsIgnoreCase("multipart/form-data")) {
				httpreq.contentType("multipart/form-data; boundary=--MyBoundary");
				httpreq.config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs("multipart/form-data", ContentType.TEXT)));
			}
			toRet =  httpreq.when().delete(path);
		}
		catch(Exception e)
		{
			 System.out.println("Error in generating Response : "+e.getMessage());
			 e.printStackTrace();
		}
		return toRet;
	}
}

