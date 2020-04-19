package com.qube.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

public class QubeUtil 
{
	public static String calculateSHA256(File file) throws FileNotFoundException, IOException
	{
		String checksumSHA256 = DigestUtils.sha256Hex(new FileInputStream(file));
        return checksumSHA256;
	}
	
	public static boolean isUUID(String uuid) 
	{
	    try 
	    {
	        UUID.fromString(uuid);
	        return true;
	    } 
	    catch (Exception ex) 
	    {
	        return false;
	    }
	}
	
	public static boolean isDateTime(String dateString) 
	{
	    try 
	    {
	    	Date date = null;
	    	date = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS").parse(dateString);
	    	if(date!=null)
	    		return true;
	    	return false;	
	    } 
	    catch (Exception ex) 
	    {
	        return false;
	    }
	}
}
