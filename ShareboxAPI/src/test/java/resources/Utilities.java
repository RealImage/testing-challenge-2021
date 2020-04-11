package resources;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Utilities {

	//Building request specification for all APIs
	public static RequestSpecification req;
	static String curDir = System.getProperty("user.dir");
	public RequestSpecification requestSpecification(String Token) throws IOException
	{
		if(req==null) {
			PrintStream log = new PrintStream(new FileOutputStream("logging.txt"));
			//Request Spec Builder
			req = new RequestSpecBuilder().setBaseUri(getGlobalValue("baseUrl")).addQueryParam("token", Token)
					.addFilter(RequestLoggingFilter.logRequestTo(log))
					.addFilter(ResponseLoggingFilter.logResponseTo(log))
					.build();
			return req;
		}
		return req;
	}

	//Getting the global variables from Global.properties file
	public static String getGlobalValue(String key) throws IOException {
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(curDir + "\\src\\test\\java\\resources\\Global.properties");
		prop.load(fis);
		return prop.getProperty(key);
	}
	
	//Parsing the json response
	public String getJsonPath(Response response, String key) {
		String resp = response.asString();
		JsonPath js = new JsonPath(resp);
		return js.get(key).toString();
	}

	//Getting the hash value for the file
	public static String gethashValue(String Path) throws IOException {
		FileInputStream fis = new FileInputStream(Path);
		String hashValue = DigestUtils.md5Hex(fis);
		System.out.println("hash(md5) Value for Test file: "+hashValue);
		return hashValue;
	}
	
   //Generating randon number
	public static int getRandomNumber() {
		Random rand = new Random();
		int randNum = rand.nextInt(100000);
		//System.out.println(randNum);
		return randNum;
	}
	
	//Setting up the test file before each run
	public static void setupTestFile() throws IOException {
		String content = "SampleText"+getRandomNumber();
		String filePath = curDir + getGlobalValue("uploadFilePath");
		//System.out.println(filePath);
		FileWriter write = new FileWriter(filePath);
		BufferedWriter w = new BufferedWriter(write);
		w.write(content);
		w.close();
		System.out.println("Test File setup done...");
	}
	
	//Getting the Token from the Sharebox website via Selenium
	public static String getToken(String userName, String password) throws IOException, InterruptedException {
		
		System.setProperty("webdriver.chrome.driver", curDir + "\\drivers\\chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
	    options.addArguments("test-type");
	    options.addArguments("ignore-certificate-errors");
	    options.setAcceptInsecureCerts(true);
		WebDriver driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		
		driver.get(getGlobalValue("testUrl"));
		
		driver.findElement(By.id("auth_user_email")).sendKeys(userName);
		Thread.sleep(2000);
		driver.findElement(By.id("auth_user_password")).sendKeys(password);
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@class='btn btn-default']")).click();
		Thread.sleep(3000);
		
		driver.findElement(By.xpath("//a[contains(text(),'Generate Token')]")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("token_button")).click();
		Thread.sleep(3000);
		driver.findElement(By.id("token_id")).click();
		Thread.sleep(2000);
		String Token = driver.findElement(By.id("token_id")).getAttribute("value");
		Thread.sleep(2000);
		driver.quit();
		//System.out.println("Token: "+Token);
		return Token;
	}
}