package Pages;

import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Base.BasePage;
import ExcelReaderUtility.Xls_Reader;

public class LoginPage {

	public void loginSVN(WebDriver driver) throws IOException {
		//LoginDetails
		
		BasePage basePage =  new BasePage();
		Properties prop = basePage.initProperties();
		
		// Dynamic wait for the element to load on the page via DOM
		WebDriverWait wait = new WebDriverWait(driver, 10);
				
		//***************** Below code is use to take username and password from config file.*********************//
		// Enter UserName
		By username = By.id("login");
		wait.until(ExpectedConditions.visibilityOfElementLocated(username)); 
		driver.findElement(username).sendKeys(prop.getProperty("username"));
		// Enter Password
		By password = By.id("password");
		wait.until(ExpectedConditions.visibilityOfElementLocated(password)); 
		driver.findElement(password).sendKeys(prop.getProperty("password"));
		
		// Click Login button
		By loginBtn = By.id("submit");
		driver.findElement(loginBtn).click();
		//***************** Above code is use to take username and password from config file.*********************//
		

		 System.Out.Println("This is updated code");
	}

}
