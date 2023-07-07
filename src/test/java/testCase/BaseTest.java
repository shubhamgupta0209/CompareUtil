package testCase;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import utils.ReadProperties;

public class BaseTest {

	WebDriver driver;

	@BeforeTest
	public void initDriver() {
		driver = new ChromeDriver();
		driver.manage().window().maximize();
	}

	@Test
	public void compareData() {
		String path = ReadProperties.getProperties().get("excelPath");
		System.out.println(path);
		driver.get("file:///C:/Users/shubh/Downloads/OneDrive_2023-05-19/02.%20Sample%20data%20files%20from%20dongles%20&%20CCMS/LG/Three%20Phase/Regular%20or%20PV/7380515/U3400_7380515.html");
		
	}

	@AfterTest
	public void tearDown() {
		driver.quit();
	}

}
