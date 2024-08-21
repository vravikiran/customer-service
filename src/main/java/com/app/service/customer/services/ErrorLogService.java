package com.app.service.customer.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * generates an error log file. writes validation errors for each column of
 * record writes validation errors for all records in given csv file
 */

@Service
public class ErrorLogService {
	Logger logger = LoggerFactory.getLogger(ErrorLogService.class);
	@Value("${errorlog.file.path}")
	private String errorLogFilePath;
	@Value("${errorlog.file.format}")
	private String fileFormat;

	/**
	 * reads the errorsMap if not empty on validating customer data from csv file
	 * and generates errorLog file
	 * 
	 * @param errorsMap
	 */
	@Async
	public void generateErrorLogExcelFile(Map<Integer, Map<String, String>> errorsMap) {
		logger.info("ErrorLogService :: generateErrorLogExcelFile - {}", "Generating error log file started");
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			XSSFSheet xssfSheet = workbook.createSheet();
			XSSFFont font = workbook.createFont();
			font.setBold(true);
			XSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setFont(font);
			cellStyle.setFillBackgroundColor(HSSFColorPredefined.BLACK.getIndex());
			int rowNum = 0;
			List<Integer> rowList = errorsMap.keySet().stream().collect(Collectors.toList());
			XSSFRow xssfRow = xssfSheet.createRow(rowNum++);
			XSSFCell cell1 = xssfRow.createCell(0);
			cell1.setCellValue("ROW NUMBER");
			cell1.setCellStyle(cellStyle);
			XSSFCell cell2 = xssfRow.createCell(1);
			cell2.setCellValue("ERROR COLUMN");
			cell2.setCellStyle(cellStyle);
			XSSFCell cell3 = xssfRow.createCell(2);
			cell3.setCellValue("ERROR DESCRIPTION");
			cell3.setCellStyle(cellStyle);
			for (int i = 0; i < rowList.size(); i++) {
				synchronized (xssfSheet) {
					rowNum = addRows(xssfSheet, rowNum++, rowList.get(i), errorsMap.get(rowList.get(i)), cellStyle);
				}
			}
			Path path = Paths.get(errorLogFilePath + LocalDateTime.now() + fileFormat);
			try {
				File file = path.toFile();
				file.setReadable(true);
				FileOutputStream out = new FileOutputStream(file);
				workbook.write(out);
				out.close();
			} catch (FileNotFoundException e) {
				logger.error(
						"ErrorLogService :: generateErrorLogExcelFile, FileNotFoundException occured while generating error log file - {}",
						e.getMessage());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(
					"ErrorLogService :: generateErrorLogExcelFile, IOException occured while generating error log file - {}",
					e.getMessage());
		}
		logger.info("ErrorLogService :: generateErrorLogExcelFile - {}", "Generating error log file completed");
	}

	/**
	 * Adds errors related to a csv record to excel sheet
	 * 
	 * @param xssfSheet
	 * @param rowCount
	 * @param rowNum
	 * @param errorColumns
	 * @param cellStyle
	 * @return
	 */
	public int addRows(XSSFSheet xssfSheet, int rowCount, int rowNum, Map<String, String> errorColumns,
			XSSFCellStyle cellStyle) {
		List<String> errorColumnNames = errorColumns.keySet().stream().collect(Collectors.toList());
		for (int i = 0; i < errorColumnNames.size(); i++) {
			XSSFRow hssfRow = xssfSheet.createRow(rowCount++);
			hssfRow.createCell(0).setCellValue("Row " + rowNum);
			hssfRow.createCell(1).setCellValue(errorColumnNames.get(i));
			hssfRow.createCell(2).setCellValue(errorColumns.get(errorColumnNames.get(i)));
		}
		return rowCount;
	}
}
