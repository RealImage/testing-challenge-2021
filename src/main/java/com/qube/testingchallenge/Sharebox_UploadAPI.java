package com.qube.testingchallenge;

import java.io.IOException;
import java.util.HashMap;

import io.restassured.response.Response;

public class Sharebox_UploadAPI {

	public Sharebox_UploadAPI() {
		api = new ApiReader();
		try {
			api.beforeTest();
		} catch (IOException e) {
			e.printStackTrace();
		}
		path = api.prop.getProperty(ConfigConstants.uploadPath);
	}
	ApiReader api = null;
	String path = null;
	public Response POST(String name,String size, String hash,String token) {
		HashMap<String, Object> queryparams = new HashMap<String, Object>();
		if (token != null && !token.isEmpty()) {
			queryparams.put(ConfigConstants.queryParamToken, token);
		}
		if (name != null && !name.isEmpty()) {
			queryparams.put(ConfigConstants.filename, name);
		}
		if (hash != null && !hash.isEmpty()) {
			queryparams.put(ConfigConstants.filehash, hash);
		}
		if (size != null && !size.isEmpty()) {
			queryparams.put(ConfigConstants.filesize, size);
		}
		Response resp = api.post(path, null, queryparams, null, null, null,null,"multipart/form-data");
		return resp;
	}
	public Response PUT(String fileId, String bytesCompleted, String token) {
		HashMap<String, Object> queryparams = new HashMap<String, Object>();
		if (token != null && !token.isEmpty()) {
			queryparams.put(ConfigConstants.queryParamToken, token);
		}
		if (fileId != null && !fileId.isEmpty()) {
			queryparams.put(ConfigConstants.fileId, fileId);
		}
		if (bytesCompleted != null && !bytesCompleted.isEmpty()) {
			queryparams.put(ConfigConstants.bytesCompleted, bytesCompleted);
		}
		Response resp = api.put(path, null, queryparams, null, null, null,null,"multipart/form-data");
		return resp;
	}
	public Response GET(String fileId,String token) {
		HashMap<String, Object> queryparams = new HashMap<String, Object>();
		if (token != null && !token.isEmpty()) {
			queryparams.put(ConfigConstants.queryParamToken, token);
		}
		if (fileId != null && !fileId.isEmpty()) {
			queryparams.put(ConfigConstants.fileId, fileId);
		}
		Response resp = api.get(path, null, queryparams,null, null, null);
		return resp;
	}
}
