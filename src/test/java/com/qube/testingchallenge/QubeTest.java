package com.qube.testingchallenge;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import io.restassured.response.Response;

public class QubeTest
{
  @BeforeSuite
  public void init() throws IOException {
	  uploadapi = new Sharebox_UploadAPI();
	  filesapi = new Sharebox_FilesAPI();
  }
  @AfterSuite
  public void exit() throws IOException {
	  if(uploadapi != null) {
		  uploadapi.api.afterTest();
		  uploadapi = null; 
	  }
	  if(filesapi != null) {
		  filesapi.api.afterTest();
		  filesapi = null; 
	  }
  }
  Sharebox_UploadAPI uploadapi = null;
  Sharebox_FilesAPI filesapi = null;
  @Test
  public void testGetFiles() throws URISyntaxException {
	  Response resp = filesapi.GET("", null);
	  Assert.assertNotNull(resp);
	  resp.then().statusCode(200);
	  /*
	   * Logging the results using any logger
	   */
//	  System.out.println("Content: "+resp.asString()+" StatusCode -> "+resp.getStatusCode());
  }
  @Test
  public void testUploadFiles() throws URISyntaxException {
	  HashMap<String,String> params = new HashMap<String,String>();
	  getFileDetails(params);
	  Response resp = uploadapi.POST(params.get(ConfigConstants.filename), params.get(ConfigConstants.filesize), params.get(ConfigConstants.filehash), null);
	  Assert.assertNotNull(resp);
	  resp.then().statusCode(200);
	  /*
	   * Logging the results using any logger
	   */
	  //System.out.println("Content: "+resp.asString()+" StatusCode -> "+resp.getStatusCode());
  }
  @Test
  public void testPutUploadFiles() throws URISyntaxException {
	  HashMap<String,String> params = new HashMap<String, String>();
	  getFileDetails(params);
	  Response resp = uploadapi.POST(params.get(ConfigConstants.filename), params.get(ConfigConstants.filesize), params.get(ConfigConstants.filehash), null);
	  Assert.assertNotNull(resp);
	  resp.then().statusCode(200);
	  String fileId = resp.jsonPath().getString(ConfigConstants.fileId);
	  Response putresp = uploadapi.PUT(fileId, params.get(ConfigConstants.filesize), null);
	  Assert.assertNotNull(putresp);
	  putresp.then().statusCode(200);
	  /*
	   * Logging the results using any logger
	   */
	  //System.out.println("Content: "+putresp.asString()+" StatusCode -> "+putresp.getStatusCode());
  }
  
  @Test
  public void testGetUploadFilesSuccessAndUpdateStatusVerification() throws URISyntaxException {
	  HashMap<String,String> params = new HashMap<String, String>();
	  getFileDetails(params);
	  Response resp = uploadapi.POST(params.get(ConfigConstants.filename), params.get(ConfigConstants.filesize), params.get(ConfigConstants.filehash), null);
	  Assert.assertNotNull(resp);
	  resp.then().statusCode(200);
	  String fileId = resp.jsonPath().getString(ConfigConstants.fileId);
	  Response getresp = uploadapi.GET(fileId, null);
	  Assert.assertNotNull(getresp);
	  getresp.then().statusCode(200);
	  Assert.assertEquals(getresp.jsonPath().getString(ConfigConstants.fileId), fileId);
	  Assert.assertEquals(getresp.jsonPath().getString(ConfigConstants.filename), params.get(ConfigConstants.filename));
	  Assert.assertEquals(getresp.jsonPath().getString(ConfigConstants.filesize), params.get(ConfigConstants.filesize));
	  Assert.assertEquals(getresp.jsonPath().getString("fileHash"), params.get(ConfigConstants.filehash));
	  Assert.assertEquals(getresp.jsonPath().getString("status"), "Pending");
	  Response putresp = uploadapi.PUT(fileId, params.get(ConfigConstants.filesize), null);
	  Assert.assertNotNull(putresp);
	  putresp.then().statusCode(200);
	  Response getrespafterupdate = uploadapi.GET(fileId, null);
	  Assert.assertNotNull(getrespafterupdate);
	  getrespafterupdate.then().statusCode(200);
	  Assert.assertEquals(getrespafterupdate.jsonPath().getString("status"), "Completed");
	  /*
	   * Logging the results using any logger
	   */
//	  System.out.println("Content: "+getrespafterupdate.asString()+" StatusCode -> "+getrespafterupdate.getStatusCode());
  }
  
