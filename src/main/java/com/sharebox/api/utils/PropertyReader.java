package com.sharebox.api.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader {

	static Properties propFile = new Properties();

	static {
		try {
			propFile.load(new FileInputStream(new File(System.getProperty("user.dir") + File.separator + "src"
					+ File.separator + "main" + File.separator + "resources" + File.separator + "config.properties")));
			System.out.println("Loaded Properties File");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		return propFile.getProperty(key);
	}

}
