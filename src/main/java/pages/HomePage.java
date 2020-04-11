package pages;

import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
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
	
	@FindBy(how = How.XPATH, using = "//a[contains(text(),'Dashboard')]")
	public WebElement dashboardTab;
	
	@FindBy(how = How.XPATH, using = "//a[contains(text(),'Inbox')]")
	public WebElement inboxTab;
	
	@FindBy(how = How.CSS, using = "a.dropdown-toggle")
	public WebElement dropDownToggle;
	
	@FindBy(how = How.XPATH, using = "//a[contains(text(),' Log Out')]")
	public WebElement logoutLink;
	
	@FindBy(how = How.CSS, using = "button#token_button")
	public WebElement generateTokenButton;
	
	@FindBy(how = How.CSS, using = "td#new_row b input")
	public WebElement token;
	
	public String FilesInPage = "//tbody/tr";
	
	public HomePage(WebDriver driver, CommonMethods method){
		this.driver = driver;
		this.method = method;
		PageFactory.initElements(this.driver, this);		
	}
	
	public List<WebElement> getListOfFiles(){
		List<WebElement> filesInPage = new  LinkedList<WebElement>();
		filesInPage = driver.findElements(By.xpath(FilesInPage));
		return filesInPage;
	}
	
	public void navigateToGenerateTokenPage(){
		method.clickElement(generateTokenTab);
	}
	
	public void navigateToDashboard(){
		method.clickElement(dashboardTab);
	}
	
	public void navigateToInbox(){
		method.clickElement(inboxTab);
	}
	
	public void logout(){
		method.clickElement(dropDownToggle);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOf(logoutLink));
		method.clickElement(logoutLink);
	}
	
	public String getToken(){
		method.clickElement(generateTokenButton);
		while(!token.isDisplayed()){
			//do event
		}
		return method.getAttribute(token, "value");
	}
	
	public boolean validateFilePresent(String fileID){
		
		boolean isPresent = false;
		for(WebElement file: getListOfFiles()){
			if(method.getAttribute(file, "id").equals(fileID)){
				isPresent = true;
				break;
			}
		}
		return isPresent;
	}
	public boolean validateFilePresentwithStatus(String fileID, String Status){
		WebElement files;
		WebElement file;
		boolean isPresent = false;
		for(int i = 0;i< getListOfFiles().size();i++){
			files=driver.findElement(By.xpath(FilesInPage+"["+String.valueOf(i+1)+"]"));
			if(method.getAttribute(files, "id").equals(fileID)){
				System.out.println(files.getAttribute("id"));
				file = driver.findElement(By.xpath(FilesInPage+"["+String.valueOf(i+1)+"]/td[5]"));
				System.out.println(file.getText());
				if(method.getText(
						file).equals(Status)){
				isPresent = true;
				break;
				
				}
			}
		}
		return isPresent;
	}
}
