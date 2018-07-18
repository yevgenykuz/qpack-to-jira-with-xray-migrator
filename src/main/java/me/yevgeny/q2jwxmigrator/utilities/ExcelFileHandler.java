package me.yevgeny.q2jwxmigrator.utilities;

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
import java.util.List;

public class ExcelFileHandler {
    public static final String outputFileName = "TestCase_To_Test_Mapping.xlsx";
    public static final String failedTcListFileName = "Failed_To_Convert_TC_List.xlsx";
    private static final Logger logger = Logger.getLogger(ExcelFileHandler.class.getSimpleName());
    private static final String outputFileColumns[] = {"QPACK TC ID", "QPACK TC Link", "JIRA Test ID", "JIRA Test " +
            "link"};
    private static ExcelFileHandler ourInstance;
    private static String tcListFile;

    public static ExcelFileHandler getInstance() throws IOException {
        if (null == ourInstance) {
            ourInstance = new ExcelFileHandler();
            tcListFile = ConfigurationManager.getInstance().getConfigurationValue("tcListFile");
            ourInstance.createOutputFileIfNeeded();
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

    public void appendLineToOutputFile(String qpackTestcaseId, String qpackTestcaseLink, String JiraTestId, String
            JiraTestLink) throws IOException {
        try (FileInputStream outputExcelFileInputStream = new FileInputStream(outputFileName)) {
            Workbook workbook = new XSSFWorkbook(outputExcelFileInputStream);
            Sheet sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();
            Row newRow = sheet.createRow(++lastRowNum);
            newRow.createCell(0).setCellValue(qpackTestcaseId);
            newRow.createCell(1).setCellValue(qpackTestcaseLink);
            newRow.createCell(2).setCellValue(JiraTestId);
            newRow.createCell(3).setCellValue(JiraTestLink);

            logger.debug(String.format("Appending to output file: [%s, %s, %s, %s]", qpackTestcaseId,
                    qpackTestcaseLink, JiraTestId, JiraTestLink));
            writeWorkbookToFile(workbook, outputFileName);
        } catch (IOException e) {
            logger.error("Failed to open file", e);
            throw e;
        }
    }

    public void createFailedTcListFileIfNeeded(List<String> failedTcIds) throws IOException {
        File outputFile = new File(failedTcListFileName);
        if (outputFile.createNewFile()) {
            logger.info(String.format("Creating new output file: %s", failedTcListFileName));
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Failed TC List");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("Failed to convert the following test cases:");
            writeWorkbookToFile(workbook, failedTcListFileName);
        } else {
            logger.info("Failed TC list file already exists, data will be appended");
        }

        try (FileInputStream outputExcelFileInputStream = new FileInputStream(failedTcListFileName)) {
            Workbook workbook = new XSSFWorkbook(outputExcelFileInputStream);
            Sheet sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();

            for (int i = 0; i < failedTcIds.size(); i++) {
                Row newRow = sheet.createRow(++lastRowNum);
                newRow.createCell(0).setCellValue(failedTcIds.get(i));
            }

            logger.debug("Writing failed TC list to file");
            writeWorkbookToFile(workbook, failedTcListFileName);
        } catch (IOException e) {
            logger.error("Failed to open file", e);
            throw e;
        }
    }

    private void createOutputFileIfNeeded() throws IOException {
        File outputFile = new File(outputFileName);
        if (outputFile.createNewFile()) {
            logger.info(String.format("Creating new output file: %s", outputFileName));
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Migration Table");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue(outputFileColumns[0]);
            row.createCell(1).setCellValue(outputFileColumns[1]);
            row.createCell(2).setCellValue(outputFileColumns[2]);
            row.createCell(3).setCellValue(outputFileColumns[3]);
            writeWorkbookToFile(workbook, outputFileName);
        } else {
            logger.info("Output file already exists, data will be appended");
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
