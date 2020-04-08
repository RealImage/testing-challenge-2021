package pages;

import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;


import utilities.CommonMethods;

public class TokenPage {
	public WebDriver driver;
	public CommonMethods method;
	
	@FindBy(how = How.CSS, using = "button#token_button")
	public WebElement generateTokenButton;
	
	@FindBy(how = How.CSS, using = "td#new_row b input")
	public WebElement token;
	
	public TokenPage(WebDriver driver, CommonMethods method){
		this.driver = driver;
		this.method = method;
		PageFactory.initElements(this.driver, this);		
	}
	
	public String getToken(){
		method.clickElement(generateTokenButton);
		while(!token.isDisplayed()){
			//do event
		}
		return method.getAttribute(token, "value");
	}
}
