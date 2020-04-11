package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
	
	//essential elements
	public static String projectPath = System.getProperty("user.dir");
	public static Properties property ;
	public static WebDriver primaryDriver;
	public static WebDriver secondaryDriver;
	public static CommonMethods method;
	public static String baseURI;
	public static String primarySessionID;
	public static String secondarySessionID;
	public static File testDatafile;
	public enum resources{
		files,upload
	}
	
	
	public WebDriver initDriver(){
		DesiredCapabilities dc = new DesiredCapabilities();
		dc.setAcceptInsecureCerts(true);
		

		ChromeOptions options = new ChromeOptions();
		options.merge(dc);
		
		System.setProperty("webdriver.chrome.driver", 
				projectPath+property.getProperty("chromeDriver"));
		WebDriver driver = new ChromeDriver(options);
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		
		return driver;
	}
	
	
	@BeforeSuite
	public void initEssentials() throws IOException{
		FileInputStream fileInput = new FileInputStream(projectPath 
				+ "\\src\\test\\resources\\config.properties");
		property = new Properties();
		property.load(fileInput);
		method = new CommonMethods();
		baseURI = property.getProperty("baseURI");
		testDatafile = new File(projectPath 
				+ property.getProperty("testDataSheet"));
		Reports.openMainReport();
	}
	
	@AfterSuite
	public void tearDown(){
		try{
			Reports.closeMainReport();
			primaryDriver.quit();
			secondaryDriver.quit();
			Runtime.getRuntime().exec("taskkill /im chromedriver.exe /f");
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
}
