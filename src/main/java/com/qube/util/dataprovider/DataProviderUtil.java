package com.qube.util.dataprovider;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

/** 
 * 
 */
public class DataProviderUtil 
{
	protected static Map<String, String> resolveDataProviderArguments(
			Method testMethod) throws Exception 
	{
		if (testMethod == null) 
		{
			throw new IllegalArgumentException(
					"Test Method context cannot be null.");
		}

		DataProviderArguments args = testMethod.getAnnotation(DataProviderArguments.class);
		if (args == null) 
		{
			throw new IllegalArgumentException(
					"Test Method context has no DataProviderArguments annotation.");
		}
		if (ArrayUtils.isEmpty(args.value())) 
		{
			throw new IllegalArgumentException(
					"Test Method context has a malformed DataProviderArguments annotation.");
		}

		Map<String, String> arguments = new HashMap<String, String>();
		for (int argsCount = 0; argsCount < args.value().length; argsCount++) 
		{
			String[] parts = args.value()[argsCount].split("=");
			arguments.put(StringUtils.trim(parts[0]),
					StringUtils.trim(parts[1]));
		}
		return arguments;
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface DataProviderArguments {
		String[] value();
	}
}