  @Test
  @SuppressWarnings("rawtypes")
  public void testGetUploadFilesSuccessAndShareToOtherUserAccept() throws URISyntaxException {
	  HashMap<String,String> params = new HashMap<String, String>();
	  getFileDetails(params);
	  Response resp = uploadapi.POST(params.get(ConfigConstants.filename), params.get(ConfigConstants.filesize), params.get(ConfigConstants.filehash), null);
	  Assert.assertNotNull(resp);
	  resp.then().statusCode(200);
	  String fileId = resp.jsonPath().getString(ConfigConstants.fileId);
	  Response getresp = uploadapi.GET(fileId, null);
	  Assert.assertNotNull(getresp);
	  getresp.then().statusCode(200);
	  Assert.assertEquals(getresp.jsonPath().getString(ConfigConstants.fileId), fileId);
	  Assert.assertEquals(getresp.jsonPath().getString(ConfigConstants.filename), params.get(ConfigConstants.filename));
	  Assert.assertEquals(getresp.jsonPath().getString(ConfigConstants.filesize), params.get(ConfigConstants.filesize));
	  Assert.assertEquals(getresp.jsonPath().getString("fileHash"), params.get(ConfigConstants.filehash));
	  Assert.assertEquals(getresp.jsonPath().getString("status"), "Pending");
	  Response putresp = uploadapi.PUT(fileId, params.get(ConfigConstants.filesize), null);
	  Assert.assertNotNull(putresp);
	  putresp.then().statusCode(200);
	  Response getrespafterupdate = uploadapi.GET(fileId, null);
	  Assert.assertNotNull(getrespafterupdate);
	  getrespafterupdate.then().statusCode(200);
	  Assert.assertEquals(getrespafterupdate.jsonPath().getString("status"), "Completed");
	  Response getinboxrespbfreshare = filesapi.GET("true", filesapi.api.prop.getProperty(ConfigConstants.user2Token));
	  Assert.assertNotNull(getinboxrespbfreshare);
	  getinboxrespbfreshare.then().statusCode(200);
	  int ibcountbfreshare = ((List) getinboxrespbfreshare.jsonPath().get()).size();
	  Response getrespbfreshare = filesapi.GET("", filesapi.api.prop.getProperty(ConfigConstants.user2Token));
	  Assert.assertNotNull(getrespbfreshare);
	  getrespbfreshare.then().statusCode(200);
	  int countbfreshare = ((List) getrespbfreshare.jsonPath().get()).size();
	  Response shareresp = filesapi.POST(fileId, filesapi.api.prop.getProperty(ConfigConstants.userId2), null);
	  Assert.assertNotNull(shareresp);
	  shareresp.then().statusCode(200);
	  Response getinboxrespaftrshare = filesapi.GET("true", filesapi.api.prop.getProperty(ConfigConstants.user2Token));
	  Assert.assertNotNull(getinboxrespaftrshare);
	  getinboxrespaftrshare.then().statusCode(200);
	  int ibcountaftrshare = ((List) getinboxrespaftrshare.jsonPath().get()).size();
	  Response getrespaftrshare = filesapi.GET("", filesapi.api.prop.getProperty(ConfigConstants.user2Token));
	  Assert.assertNotNull(getrespaftrshare);
	  getrespaftrshare.then().statusCode(200);
	  int countaftrshare = ((List) getrespaftrshare.jsonPath().get()).size();
	  Assert.assertTrue(ibcountaftrshare > ibcountbfreshare);
	  Assert.assertTrue(countaftrshare == countbfreshare);
	  Response updtshareresp = filesapi.PUT(fileId,true,filesapi.api.prop.getProperty(ConfigConstants.user2Token));
	  Assert.assertNotNull(updtshareresp);
	  shareresp.then().statusCode(200);
	  Response getinboxrespaftraccept = filesapi.GET("true", filesapi.api.prop.getProperty(ConfigConstants.user2Token));
	  Assert.assertNotNull(getinboxrespaftraccept);
	  getinboxrespaftraccept.then().statusCode(200);
	  int ibcountaftraccept = ((List) getinboxrespaftraccept.jsonPath().get()).size();
	  Response getrespaftraccept = filesapi.GET("", filesapi.api.prop.getProperty(ConfigConstants.user2Token));
	  Assert.assertNotNull(getrespaftraccept);
	  getrespaftraccept.then().statusCode(200);
	  int countaftraccept = ((List) getrespaftraccept.jsonPath().get()).size();
	  Assert.assertTrue(ibcountaftraccept == ibcountbfreshare);
	  Assert.assertTrue(countaftraccept > countaftrshare);
	  /*
	   * Logging the results using any logger
	   */
//	  System.out.println("Content: "+getrespafterupdate.asString()+" StatusCode -> "+getrespafterupdate.getStatusCode());
  }
  
