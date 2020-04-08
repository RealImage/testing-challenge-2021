package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import utilities.CommonMethods;

public class LoginPage {
	public WebDriver driver;
	public CommonMethods method;
	
	@FindBy(how = How.CSS, using = "input#auth_user_email")
	public WebElement email;
	
	@FindBy(how = How.CSS, using = "input#auth_user_password")
	public WebElement password;
	
	@FindBy(how = How.CSS, using = "input[type='submit']")
	public WebElement loginButton;
	
	public LoginPage(WebDriver driver, CommonMethods method){
		this.driver = driver;
		this.method = method;
		PageFactory.initElements(this.driver, this);		
	}
	
	public void login(String emailText, String passwordText){
		method.sendText(email, emailText);
		method.sendText(password, passwordText);
		method.clickElement(loginButton);
	}
	
}
