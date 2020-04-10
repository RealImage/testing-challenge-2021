package utilities;

import java.io.IOException;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import pages.HomePage;
import pages.LoginPage;
import pages.TokenPage;

public class CommonScenarios extends Reports{
	public LoginPage login;
	public HomePage home;
	public TokenPage token;
	public Reports report;
	public enum resources{
		files,upload
	}
	
	public String getUserSessionID(String email, String password){
		login = new LoginPage(driver, method);
		login.login(email, password);
		home = new HomePage(driver, method);
		home.navigateToGenerateTokenPage();
		token = new TokenPage(driver, method);
		return token.getToken();
	}

	
	@BeforeTest
	public void scenarioSetup() throws IOException{
		//get session primary user ID
		initDriver();
		driver.get(property.getProperty("loginURL"));
		primarySessionID = getUserSessionID(property.getProperty("primUserId")
				, property.getProperty("primPassword"));
		openMainReport();
	}

	@BeforeMethod
	public void testSetup(){
		
	}
	
	@AfterTest
	public void scenarioteardown() throws IOException{
		closeMainReport();
		try{
		home.logout();
		}catch(Exception e){
			//do nothing
		}
		driver.quit();
		Runtime.getRuntime().exec("taskkill /im chromedriver.exe /f");
	}
	
	@AfterMethod
	public void testteardown() throws IOException{
		
	}
	
}
