package me.yevgeny.q2jwxmigrator.utilities;

import javafx.util.Pair;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelFileHandler {
    public static final String outputFileName = "TestCase_To_Test_Mapping.xlsx";
    public static final String failedTcListFileName = "Failed_To_Convert_TC_List.xlsx";
    public static final String validationOutputFileName = "Migration_Validation_Errors.xlsx";
    public static final String validationFailedTestListFileName = "Failed_To_Validate_Tests_List.xlsx";
    private static final Logger logger = Logger.getLogger(ExcelFileHandler.class.getSimpleName());
    private static final String outputFileColumns[] = {"QPACK TC ID", "QPACK TC Link", "JIRA Test ID", "JIRA Test " +
            "link"};
    private static final String validationOutputFileColumns[] = {"QPACK TC ID", "QPACK Path", "JIRA Test ID", "JIRA " +
            "Path"};
    private static ExcelFileHandler ourInstance;
    private static String tcListFile;

    public static ExcelFileHandler getInstance() throws IOException {
        if (null == ourInstance) {
            ourInstance = new ExcelFileHandler();
            tcListFile = ConfigurationManager.getInstance().getConfigurationValue("tcListFile");
            ourInstance.createOutputFileIfNeeded(outputFileName, "Migration Table", outputFileColumns);
            ourInstance.createOutputFileIfNeeded(validationOutputFileName, "Migration Validation",
                    validationOutputFileColumns);
        }
        return ourInstance;
    }

    public List<Integer> getTcListFromInputFile() throws IOException {
        List<Integer> tcList = new ArrayList<>();

        try (FileInputStream excelFile = new FileInputStream(new File(tcListFile))) {
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row currentRow : sheet) {
                Cell currentCell = currentRow.getCell(currentRow.getFirstCellNum());
                if (currentCell.getCellTypeEnum() == CellType.STRING) {
                    if (currentCell.getStringCellValue().contains("TC-")) {
                        // Strip "TC-" from the beginning:
                        tcList.add(Integer.valueOf(currentCell.getStringCellValue().substring(3)));
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Failed to read from file", e);
            throw e;
        }

        return tcList;
    }

    public void appendLineToOutputFile(String fileName, String qpackTestcaseId, String qpackTestcaseData,
                                       String JiraTestId, String
                                               JiraTestData) throws IOException {
        try (FileInputStream outputExcelFileInputStream = new FileInputStream(fileName)) {
            Workbook workbook = new XSSFWorkbook(outputExcelFileInputStream);
            Sheet sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();
            Row newRow = sheet.createRow(++lastRowNum);
            newRow.createCell(0).setCellValue(qpackTestcaseId);
            newRow.createCell(1).setCellValue(qpackTestcaseData);
            newRow.createCell(2).setCellValue(JiraTestId);
            newRow.createCell(3).setCellValue(JiraTestData);

            logger.debug(String.format("Appending to output file: [%s, %s, %s, %s]", qpackTestcaseId,
                    qpackTestcaseData, JiraTestId, JiraTestData));
            writeWorkbookToFile(workbook, fileName);
        } catch (IOException e) {
            logger.error("Failed to open file", e);
            throw e;
        }
    }

    public void createFailedTcListFileIfNeeded(List<String> failedTcIds, String fileName) throws IOException {
        File outputFile = new File(fileName);
        if (outputFile.createNewFile()) {
            logger.info(String.format("Creating new output file: %s", fileName));
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Failed TC List");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("Key:");
            writeWorkbookToFile(workbook, fileName);
        } else {
            logger.info("Failed TC list file already exists, data will be appended");
        }

        try (FileInputStream outputExcelFileInputStream = new FileInputStream(fileName)) {
            Workbook workbook = new XSSFWorkbook(outputExcelFileInputStream);
            Sheet sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();

            for (String failedTcId : failedTcIds) {
                Row newRow = sheet.createRow(++lastRowNum);
                newRow.createCell(0).setCellValue(failedTcId);
            }

            logger.debug("Writing failed TC list to file");
            writeWorkbookToFile(workbook, fileName);
        } catch (IOException e) {
            logger.error("Failed to open file", e);
            throw e;
        }
    }

    public List<Pair<Integer, String>> getQpackTestCaseToJiraTestMapping() throws IOException {
        List<Pair<Integer, String>> mapping = new ArrayList<>();

        try (FileInputStream excelFile = new FileInputStream(new File(outputFileName))) {
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            // skip first row:
            rowIterator.next();

            while (rowIterator.hasNext()) {
                Row currentRow = rowIterator.next();
                Pair<Integer, String> pair = new Pair<>(Integer.valueOf(currentRow.getCell(0).getStringCellValue()),
                        currentRow.getCell(2).getStringCellValue());
                mapping.add(pair);
            }
        } catch (IOException e) {
            logger.error("Failed to read from file", e);
            throw e;
        }

        return mapping;

    }

    private void createOutputFileIfNeeded(String fileName, String sheetName, String[] columnHeaders) throws IOException {
        File outputFile = new File(fileName);
        if (outputFile.createNewFile()) {
            logger.info(String.format("Creating new output file: %s", fileName));
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(sheetName);
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue(columnHeaders[0]);
            row.createCell(1).setCellValue(columnHeaders[1]);
            row.createCell(2).setCellValue(columnHeaders[2]);
            row.createCell(3).setCellValue(columnHeaders[3]);
            writeWorkbookToFile(workbook, fileName);
        } else {
            logger.info(String.format("Output file (\"%s\") already exists, data will be appended", fileName));
        }
    }

    private void writeWorkbookToFile(Workbook workbook, String fileName) throws IOException {
        try (FileOutputStream outputExcelFileOutputStream = new FileOutputStream(fileName)) {
            workbook.write(outputExcelFileOutputStream);
            workbook.close();
        } catch (IOException e) {
            logger.error("Failed to write to file", e);
            throw e;
        }
    }

    private ExcelFileHandler() {
    }
}
