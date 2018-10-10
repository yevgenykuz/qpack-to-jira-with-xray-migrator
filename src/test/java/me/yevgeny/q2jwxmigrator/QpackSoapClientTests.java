package me.yevgeny.q2jwxmigrator;

import me.yevgeny.q2jwxmigrator.model.jiraxrayteststeplist.Step;
import me.yevgeny.q2jwxmigrator.model.jiraxrayteststeplist.XrayTestStepList;
import me.yevgeny.q2jwxmigrator.model.qpackguiobject.QpackGuiObject;
import me.yevgeny.q2jwxmigrator.model.qpackobject.QpackObject;
import me.yevgeny.q2jwxmigrator.model.qpackobject.QpackObjectField;
import me.yevgeny.q2jwxmigrator.model.qpackwebobject.QpackWebObject;
import me.yevgeny.q2jwxmigrator.wsclient.qpack.QpackSoapClient;
import me.yevgeny.q2jwxmigrator.wsclient.qpack.QpackSoapClientException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QpackSoapClientTests {

    @Test
    @DisplayName("Test get Qpack Object")
    void testGetQpackObject() throws QpackSoapClientException {
        int id = 72028;
        String objectFieldNameKey = "Name";
        String objectFieldNameValue = "Stage_0: Pre Condition";
        QpackObject qpackObject = QpackSoapClient.getInstance().getQpackObject(id);
        List<QpackObjectField> objectFields = qpackObject.getFields();
        System.out.println(String.format(" - Fields for TC-%s:", id));
        for (QpackObjectField objectField : objectFields) {
            System.out.println(String.format(" ---> %s: %s", objectField.getName(), objectField.getValue()));
        }
        assertEquals(qpackObject.getFieldValue(objectFieldNameKey), objectFieldNameValue);
    }

    @Test
    @DisplayName("Test get Qpack WebObject")
    void testGetQpackWebObject() throws QpackSoapClientException {
        int id = 73368;
        String objectFieldPathValue = "\\3102\\3103\\30903\\101231\\";
        QpackWebObject qpackwebObject = QpackSoapClient.getInstance().getQpackWebObject(id);
        System.out.println("- Description: " + qpackwebObject.getDescription() + "\n\n");
        List<QpackWebObject.Steps.Step> stepList = qpackwebObject.getSteps().getStep();
        System.out.println("- Total Steps: " + qpackwebObject.getSteps().getStep().size());
        for (QpackWebObject.Steps.Step step : stepList) {
            System.out.println(String.format("---> Step %s [ID=%s]:\nDescription:\n%s\nExpected:\n%s", step.getStepno
                    (), step.getID(), step.getDescription(), step.getExpectedresult()));
        }

        assertEquals(qpackwebObject.getPath(), objectFieldPathValue);
    }

    @Test
    @DisplayName("Test get Qpack GuiObject")
    void testGetQpackGuiObject() throws QpackSoapClientException {
        int id = 71592;
        String expectedObjectPath = "\\LTE eNB\\Templates\\How to Write Test plan - Methodology\\[SR XX.XX] Feature " +
                "Name\\Feature Cross Functional - legacy features\\Multi-cell Cross Feature Testing";
        String actualObjectPath = "";
        QpackGuiObject qpackGuiObject = QpackSoapClient.getInstance().getQpackGuiObject(id);
        List<QpackGuiObject.Section.Path.Item> objectPathItems = qpackGuiObject.getSection().get(0).getPath().getItem();
        for (QpackGuiObject.Section.Path.Item objectPathItem : objectPathItems) {
            actualObjectPath += String.format("\\%s", objectPathItem.getObjName());
        }

        assertEquals(expectedObjectPath, actualObjectPath);
    }

    @Test
    @DisplayName("Test steps conversion to JSON string")
    void testConvertTestCaseStepsToJsonString() throws QpackSoapClientException {
        int id = 73368;
        String expectedContained = "STEP_1";
        QpackWebObject qpackwebObject = QpackSoapClient.getInstance().getQpackWebObject(id);
        List<QpackWebObject.Steps.Step> stepList = qpackwebObject.getSteps().getStep();
        List<Step> xrayStepList = new ArrayList<>();
        for (QpackWebObject.Steps.Step step : stepList) {
            Step s = new Step();
            s.setIndex(step.getStepno());
            s.setStep(step.getDescription());
            s.setResult(step.getExpectedresult());
            xrayStepList.add(s);
        }
        XrayTestStepList xrayTestStepList = new XrayTestStepList();
        xrayTestStepList.setSteps(xrayStepList);
        String result = xrayTestStepList.toJson();
        System.out.println(result);
        assertTrue(result.contains(expectedContained));
    }

    @Test
    @DisplayName("Test SR version extraction from test path")
    void testExtractSrVersionFromTestCasePath() throws QpackSoapClientException {
        int id = 72028;
        String expectedVersion = "SR15.20";
        String actualVersion = "";
        QpackSoapClient qpackSoapClientInstance = QpackSoapClient.getInstance();
        QpackWebObject qpackwebObject = QpackSoapClient.getInstance().getQpackWebObject(id);

        StringBuilder objectNamedPath = new StringBuilder();
        String objectPath = qpackwebObject.getPath();
        String[] objectPathElements = objectPath.split("\\\\");

        for (int i = 1; i < objectPathElements.length; i++) {
            String pathElementName = String.format("\\%s", qpackSoapClientInstance.getQpackObject(Integer.parseInt
                    (objectPathElements[i])).getFieldValue("Name"));

            if (pathElementName.contains("SR")) {
                String re1 = ".*?";    // Non-greedy match on filler
                String re2 = "(SR)";    // SR
                String re3 = ".*?";    // Non-greedy match on filler
                String re4 = "([+-]?\\d*\\.\\d+)(?![-+0-9\\.])";    // any number that matches WX.YZ format
                Pattern p = Pattern.compile(re1 + re2 + re3 + re4, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                Matcher m = p.matcher(pathElementName);
                if (m.find()) {
                    actualVersion = m.group(1) + m.group(2);
                }
            }
            objectNamedPath.append(pathElementName);
        }

        assertEquals(expectedVersion, actualVersion);
    }
}
