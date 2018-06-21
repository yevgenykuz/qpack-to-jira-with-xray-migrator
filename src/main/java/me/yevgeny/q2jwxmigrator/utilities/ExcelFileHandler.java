package me.yevgeny.q2jwxmigrator.utilities;

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
import java.util.logging.Logger;

public class ExcelFileHandler {
    public static final String outputFileName = "TestCase<->Test_Mapping.xlsx";
    private static final Logger logger = Logger.getLogger(ExcelFileHandler.class.getSimpleName());
    private static final String outputFileColumns[] = {"QPACK TC ID", "QPACK TC Link", "JIRA Test ID", "JIRA Test " +
            "link"};
    private static ExcelFileHandler ourInstance = new ExcelFileHandler();
    private static String tcLIstFile;

    public static ExcelFileHandler getInstance() throws IOException {
        if (null == ourInstance) {
            ourInstance = new ExcelFileHandler();
            tcLIstFile = ConfigurationManager.getInstance().getConfigurationValue("tcListFile");
            ourInstance.createOutputFileIfNeeded();
        }
        return ourInstance;
    }

    public List<Integer> getTcListFromInputFile() {
        List<Integer> tcList = new ArrayList<>();

        try (FileInputStream excelFile = new FileInputStream(new File(tcLIstFile))) {
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = sheet.iterator();

            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                Cell currentCell = currentRow.getCell(currentRow.getFirstCellNum());
                if (currentCell.getCellTypeEnum() == CellType.STRING) {
                    if (currentCell.getStringCellValue().contains("TC-")) {
                        // Strip "TC-" from the beginning:
                        tcList.add(Integer.valueOf(currentCell.getStringCellValue().substring(3)));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
            newRow.createCell(1).setCellValue(qpackTestcaseId);
            newRow.createCell(2).setCellValue(qpackTestcaseLink);
            newRow.createCell(3).setCellValue(JiraTestId);
            newRow.createCell(4).setCellValue(JiraTestLink);

            try (FileOutputStream outputExcelFileOutputStream = new FileOutputStream(outputFileName)) {
                logger.info(String.format("Appending to output file: [%s, %s, %s, %s]", qpackTestcaseId,
                        qpackTestcaseLink,
                        JiraTestId, JiraTestLink));
                workbook.write(outputExcelFileOutputStream);
                workbook.close();
            } catch (IOException e) {
                throw e;
            }
        } catch (IOException e) {
            throw e;
        }
    }

    private void createOutputFileIfNeeded() throws IOException {
        File outputFile = new File(outputFileName);
        if (!outputFile.exists()) {
            outputFile.createNewFile();
            logger.info(String.format("Creating new output file: %s", outputFileName));
            appendLineToOutputFile(outputFileColumns[0], outputFileColumns[1], outputFileColumns[2],
                    outputFileColumns[3]);
        } else {
            logger.info(String.format("Output file exists already, data will be appended"));
        }
    }

    private ExcelFileHandler() {
    }
}
