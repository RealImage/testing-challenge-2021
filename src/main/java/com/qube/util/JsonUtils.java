package com.qube.util;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class JsonUtils 
{
	public static String removeJsonField(String json, String jsonPath)
			throws JSONException 
	{
		if (StringUtils.isBlank(json) || StringUtils.isBlank(jsonPath)) 
		{
			return json;
		}

		JSONObject jsonObject = (JSONObject) JSONValue.parse(json);
		jsonObject.remove(jsonPath);
		return jsonObject.toJSONString();
	}
	
	public static String fetchFileId(String response) throws JSONException
	{
		JSONObject jsonObject = (JSONObject) JSONValue.parse(response);
		String fileId = (String) jsonObject.get("fileId");
		return fileId;
	}
	
	public static String getValue(String response, String field) throws JSONException
	{
		JSONObject jsonObject = (JSONObject) JSONValue.parse(response);
		return (String) jsonObject.get(field);
	}
}
