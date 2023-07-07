package utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

	public HashMap<String, HashMap<String, String>> getExcelData(String filePath) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(filePath));
		HSSFSheet sheet = workbook.getSheetAt(0);
		HashMap<String, HashMap<String, String>> completeSheetData = new HashMap<String, HashMap<String, String>>();
		List<String> columnHeader = new ArrayList<String>();
		Row row = sheet.getRow(0);
		Iterator<Cell> cellIterator = row.cellIterator();
		while (cellIterator.hasNext()) {
			columnHeader.add(cellIterator.next().getStringCellValue());
		}
		int rowCount = sheet.getPhysicalNumberOfRows();
		int columnCount = row.getLastCellNum();
		for (int i = 1; i < rowCount; i++) {
			String key = null;
			HashMap<String, String> singleRowData = new HashMap<String, String>();
			Row row1 = sheet.getRow(i);
			for (int j = 0; j < columnCount; j++) {
				Cell cell = row1.getCell(j);
				if (j == 1)
					singleRowData.put(columnHeader.get(j),
							String.valueOf(new SimpleDateFormat("HHmm").format(cell.getDateCellValue())));
				else
					singleRowData.put(columnHeader.get(j), getCellValueAsString(cell));
			}
			if (columnCount > 1) {
				key = String.valueOf(new SimpleDateFormat("ddMMyyyy").format(row1.getCell(0).getDateCellValue())) + "_"
						+ String.valueOf(
								new SimpleDateFormat("HHmm").format(row1.getCell(1).getDateCellValue().getTime()));
			}
			completeSheetData.put(String.valueOf(key), singleRowData);
		}
		return completeSheetData;
	}

	public String getCellValueAsString(Cell cell) {
		String cellValue = null;
		switch (cell.getCellType()) {
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				cellValue = String.valueOf(new SimpleDateFormat("dd-MM-yyyy").format(cell.getDateCellValue()));
			} else {
				cellValue = String.valueOf(cell.getNumericCellValue());
			}
			break;
		case STRING:
			cellValue = cell.getStringCellValue();
			break;
		case BOOLEAN:
			cellValue = String.valueOf(cell.getBooleanCellValue());
			break;
		default:
			cellValue = "DEFAULT";
		}
		return cellValue;
	}

	public static void writeDatainExcel(HashMap<String, HashMap<String, String>> dataMap, String result) throws ParseException {

		String path = "data.xlsx";
		Workbook workbook;
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            workbook = WorkbookFactory.create(fileInputStream);
        } catch (IOException e) {
        	workbook = new XSSFWorkbook();
        }
        Sheet sheet = workbook.getSheet("Data");
        if (sheet == null) {
        	sheet = workbook.createSheet("Data");
        }

		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("Date");
		headerRow.createCell(1).setCellValue("Time");
		headerRow.createCell(2).setCellValue("kWh Imported Total");
		headerRow.createCell(3).setCellValue("kWh Exported Total");
		headerRow.createCell(4).setCellValue("kVARh Imported Total(Q1+Q2)");
		headerRow.createCell(5).setCellValue("kVARh Exported Total(Q3+Q4)");
		headerRow.createCell(6).setCellValue("Result");
		int rowNum = sheet.getLastRowNum() + 1;
		short color = IndexedColors.LIGHT_YELLOW.getIndex();
		if(result.equalsIgnoreCase("FALSE")) {
			color = IndexedColors.RED.getIndex();
		}else if(result.equalsIgnoreCase("TRUE")) {
			color = IndexedColors.LIGHT_GREEN.getIndex();
		}
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFillForegroundColor(color);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		
		for (Entry<String, HashMap<String, String>> entry : dataMap.entrySet()) {
			String date = entry.getKey().split("_")[0];
			String time = entry.getKey().split("_")[1];
			Map<String, String> innerMap = entry.getValue();

			Row row = sheet.createRow(rowNum++);
			date = String.valueOf(new SimpleDateFormat("dd-MM-yyyy").format(new SimpleDateFormat("ddMMyyyy").parse(date)));
			time = String.valueOf(new SimpleDateFormat("HH:ss").format(new SimpleDateFormat("HHss").parse(time)));
			row.createCell(0).setCellValue(date);
			row.createCell(1).setCellValue(time);
			row.createCell(2).setCellValue(innerMap.get("kWh Imported Total"));
			row.createCell(3).setCellValue(innerMap.get("kWh Exported Total"));
			row.createCell(4).setCellValue(innerMap.get("kVARh Imported Total(Q1+Q2)"));
			row.createCell(5).setCellValue(innerMap.get("kVARh Exported Total(Q3+Q4)"));
			Cell cell = row.createCell(6);
	        cell.setCellValue(result);
	        cell.setCellStyle(cellStyle);
		}
		sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);

		try (FileOutputStream outputStream = new FileOutputStream(path)) {
			workbook.write(outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
