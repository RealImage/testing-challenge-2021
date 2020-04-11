package utilities;


import java.util.LinkedHashMap;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import pages.HomePage;
import pages.LoginPage;

public class Hooks extends TestBase{
	
	public LinkedHashMap<String, String> testData;
	
	@BeforeTest
	public void scenarioSetup(){
		primaryDriver = initDriver();
		primaryDriver.get(property.getProperty("loginURL"));
		primarySessionID = setSessionID(primaryDriver, 
				property.getProperty("primUserId"), 
				property.getProperty("primPassword"));
		
		secondaryDriver = initDriver();
		secondaryDriver.get(property.getProperty("loginURL"));
		secondarySessionID = setSessionID(secondaryDriver, 
				property.getProperty("secUserId"), 
				property.getProperty("secPassword"));
	}
	
	@AfterTest
	public void scenarioTeardown(){
		
	}
	
	@BeforeMethod
	public void testSetup(){
		testData = new LinkedHashMap<String, String>();
	}
	
	@AfterMethod
	public void testTeardown(){
		
	}
	
	public String setSessionID(WebDriver driver, String email, String password){
		LoginPage login = new LoginPage(driver, method);
		login.login(email, password);
		HomePage home = new HomePage(driver, method);
		home.navigateToGenerateTokenPage();
		return home.getToken();
	}
}