  @Test
  @SuppressWarnings("rawtypes")
  public void testGetUploadFilesSuccessAndShareToOtherUserReject() throws URISyntaxException {
	  HashMap<String,String> params = new HashMap<String, String>();
	  getFileDetails(params);
	  Response resp = uploadapi.POST(params.get(ConfigConstants.filename), params.get(ConfigConstants.filesize), params.get(ConfigConstants.filehash), null);
	  Assert.assertNotNull(resp);
	  resp.then().statusCode(200);
	  String fileId = resp.jsonPath().getString(ConfigConstants.fileId);
	  Response getresp = uploadapi.GET(fileId, null);
	  Assert.assertNotNull(getresp);
	  getresp.then().statusCode(200);
	  Assert.assertEquals(getresp.jsonPath().getString(ConfigConstants.fileId), fileId);
	  Assert.assertEquals(getresp.jsonPath().getString(ConfigConstants.filename), params.get(ConfigConstants.filename));
	  Assert.assertEquals(getresp.jsonPath().getString(ConfigConstants.filesize), params.get(ConfigConstants.filesize));
	  Assert.assertEquals(getresp.jsonPath().getString("fileHash"), params.get(ConfigConstants.filehash));
	  Assert.assertEquals(getresp.jsonPath().getString("status"), "Pending");
	  Response putresp = uploadapi.PUT(fileId, params.get(ConfigConstants.filesize), null);
	  Assert.assertNotNull(putresp);
	  putresp.then().statusCode(200);
	  Response getrespafterupdate = uploadapi.GET(fileId, null);
	  Assert.assertNotNull(getrespafterupdate);
	  getrespafterupdate.then().statusCode(200);
	  Assert.assertEquals(getrespafterupdate.jsonPath().getString("status"), "Completed");
	  Response getinboxrespbfreshare = filesapi.GET("true", filesapi.api.prop.getProperty(ConfigConstants.user2Token));
	  Assert.assertNotNull(getinboxrespbfreshare);
	  getinboxrespbfreshare.then().statusCode(200);
	  int ibcountbfreshare = ((List) getinboxrespbfreshare.jsonPath().get()).size();
	  Response getrespbfreshare = filesapi.GET("", filesapi.api.prop.getProperty(ConfigConstants.user2Token));
	  Assert.assertNotNull(getrespbfreshare);
	  getrespbfreshare.then().statusCode(200);
	  int countbfreshare = ((List) getrespbfreshare.jsonPath().get()).size();
	  Response shareresp = filesapi.POST(fileId, filesapi.api.prop.getProperty(ConfigConstants.userId2), null);
	  Assert.assertNotNull(shareresp);
	  shareresp.then().statusCode(200);
	  Response getinboxrespaftrshare = filesapi.GET("true", filesapi.api.prop.getProperty(ConfigConstants.user2Token));
	  Assert.assertNotNull(getinboxrespaftrshare);
	  getinboxrespaftrshare.then().statusCode(200);
	  int ibcountaftrshare = ((List) getinboxrespaftrshare.jsonPath().get()).size();
	  Response getrespaftrshare = filesapi.GET("", filesapi.api.prop.getProperty(ConfigConstants.user2Token));
	  Assert.assertNotNull(getrespaftrshare);
	  getrespaftrshare.then().statusCode(200);
	  int countaftrshare = ((List) getrespaftrshare.jsonPath().get()).size();
	  Assert.assertTrue(ibcountaftrshare > ibcountbfreshare);
	  Assert.assertTrue(countaftrshare == countbfreshare);
	  Response updtshareresp = filesapi.PUT(fileId,false,filesapi.api.prop.getProperty(ConfigConstants.user2Token));
	  Assert.assertNotNull(updtshareresp);
	  shareresp.then().statusCode(200);
	  Response getinboxrespaftrreject = filesapi.GET("true", filesapi.api.prop.getProperty(ConfigConstants.user2Token));
	  Assert.assertNotNull(getinboxrespaftrreject);
	  getinboxrespaftrreject.then().statusCode(200);
	  int ibcountaftrreject = ((List) getinboxrespaftrreject.jsonPath().get()).size();
	  Response getrespaftrreject = filesapi.GET("", filesapi.api.prop.getProperty(ConfigConstants.user2Token));
	  Assert.assertNotNull(getrespaftrreject);
	  getrespaftrreject.then().statusCode(200);
	  int countaftrreject = ((List) getrespaftrreject.jsonPath().get()).size();
	  Assert.assertTrue(ibcountaftrreject == ibcountbfreshare);
	  Assert.assertTrue(countaftrreject == countaftrshare);
	  /*
	   * Logging the results using any logger
	   */
//	  System.out.println("Content: "+getrespaftraccept.asString()+" StatusCode -> "+getrespaftraccept.getStatusCode());
  }
  
