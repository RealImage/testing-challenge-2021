package com.qube.testingchallenge;

import java.io.IOException;
import java.util.HashMap;

import io.restassured.response.Response;

public class Sharebox_FilesAPI {

	public Sharebox_FilesAPI() {
		api = new ApiReader();
		try {
			api.beforeTest();
		} catch (IOException e) {
			e.printStackTrace();
		}
		path = api.prop.getProperty(ConfigConstants.filePath);
	}
	ApiReader api = null;
	String path = null;
	public Response POST(String fileId,String shareTo,String token) {
		HashMap<String, Object> queryparams = new HashMap<String, Object>();
		if (token != null && !token.isEmpty()) {
			queryparams.put(ConfigConstants.queryParamToken, token);
		}
		if (fileId != null && !fileId.isEmpty()) {
			queryparams.put(ConfigConstants.fileId, fileId);
		}
		if (shareTo != null && !shareTo.isEmpty()) {
			queryparams.put(ConfigConstants.shareTo, shareTo);
		}
		Response resp = api.post(path, null, queryparams, null, null, null,null,"multipart/form-data");
		return resp;
	}
	public Response PUT(String fileId, boolean isAccepted, String token) {
		HashMap<String, Object> queryparams = new HashMap<String, Object>();
		if (token != null && !token.isEmpty()) {
			queryparams.put(ConfigConstants.queryParamToken, token);
		}
		if (fileId != null && !fileId.isEmpty()) {
			queryparams.put(ConfigConstants.fileId, fileId);
		}
		queryparams.put("isAccepted",isAccepted);
		Response resp = api.put(path, null, queryparams, null, null, null,null,"multipart/form-data");
		return resp;
	}
	public Response GET(String getSharedFiles,String token) {
		HashMap<String, Object> queryparams = new HashMap<String, Object>();
		if (token != null && !token.isEmpty()) {
			queryparams.put(ConfigConstants.queryParamToken, token);
		}
		if (getSharedFiles != null && !getSharedFiles.isEmpty()) {
			queryparams.put(ConfigConstants.getSharedFiles, getSharedFiles);
		}
		Response resp = api.get(path, null, queryparams,null, null, null);
		return resp;
	}
	public Response DELETE(String fileId,String token) {
		HashMap<String, Object> queryparams = new HashMap<String, Object>();
		if (token != null && !token.isEmpty()) {
			queryparams.put(ConfigConstants.queryParamToken, token);
		}
		if (fileId != null && !fileId.isEmpty()) {
			queryparams.put(ConfigConstants.fileId, fileId);
		}
		Response resp = api.delete(path, null, queryparams, null, null, null,null,"multipart/form-data");
		return resp;
	}
}
