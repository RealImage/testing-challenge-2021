package com.sharebox.api.utils;

import static org.testng.Assert.assertEquals;

import org.json.JSONObject;

import io.restassured.response.Response;

public class ShareBoxUtils {

	/**
	 * Uploads new file
	 * 
	 * @return fileId of new file
	 * @throws Exception
	 */
	public static String uploadNewFile() throws Exception {
		int rand = CommonUtil.getRandomNumber();
		Response resp = APIUtils.uploadFile("New File " + rand, "256", "" + rand);
		
		String bodyResponse = resp.getBody().asString();
		int statusCode = resp.getStatusCode();
		System.out.println(statusCode + "  " + bodyResponse);

		assertEquals(resp.getStatusCode(), 200, "Successful response! File Created !");

		JSONObject json = new JSONObject(resp.getBody().asString());

		return json.getString("fileId");

	}

}
