package compareData;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import uidata.SeleniumUtil;
import utils.ExcelUtil;
import utils.ReadProperties;
import utils.ReportUtil;

public class CompareData {
	static ExcelUtil excel = new ExcelUtil();
	static SeleniumUtil uiUtil = new SeleniumUtil();

	public static void main(String[] args) throws IOException, ParseException {

		// get Data from excel
		HashMap<String, HashMap<String, String>> dataFromExcel = excel.getExcelData(ReadProperties.getProperties().get("excelPath"));

		// get Data from UI
		HashMap<String, HashMap<String, String>> dataFromUI = uiUtil.getUiData(ReadProperties.getProperties().get("url"));

		//generate report
		ReportUtil.generateReport(dataFromExcel, dataFromUI);

	}

}
