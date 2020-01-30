package com.sharebox.api.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.sharebox.api.utils.APIUtils;
import com.sharebox.api.utils.CommonUtil;
import com.sharebox.api.utils.PropertyReader;
import com.sharebox.api.utils.ShareBoxUtils;

import io.restassured.response.Response;

public class APITests {

	private static final String token1 = PropertyReader.getProperty("token1");
	private static final String token2 = PropertyReader.getProperty("token2");

	String rand;
	String fileName;
	String fileSize;
	String fileId1;

	@Test()
	public void uploadFileTest() throws Exception {

		String rand1 = rand = "" + CommonUtil.getRandomNumber();
		String fileName = this.fileName = "Sample file " + rand1;
		String fileSize = this.fileSize = "" + new Random().nextInt(1000) + 1;

		try {
			Response resp = APIUtils.uploadFile(fileName, fileSize, rand1);
			String bodyResponse = resp.getBody().asString();
			int statusCode = resp.getStatusCode();
			System.out.println(statusCode + "  " + bodyResponse);

			assertTrue(statusCode == 200, "File Successfully Uploaded");

			String fileIdUploaded = fileId1 = new JSONObject(bodyResponse).getString("fileId");
			Response getResponse = APIUtils.getFileDetails(token1, fileIdUploaded);
			JSONObject json = new JSONObject(getResponse.getBody().asString());

			assertEquals(json.getString("name"), fileName, "File Name verified!");
			assertEquals(json.getString("fileHash"), rand1, "File Hash verified!");
			assertEquals("" + json.getInt("size"), fileSize, "File Size verified!");

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

	}

	@Test(dependsOnMethods = { "uploadFileTest" })
	public void getUploadedFilesTest() throws Exception {

		try {
			Response resp = APIUtils.getAllFiles();

			assertTrue(verifyFileIdInJSONArray(resp, fileId1), "Created file is not present from all files");

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

	}

	@Test()
	public void deleteFileTest() throws Exception {

		try {
			String fileNew = ShareBoxUtils.uploadNewFile();
			Response resp = APIUtils.getAllFiles();

			assertTrue(verifyFileIdInJSONArray(resp, fileNew), "Created file is not present in all files");

			resp = APIUtils.deleteFile(fileNew);

			resp = APIUtils.getAllFiles();

			assertFalse(verifyFileIdInJSONArray(resp, fileNew), "Deleted file is present in dashboard!");

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

	}

	@Test()
	public void shareFileAndAcceptTest() throws Exception {

		try {
			String fileNew = ShareBoxUtils.uploadNewFile();
			Response response = APIUtils.shareFile(fileNew, "198");

			response = APIUtils.getAllFilesInbox(token2);
			assertTrue(verifyFileIdInJSONArray(response, fileNew), "Shared file is not shown in Inbox!");

			APIUtils.acceptOrRejectFile(token2, fileNew, true);

			response = APIUtils.getAllFilesInbox(token2);
			assertFalse(verifyFileIdInJSONArray(response, fileNew), "Accepted file is shown in Inbox!");

			response = APIUtils.getAllFiles(token2);
			assertTrue(verifyFileIdInJSONArray(response, fileNew), "Accepted file is not shown in Dashboard!");

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

	}

	@Test()
	public void shareFileAndRejectTest() throws Exception {

		try {
			String fileNew = ShareBoxUtils.uploadNewFile();
			Response response = APIUtils.shareFile(fileNew, "198");

			response = APIUtils.getAllFilesInbox(token2);
			assertTrue(verifyFileIdInJSONArray(response, fileNew), "Shared file is not shown in Inbox!");

			APIUtils.acceptOrRejectFile(token2, fileNew, false);

			response = APIUtils.getAllFilesInbox(token2);
			assertFalse(verifyFileIdInJSONArray(response, fileNew), "Rejected file is still shown in Inbox!");

			response = APIUtils.getAllFiles(token2);
			assertFalse(verifyFileIdInJSONArray(response, fileNew), "Rejected file is shown in Dashboard!");

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}

	}

	private static boolean verifyFileIdInJSONArray(JSONArray jsonArr, String fileId) {
		for (int index = 0; index < jsonArr.length(); index++) {
			if (jsonArr.getJSONObject(index).getString("fileId").equals(fileId))
				return true;
		}
		return false;
	}

	private static boolean verifyFileIdInJSONArray(Response response, String fileId) {
		return verifyFileIdInJSONArray(new JSONArray(response.getBody().asString()), fileId);
	}
}
