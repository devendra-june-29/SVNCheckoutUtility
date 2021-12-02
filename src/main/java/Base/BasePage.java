package Base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BasePage {

	public WebDriver driver;
	public Properties prop;
	public FileInputStream fis;
	
	public Properties initProperties() throws IOException {
			
		// Getting user directory path
		String projectLocation = System.getProperty("user.dir");
		fis = new FileInputStream(projectLocation+"\\src\\main\\java\\Configuation\\config.properties");
		prop = new Properties();
		prop.load(fis);
		return prop;
	}
	
	public WebDriver setUp() {
		
		// Selenium Server setup
		WebDriverManager.chromedriver().setup(); 
		
		// Chrome browser setup
		driver = new ChromeDriver(); 
		
		// Fetching SVN URL from Configuration file.
		Properties prop = null;
		try {
			prop = initProperties();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("URL : " + prop.getProperty("url"));
		
		//Launching SVN URL
		driver.get(prop.getProperty("url"));
		
		driver.manage().window().maximize(); // Open window in maximize mode
		
		return driver;
	}
	
	
}