  @SuppressWarnings("rawtypes")
  @Test
  public void testDeleteFiles() throws URISyntaxException {
	  Response getresp = filesapi.GET("", null);
	  Assert.assertNotNull(getresp);
	  getresp.then().statusCode(200);
	  int getrespcount = ((List) getresp.jsonPath().get()).size();
	  if(getrespcount == 0) {
		  HashMap<String,String> params = new HashMap<String, String>();
		  getFileDetails(params);
		  Response resp = uploadapi.POST(params.get(ConfigConstants.filename), params.get(ConfigConstants.filesize), params.get(ConfigConstants.filehash), null);
		  Assert.assertNotNull(resp);
		  resp.then().statusCode(200);  
	  }
	  Response getrespaftrupld = filesapi.GET("", null);
	  Assert.assertNotNull(getrespaftrupld);
	  getrespaftrupld.then().statusCode(200);
	  List files = ((List) getrespaftrupld.jsonPath().get());
	  for(Object file : files) {
		 JSONObject f = new JSONObject((Map) file);
		 String fileId = f.getString(ConfigConstants.fileId);
         Response resp = filesapi.DELETE(fileId, null);
		 Assert.assertNotNull(resp);
		 resp.then().statusCode(200);
	  }
	  Response getrespaftrdel = filesapi.GET("", null);
	  Assert.assertNotNull(getrespaftrdel);
	  getrespaftrdel.then().statusCode(200);
	  int getrespaftrdelcount = ((List) getrespaftrdel.jsonPath().get()).size();
	  Assert.assertEquals(getrespaftrdelcount, 0);
	  /*
	   * Logging the results using any logger
	   */
//	  System.out.println("Content: "+getrespaftrdel.asString()+" StatusCode -> "+getrespaftrdel.getStatusCode());
  }
  @Test
  public void testGetFilesInvalidToken() throws URISyntaxException {
	  Response resp = filesapi.GET("", "7667887677567576");
	  Assert.assertNotNull(resp);
	  resp.then().statusCode(401);
	  /*
	   * Logging the results using any logger
	   */
	  //  System.out.println("Content: "+resp.asString()+" StatusCode -> "+resp.getStatusCode());
  }
  @Test
  public void testGetUploadFileNegativeScenarios() throws URISyntaxException {
	  HashMap<String,String> params = new HashMap<String, String>();
	  getFileDetails(params);
	  Response resp = uploadapi.POST(params.get(ConfigConstants.filename), params.get(ConfigConstants.filesize), params.get(ConfigConstants.filehash), null);
	  Assert.assertNotNull(resp);
	  resp.then().statusCode(200);
	  String fileId = resp.jsonPath().getString(ConfigConstants.fileId);
	  Response getInvalidtokenresp = uploadapi.GET(fileId, "7667887677567576");
	  Assert.assertNotNull(getInvalidtokenresp);
	  getInvalidtokenresp.then().statusCode(401);
	  Response nofileIdresp = uploadapi.GET("", null);
	  Assert.assertNotNull(nofileIdresp);
	  nofileIdresp.then().statusCode(400);
	  Response getInvalidfileIdresp = uploadapi.GET("8976877896786", null);
	  Assert.assertNotNull(getInvalidfileIdresp);
	  getInvalidfileIdresp.then().statusCode(404);//It should be 404 not found as per REST standard
	  /*
	   * Logging the results using any logger
	   */
	  //  System.out.println("Content: "+getInvalidfileIdresp.asString()+" StatusCode -> "+getInvalidfileIdresp.getStatusCode());
  }
  @Test
  public void testPostUploadFileNegativeScenarios() throws URISyntaxException {
	  HashMap<String,String> params = new HashMap<String, String>();
	  getFileDetails(params);
	  Response sizeaboveresp = uploadapi.POST(params.get(ConfigConstants.filename), "987654322345678", params.get(ConfigConstants.filehash), null);
	  Assert.assertNotNull(sizeaboveresp);
	  sizeaboveresp.then().statusCode(400);
	  Response nonameresp = uploadapi.POST(null, params.get(ConfigConstants.filesize), params.get(ConfigConstants.filehash), null);
	  Assert.assertNotNull(nonameresp);
	  nonameresp.then().statusCode(400);
	  Response nosizeresp = uploadapi.POST(params.get(ConfigConstants.filename), null, params.get(ConfigConstants.filehash), null);
	  Assert.assertNotNull(nosizeresp);
	  nosizeresp.then().statusCode(400);
	  Response nohashresp = uploadapi.POST(params.get(ConfigConstants.filename), params.get(ConfigConstants.filesize), null, null);
	  Assert.assertNotNull(nohashresp);
	  nohashresp.then().statusCode(400);
	  Response invalidtokenresp = uploadapi.POST(params.get(ConfigConstants.filename), params.get(ConfigConstants.filesize), params.get(ConfigConstants.filehash), "909879878");
	  Assert.assertNotNull(invalidtokenresp);
	  invalidtokenresp.then().statusCode(401);
	  Response resp = uploadapi.POST(params.get(ConfigConstants.filename), params.get(ConfigConstants.filesize), params.get(ConfigConstants.filehash), null);
	  Assert.assertNotNull(resp);
	  resp.then().statusCode(200);
//	  System.out.println("Content: "+resp.asString()+" StatusCode -> "+resp.getStatusCode());
	  String fileId = resp.jsonPath().getString(ConfigConstants.fileId);
	  String existinghash = params.get(ConfigConstants.filehash);
	  String existingName = params.get(ConfigConstants.filename);
	  getFileDetails(params);
	  Response samehashresp = uploadapi.POST(params.get(ConfigConstants.filename), params.get(ConfigConstants.filesize), existinghash, null);
	  Assert.assertNotNull(samehashresp);
	  samehashresp.then().statusCode(200);
	  Assert.assertEquals(samehashresp.jsonPath().getString(ConfigConstants.fileId), fileId);
	  Response samenameresp = uploadapi.POST(existingName, params.get(ConfigConstants.filesize), params.get(ConfigConstants.filehash), null);
	  Assert.assertNotNull(samenameresp);
	  samenameresp.then().statusCode(200);
	  Assert.assertNotEquals(samenameresp.jsonPath().getString(ConfigConstants.fileId), fileId);
	  /*
	   * Logging the results using any logger
	   */
//	    System.out.println("Content: "+samehashresp.asString()+" StatusCode -> "+samehashresp.getStatusCode());
  }
  @Test
  public void testPutUploadFileNegativeScenarios() throws URISyntaxException {
	  HashMap<String,String> params = new HashMap<String, String>();
	  getFileDetails(params);
	  Response postresp = uploadapi.POST(params.get(ConfigConstants.filename), params.get(ConfigConstants.filesize), params.get(ConfigConstants.filehash), null);
	  Assert.assertNotNull(postresp);
	  postresp.then().statusCode(200);
	  String fileId = postresp.jsonPath().getString(ConfigConstants.fileId);
	  Response invalidtokenresp = uploadapi.PUT(fileId, params.get(ConfigConstants.filesize), "9897897676");
	  Assert.assertNotNull(invalidtokenresp);
	  invalidtokenresp.then().statusCode(401);
	  Response nofileIdresp = uploadapi.PUT(null, params.get(ConfigConstants.filesize), null);
	  Assert.assertNotNull(nofileIdresp);
	  nofileIdresp.then().statusCode(400);
	  Response nobytesCompletedresp = uploadapi.PUT(fileId, null, null);
	  Assert.assertNotNull(nobytesCompletedresp);
	  nobytesCompletedresp.then().statusCode(400);
	  Response invalidfileIdresp = uploadapi.PUT("987897897", params.get(ConfigConstants.filesize), null);
	  Assert.assertNotNull(invalidfileIdresp);
	  invalidfileIdresp.then().statusCode(404);//It should be 404
	  int size = Integer.parseInt(params.get(ConfigConstants.filesize));
	  Response sizebelowresp = uploadapi.PUT(fileId, String.valueOf(size-10), null);
	  Assert.assertNotNull(sizebelowresp);
	  sizebelowresp.then().statusCode(200);
	  Response uploadgetresp = uploadapi.GET(fileId, null);
	  Assert.assertNotNull(uploadgetresp);
	  uploadgetresp.then().statusCode(200);
	  Assert.assertEquals(uploadgetresp.jsonPath().getString("status"), "Pending");
	  Assert.assertEquals(uploadgetresp.jsonPath().getString("bytesCompleted"), String.valueOf(size-10));
	  Response sizeaboveresp = uploadapi.PUT(fileId, String.valueOf(size+10), null);
	  Assert.assertNotNull(sizeaboveresp);
	  sizeaboveresp.then().statusCode(400);
	  /*
	   * Logging the results using any logger
	   */
//	    System.out.println("Content: "+sizeaboveresp.asString()+" StatusCode -> "+sizeaboveresp.getStatusCode());
  }
  @Test
  public void testDelFileNegativeScenarios() throws URISyntaxException {
	  HashMap<String,String> params = new HashMap<String, String>();
	  getFileDetails(params);
	  Response postresp = uploadapi.POST(params.get(ConfigConstants.filename), params.get(ConfigConstants.filesize), params.get(ConfigConstants.filehash), null);
	  Assert.assertNotNull(postresp);
	  postresp.then().statusCode(200);
	  String fileId = postresp.jsonPath().getString(ConfigConstants.fileId);
	  Response invalidtokenresp = filesapi.DELETE(fileId, "9897897676");
	  Assert.assertNotNull(invalidtokenresp);
	  invalidtokenresp.then().statusCode(401);
	  Response nofileIdresp = filesapi.DELETE(null, null);
	  Assert.assertNotNull(nofileIdresp);
	  nofileIdresp.then().statusCode(400);
	  Response invalidfileIdresp = filesapi.DELETE("987897897",null);
	  Assert.assertNotNull(invalidfileIdresp);
	  invalidfileIdresp.then().statusCode(404);//It should be 404
	  /*
	   * Logging the results using any logger
	   */
//	    System.out.println("Content: "+invalidfileIdresp.asString()+" StatusCode -> "+invalidfileIdresp.getStatusCode());
  }
  @Test
  public void testPostFileNegativeScenarios() throws URISyntaxException {
	  HashMap<String,String> params = new HashMap<String, String>();
	  getFileDetails(params);
	  Response postresp = uploadapi.POST(params.get(ConfigConstants.filename), params.get(ConfigConstants.filesize), params.get(ConfigConstants.filehash), null);
	  Assert.assertNotNull(postresp);
	  postresp.then().statusCode(200);
	  String fileId = postresp.jsonPath().getString(ConfigConstants.fileId);
	  Response invalidtokenresp = filesapi.POST(fileId,filesapi.api.prop.getProperty(ConfigConstants.userId2), "9897897676");
	  Assert.assertNotNull(invalidtokenresp);
	  invalidtokenresp.then().statusCode(401);
	  Response nofileIdresp = filesapi.POST(null,filesapi.api.prop.getProperty(ConfigConstants.userId2), null);
	  Assert.assertNotNull(nofileIdresp);
	  nofileIdresp.then().statusCode(400);
	  Response invalidfileIdresp = filesapi.POST("0987654567",filesapi.api.prop.getProperty(ConfigConstants.userId2),null);
	  Assert.assertNotNull(invalidfileIdresp);
	  invalidfileIdresp.then().statusCode(404);//It should be 404
//	  System.out.println("Content: "+invalidfileIdresp.asString()+" StatusCode -> "+invalidfileIdresp.getStatusCode());
	  Response noshareToresp = filesapi.POST(fileId,null, null);
	  Assert.assertNotNull(noshareToresp);
	  noshareToresp.then().statusCode(400);
	  Response invalidshareToresp = filesapi.POST(fileId,"123987787667",null);
	  Assert.assertNotNull(invalidshareToresp);
	  invalidshareToresp.then().statusCode(404);//It should be 404
	  /*
	   * Logging the results using any logger
	   */
//	    System.out.println("Content: "+invalidshareToresp.asString()+" StatusCode -> "+invalidshareToresp.getStatusCode());
  }
  @Test
  public void testPutFileNegativeScenarios() throws URISyntaxException {
	  HashMap<String,String> params = new HashMap<String, String>();
	  getFileDetails(params);
	  Response postresp = uploadapi.POST(params.get(ConfigConstants.filename), params.get(ConfigConstants.filesize), params.get(ConfigConstants.filehash), null);
	  Assert.assertNotNull(postresp);
	  postresp.then().statusCode(200);
	  String fileId = postresp.jsonPath().getString(ConfigConstants.fileId);
	  Response invalidtokenresp = filesapi.PUT(fileId, false, "9897897676");
	  Assert.assertNotNull(invalidtokenresp);
	  invalidtokenresp.then().statusCode(401);
	  Response nofileIdresp = filesapi.PUT(null, true, null);
	  Assert.assertNotNull(nofileIdresp);
	  nofileIdresp.then().statusCode(400);
	  Response invalidfileIdresp = filesapi.PUT("0987654567", false, null);
	  Assert.assertNotNull(invalidfileIdresp);
	  invalidfileIdresp.then().statusCode(404);
	  /*
	   * Logging the results using any logger
	   */
//	    System.out.println("Content: "+invalidfileIdresp.asString()+" StatusCode -> "+invalidfileIdresp.getStatusCode());
  }
  private void getFileDetails(HashMap<String,String> params) {
	params.put(ConfigConstants.filename,"test"+System.currentTimeMillis()+".txt");
	params.put(ConfigConstants.filehash,""+System.currentTimeMillis());
	int max = 10000, min=10;
	int randomInt = (int)(Math.random() * ((max - min) + 1)) + min;
	params.put(ConfigConstants.filesize,""+randomInt);
  }
}
