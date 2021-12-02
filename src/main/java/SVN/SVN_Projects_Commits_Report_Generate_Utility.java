package SVN;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import Base.BasePage;
import ExcelReaderUtility.Xls_Reader;
import Pages.LoginPage;

public class SVN_Projects_Commits_Report_Generate_Utility { 

	BasePage basePage =  new BasePage();
	LoginPage loginPage = new LoginPage();
	WebDriver driver;
	
	public static void main(String[] args) {
		TestListenerAdapter tla = new TestListenerAdapter();
		TestNG testng = new TestNG();
		testng.setTestClasses(new Class[] { SVN_Projects_Commits_Report_Generate_Utility.class });
		testng.addListener(tla);
		testng.run();
	}
	
	@BeforeTest
	public void loginSVNTest() throws IOException {
		
		driver = basePage.setUp();
		loginPage.loginSVN(driver);
		
	}
	
	@Test
	public void SVNReportExtractUtility() throws InterruptedException {
		
		// Dynamic wait for the element to load on the page via DOM
		WebDriverWait wait = new WebDriverWait(driver, 10);
		
		// Getting user directory path
		String projectLocation = System.getProperty("user.dir");
		
		// Define Xls object to get the count of the rows
		//Xls_Reader reader =  new Xls_Reader(projectLocation+"\\src\\TestData\\SVNReport.xlsx");
		Xls_Reader reader =  new Xls_Reader("C:\\SVN_Export\\SVNReport.xlsx");
		reader =  new Xls_Reader("C:\\SVN_Export\\SVNReport.xlsx");
		
		int rowCount = reader.getRowCount("SVNCommitDetails");
		System.out.println("Total No of Projects : " + (rowCount-1));
		
		// For to iterate each project available in excel
		for(int startingCount = 2; startingCount <= rowCount;  startingCount++) {
			
			// Admin Menu
			By adminLocator = By.xpath("(//div[@id='main-menu']//a)[position()=3]");
			wait.until(ExpectedConditions.visibilityOfElementLocated(adminLocator));
			driver.findElement(adminLocator).click();
			
			// Left side Project option
			By projectsLocator = By.xpath("(//div[@id='sub-menu']//a)[position()=3]");
			wait.until(ExpectedConditions.visibilityOfElementLocated(projectsLocator));
			driver.findElement(projectsLocator).click();
			
			String projectName = reader.getCellData("SVNCommitDetails", "ProjectName", startingCount);
			
			// Click on Lock Image to see baseline folders
			By clickOnBrowse = By.xpath("(//table[@id='projects']//tr//td/a[text()='"+projectName+"']//following::td[2])[position()=1]/a[4]");
			try {
				wait.until(ExpectedConditions.visibilityOfElementLocated(clickOnBrowse));
				driver.findElement(clickOnBrowse).click();
			}catch (Exception e) {
				System.out.println("*************************************** Please provide correct Project Name of : " + projectName);
				continue;
			}
			// Click on Timeline option to see the svn check-in files
			By timelineLocator = By.xpath("//div[@id='sub-menu']//li[3]/a");
			wait.until(ExpectedConditions.visibilityOfElementLocated(timelineLocator));
			driver.findElement(timelineLocator).click();
			
			By totalRowsUnderTimelineLocator = By.xpath("//table[@id='commits']//tbody/tr");
			int totalRowsUnderTimeline = driver.findElements(totalRowsUnderTimelineLocator).size(); 
			System.out.println("Total Number of Rows under timeline : " + totalRowsUnderTimeline);
			
			// For loop to iterate each commits file to check the file extension.
			for(int counter = 1; counter<=(totalRowsUnderTimeline-1); counter++) {
				
				boolean flag = false;
				
				// Click to view the checked-in file.
				By linkLocator = By.xpath("(//table[@id='commits']//tbody/tr)[position()="+counter+"]");
				wait.until(ExpectedConditions.visibilityOfElementLocated(linkLocator));
				driver.findElement(linkLocator).click();
				
				String getFileName = null;
				
				try {
					// Getting the file name locator.
					By getFileNameLocator = By.xpath("//div[@class='usvn_info']//table/tbody/tr[3]/td/a");
					getFileName = driver.findElement(getFileNameLocator).getText();
					System.out.println("Commit File Name : " + getFileName);
				}catch (Exception e) {
					System.out.println("*************************************** Broken link found. Execution will continue...!");
					driver.navigate().back(); // Once broken link found, this command will send control to previous page and operation will continue.
					continue;
				}
				
				// Checking the file extension.
				if((getFileName.contains(".java")) || (getFileName.contains(".jsp")) || (getFileName.contains(".js")) || (getFileName.contains(".war")) || (getFileName.contains(".sql")))  {
					
					// Author : Setting the value in excel's cell
					By authorLocator = By.xpath("//div[@class='usvn_info']//table/tbody/tr[1]/td[2]");
					String  author = driver.findElement(authorLocator).getText();
					System.out.println("Author : " + author);
					reader.setCellData("SVNCommitDetails", "Author", startingCount, author);
					
					// Date : Setting the value in excel's cell
					By dateLocator = By.xpath("//div[@class='usvn_info']//table/tbody/tr[2]/td[2]");
					String  date = driver.findElement(dateLocator).getText();
					System.out.println("Date : " + date);
					reader.setCellData("SVNCommitDetails", "Date", startingCount, date);
					
					// Message or Comment : Setting the value in excel's cell
					By messageLocator = By.xpath("//div[@class='usvn_info']//table/tbody/tr[4]/td[2]");
					String  message = driver.findElement(messageLocator).getText();
					System.out.println("Message : " + message);
					reader.setCellData("SVNCommitDetails", "Message", startingCount, message);
					
					// Setting the value in excel's cell 
					By fileNameLocator = By.xpath("//div[@class='usvn_info']//table/tbody/tr[3]/td[2]/a");
					wait.until(ExpectedConditions.visibilityOfElementLocated(fileNameLocator));
					String fileName = driver.findElement(fileNameLocator).getText();
					reader.setCellData("SVNCommitDetails", "FileName", startingCount, fileName);
					
					flag = true;
					
				} // End if loop
				 
				if(flag) {
					System.out.println("*************************************** Break : Found .JSP or .JS or .Java file for this project.");
					break;
				} else {
					
					// Author : Setting the value in excel's cell
					reader.setCellData("SVNCommitDetails", "Author", startingCount, "Not Available");
					
					// Date : Setting the value in excel's cell
					reader.setCellData("SVNCommitDetails", "Date", startingCount, "Not Available");
					
					// Message or Comment : Setting the value in excel's cell
					reader.setCellData("SVNCommitDetails", "Message", startingCount, "Not Available");
					
					// Setting the value in excel's cell 
					reader.setCellData("SVNCommitDetails", "FileName", startingCount, "File extension with .JSP, .JS, .JAVA, .WAR, .SQL is not checked-in yet for this project.");
					
					// Click on Timeline option to see the svn check-in files
					timelineLocator = By.xpath("//div[@id='sub-menu']//li[3]/a");
					wait.until(ExpectedConditions.visibilityOfElementLocated(timelineLocator));
					driver.findElement(timelineLocator).click();
				}
			} // End nested for loop : This loop for fetch file extension.
		} // End for loop : This loop for fetch project name.
	}
	
	@AfterTest
	public void setDown() throws InterruptedException {
		System.out.println();System.out.println();
		System.out.println("<<<<<<<<<<<< Report have been generated successfully. >>>>>>>>>>>>");
		System.out.println();System.out.println();
//		By logout = By.xpath("//div[@id = 'main-menu']//li[4]/a");
//		driver.findElement(logout).click();
//		Thread.sleep(4000);
//		driver.quit();
		
	}
	
}