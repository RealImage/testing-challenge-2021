package com.sharebox.api.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class APIUtils {

	private static final String baseURL = RestAssured.baseURI = PropertyReader.getProperty("baseURL");
	private static final String token1 = PropertyReader.getProperty("token1");
	private static final String token2 = PropertyReader.getProperty("token2");

	private static final String uploadParam = PropertyReader.getProperty("uploadParam");
	private static final String filesParam = PropertyReader.getProperty("filesParam");

	public static RequestSpecification rSpec;
	public static Response response;

	public static Response getFileDetails(String token, String fileId) {
		return RestAssured.given().relaxedHTTPSValidation().queryParam("fileId", fileId).queryParam("token", token)
				.when().get(uploadParam);
	}

	public static Response uploadFile(String token, String name, String size, String hash) throws Exception {
		rSpec = RestAssured.given().relaxedHTTPSValidation();
		response = rSpec.queryParam("token", token).queryParam("name", name).queryParam("size", size)
				.queryParam("hash", hash).when().post(uploadParam);

		if (response.getStatusCode() != 200) {
			throw new Exception("Failed to upload file : " + response.getStatusCode() + " " + response.getStatusLine());
		}

		return response;
	}

	public static Response uploadFile(String name, String size, String hash) throws Exception {
		return uploadFile(token1, name, size, hash);
	}

	public static Response updateStatusOfFile(String fileId, String bytesCompleted) throws Exception {
		rSpec = RestAssured.given().relaxedHTTPSValidation();
		response = rSpec.queryParam("token", token1).queryParams("fileId", fileId)
				.queryParams("bytesCompleted", bytesCompleted).when().put(uploadParam);
		if (response.getStatusCode() != 200) {
			throw new Exception(
					"Failed to update file status : " + response.getStatusCode() + " " + response.getStatusLine());
		}
		return response;

	}

	public static Response getAllFiles() throws Exception {
		return getAllFiles(token1);

	}

	public static Response getAllFiles(String token) throws Exception {
		rSpec = RestAssured.given().relaxedHTTPSValidation();
		response = rSpec.queryParam("token", token).when().get(filesParam);
		if (response.getStatusCode() != 200) {
			throw new Exception(
					"Failed to get all file details : " + response.getStatusCode() + " " + response.getStatusLine());
		}
		return response;

	}

	public static Response getAllFilesInbox() throws Exception {

		return getAllFilesInbox(token1);

	}

	public static Response getAllFilesInbox(String token) throws Exception {
		rSpec = RestAssured.given().relaxedHTTPSValidation();
		response = rSpec.queryParam("token", token).queryParams("getSharedFiles", "null").when().get(filesParam);
		if (response.getStatusCode() != 200) {
			throw new Exception(
					"Failed to get all file details : " + response.getStatusCode() + " " + response.getStatusLine());
		}
		return response;

	}

	public static Response deleteFile(String fileId) throws Exception {
		rSpec = RestAssured.given().relaxedHTTPSValidation();
		response = rSpec.queryParam("token", token1).queryParams("fileId", fileId).when().delete(filesParam);
		if (response.getStatusCode() != 200) {
			throw new Exception("Failed to delete file : " + response.getStatusCode() + " " + response.getStatusLine());
		}
		return response;
	}

	public static Response shareFile(String fileId, String shareTo) throws Exception {
		rSpec = RestAssured.given().relaxedHTTPSValidation();
		response = rSpec.queryParam("token", token1).queryParams("fileId", fileId).queryParam("shareTo", shareTo).when()
				.post(filesParam);
		if (response.getStatusCode() != 200) {
//			throw new Exception("Failed to share file : " + response.getStatusCode() + " " + response.getStatusLine());

			/*
			 * 
			 * (java.lang.String) <html><body><h1>Internal error</h1>Ticket issued: <a href=
			 * "/admin/default/ticket/sharebox/171.78.168.222.2020-01-30.03-19-02.3d9c1955-18c4-4017-9e4f-35369d284185"
			 * target="_blank">sharebox/171.78.168.222.2020-01-30.03-19-02.3d9c1955-18c4-
			 * 4017-9e4f-35369d284185</a></body><!-- this is junk text else IE does not
			 * display the page:
			 */

		}
		return response;
	}

	public static Response acceptOrRejectFile(String token, String fileId, boolean isAccepted) throws Exception {
		rSpec = RestAssured.given().relaxedHTTPSValidation();
		response = rSpec.queryParam("token", token).queryParams("fileId", fileId).queryParam("isAccepted", isAccepted)
				.when().put(filesParam);

		if (response.getStatusCode() != 200) {
			throw new Exception(
					"Failed to accept/reject file : " + response.getStatusCode() + " " + response.getStatusLine());
		}

		return response;
	}

	public static Response acceptOrRejectFile(String fileId, boolean isAccepted) throws Exception {

		return acceptOrRejectFile(token1, fileId, isAccepted);
	}
}
