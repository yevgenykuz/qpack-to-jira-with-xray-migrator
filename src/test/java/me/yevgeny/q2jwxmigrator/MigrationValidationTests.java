package me.yevgeny.q2jwxmigrator;

import javafx.util.Pair;
import me.yevgeny.q2jwxmigrator.utilities.ExcelFileHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class MigrationValidationTests {
    @Test
    @DisplayName("Test get Qpack test case to Jira test mapping")
    void testGetQpackTestCaseToJiraTestMapping() throws IOException {
        List<Pair<Integer, String>> qpackTestCaseToJiraTestMapping =
                ExcelFileHandler.getInstance().getQpackTestCaseToJiraTestMapping();
        for (Pair<Integer, String> pair : qpackTestCaseToJiraTestMapping) {
            System.out.println(String.format("TC: %s <-> Jira Test: %s", pair.getKey(), pair.getValue()));
        }
    }
}
