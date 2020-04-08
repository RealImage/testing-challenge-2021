package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestBase {
	//framework wise
	public WebDriver driver;
	public Properties property;
	public String projectPath;
	public CommonMethods method;
	
	//test suite wise
	public String baseURI;
	public String primarySessionID;
	public String secondarySessionID;
	
	@BeforeSuite
	public void initialSetup() throws IOException{
		projectPath = System.getProperty("user.dir");
		FileInputStream fileInput = new FileInputStream(projectPath 
				+ "\\src\\test\\resources\\config.properties");
		property = new Properties();
		property.load(fileInput);
		method = new CommonMethods();
		baseURI = property.getProperty("baseURI");
	}
	
	@AfterSuite
	public void tearDown() throws IOException{
		
	}
	
	public void initDriver(){
		
		DesiredCapabilities dc = new DesiredCapabilities();
		dc.setAcceptInsecureCerts(true);
		

		ChromeOptions options = new ChromeOptions();
		options.merge(dc);
		
		System.setProperty("webdriver.chrome.driver", 
				projectPath+property.getProperty("chromeDriver"));
		driver = new ChromeDriver(options);
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
	}
	
}
