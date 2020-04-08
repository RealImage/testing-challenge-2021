package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utilities.CommonMethods;

public class HomePage {
	public WebDriver driver;
	public CommonMethods method;
	
	@FindBy(how = How.XPATH, using = "//a[contains(text(),'Generate Token')]")
	public WebElement generateTokenTab;
	
	@FindBy(how = How.CSS, using = "a.dropdown-toggle")
	public WebElement dropDownToggle;
	
	@FindBy(how = How.XPATH, using = "//a[contains(text(),' Log Out')]")
	public WebElement logoutLink;
	
	public HomePage(WebDriver driver, CommonMethods method){
		this.driver = driver;
		this.method = method;
		PageFactory.initElements(this.driver, this);		
	}
	
	public void navigateToGenerateTokenPage(){
		method.clickElement(generateTokenTab);
	}
	
	public void logout(){
		method.clickElement(dropDownToggle);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOf(logoutLink));
		method.clickElement(logoutLink);
	}
}
