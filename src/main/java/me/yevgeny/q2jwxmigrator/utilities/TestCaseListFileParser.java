package me.yevgeny.q2jwxmigrator.utilities;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class TestCaseListFileParser {
    private static final Logger logger = Logger.getLogger(TestCaseListFileParser.class.getSimpleName());
    private static TestCaseListFileParser ourInstance = new TestCaseListFileParser();

    private String tcLIstFile;

    public static TestCaseListFileParser getInstance() throws FileNotFoundException {
        if (null == ourInstance) {
            ourInstance = new TestCaseListFileParser();
            ourInstance.tcLIstFile = ConfigurationManager.getInstance().getConfigurationValue("tcListFile");
        }
        return ourInstance;
    }

    public List<Integer> getTcListFromFile() {
        List<Integer> tcList = new ArrayList<>();

        Workbook workbook;
        try (FileInputStream excelFile = new FileInputStream(new File(ourInstance.tcLIstFile))) {
            workbook = new XSSFWorkbook(excelFile);
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

    private TestCaseListFileParser() {
    }
}
