package uidata;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumUtil {

	WebDriver driver;
	By preElement = By
			.xpath("//h3[contains(text(),'Previous Period')]/following-sibling::h4[not(contains(text(),'Self'))]");
	By tablecols = By.xpath(
			"//h3[contains(text(),'Previous Period')]/following-sibling::h4[not(contains(text(),'Self'))]/following-sibling::table[1]//tr[2]/th");
	By tablecolValue1 = By.xpath(
			"//h3[contains(text(),'Previous Period')]/following-sibling::h4[not(contains(text(),'Self'))]/following-sibling::table[1]//tr[2]/th");

	public HashMap<String, HashMap<String, String>> getUiData(String url) throws ParseException {
		// public static void main(String[] args) throws ParseException {

		HashMap<String, HashMap<String, String>> data = new HashMap<String, HashMap<String, String>>();

		try {
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			driver.get(url);
			int noOfTables = driver.findElements(preElement).size();
			for (int i = 1; i <=noOfTables; i++) {

				String dataText = StringUtils.substringBetween(driver.findElements(preElement).get(i-1).getText(),
						"Read at ", ",");
				String date = String.valueOf(new SimpleDateFormat("ddMMyyyy")
						.format(new SimpleDateFormat("dd/MM/yyyy").parse(dataText.split(" ")[0])));
				String time = String.valueOf(new SimpleDateFormat("HHmm")
						.format(new SimpleDateFormat("HH:mm:ss").parse(dataText.split(" ")[1])));
				int tableIndexes = driver.findElements(By.xpath(
						"(//h3[contains(text(),'Previous Period')]/following-sibling::h4[not(contains(text(),'Self'))]/following-sibling::table)["
								+ i + "]//tr"))
						.size();
				HashMap<String, String> tableData = new HashMap<String, String>();
				for (int j = 2; j <= tableIndexes; j++) {
					String key = driver.findElement(By.xpath(
							"(//h3[contains(text(),'Previous Period')]/following-sibling::h4[not(contains(text(),'Self'))]/following-sibling::table)["
									+ i + "]//tr[" + j + "]/th"))
							.getText();
					String value = String.valueOf(Double.parseDouble(driver.findElement(By.xpath(
							"(//h3[contains(text(),'Previous Period')]/following-sibling::h4[not(contains(text(),'Self'))]/following-sibling::table)["
									+ i + "]//tr[" + j + "]/td[3]"))
							.getText()));
					tableData.put(key, value);
				}
				data.put(date + "_" + time, tableData);
			}
			return data;
		} finally {
			driver.quit();
		}

	}

}
