package com.yevgenyk.q2jwxmigrator;

import com.atlassian.renderer.wysiwyg.converter.DefaultWysiwygConverter;
import com.yevgenyk.q2jwxmigrator.model.jirafield.JiraField;
import com.yevgenyk.q2jwxmigrator.model.qpackobject.QpackObject;
import com.yevgenyk.q2jwxmigrator.utilities.JiraFieldKey;
import com.yevgenyk.q2jwxmigrator.model.jiraxrayteststeplist.Step;
import com.yevgenyk.q2jwxmigrator.model.jiraxrayteststeplist.XrayTestStepList;
import com.yevgenyk.q2jwxmigrator.model.qpackguiobject.QpackGuiObject;
import com.yevgenyk.q2jwxmigrator.model.qpackobject.QpackObjectField;
import com.yevgenyk.q2jwxmigrator.model.qpackwebobject.QpackWebObject;
import com.yevgenyk.q2jwxmigrator.utilities.ConfigurationManager;
import com.yevgenyk.q2jwxmigrator.utilities.ExcelFileHandler;
import com.yevgenyk.q2jwxmigrator.utilities.QpackFieldKey;
import com.yevgenyk.q2jwxmigrator.wsclient.jira.JiraRestClient;
import com.yevgenyk.q2jwxmigrator.wsclient.jira.JiraRestClientException;
import com.yevgenyk.q2jwxmigrator.wsclient.qpack.QpackSoapClient;
import com.yevgenyk.q2jwxmigrator.wsclient.qpack.QpackSoapClientException;
import net.rcarz.jiraclient.Field;
import net.rcarz.jiraclient.Issue;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QpackToJiraWithXrayMigrator {
    private static final Logger logger = Logger.getLogger(QpackToJiraWithXrayMigrator.class.getSimpleName());
    private static final String qpackTestcaseUrlPattern = "%s/QPack/Web/1022/QPackItems/View?Item=T_CASE&ItemId=%s";
    private static final String qpackImageLocationPattern = "src=\"%s//QpackFTP/PROJECT_";

    private static final String jiraIssueUrlPrefix = "%s/browse/%s";
    private static final String jiraTestIssueType = "Test";
    private static final String jiraTestDefaultComponent = "SVG Test Repository";

    private static final String jiraDefaultTestVersion = "SR_General";

    private static final int oneSecond = 1000;

    private static ConfigurationManager configurationManagerInstance;
    private static ExcelFileHandler excelFileHandlerInstance;
    private static QpackSoapClient qpackSoapClientInstance;
    private static JiraRestClient jiraRestClientInstance;
    private static DefaultWysiwygConverter converter;

    private static String qpackUrl;
    private static String jiraUrl;

    private static int statusInterval;
    private static List<String> failedTestCases = new ArrayList<>();
    private static List<String> failedTests = new ArrayList<>();


    public static void main(String[] args) throws QpackSoapClientException, JiraRestClientException, IOException {
        init();
        migrate();
        validateMigration();
    }

    private static void init() throws IOException, QpackSoapClientException, JiraRestClientException {
        logger.info("Initializing ...");
        configurationManagerInstance = ConfigurationManager.getInstance();
        excelFileHandlerInstance = ExcelFileHandler.getInstance();
        qpackSoapClientInstance = QpackSoapClient.getInstance();
        jiraRestClientInstance = JiraRestClient.getInstance();
        converter = new DefaultWysiwygConverter();

        qpackUrl = configurationManagerInstance.getConfigurationValue("qpackUrl");
        jiraUrl = configurationManagerInstance.getConfigurationValue("jiraUrl");
    }

    private static void migrate() throws IOException {
        logger.info("Starting migration ...");

        List<Integer> testCaseIds = excelFileHandlerInstance.getTcListFromInputFile();
        int totalTestCases = testCaseIds.size();
        if (totalTestCases < 1) {
            logger.error("No test cases found in input file, aborting ...");
            System.exit(1);
        }

        logger.info(String.format("%s QPACK Test Cases will be migrated", totalTestCases));
        statusInterval = (int) (totalTestCases * (1.0f / 100.0f));
        statusInterval = (statusInterval == 0) ? 1 : statusInterval;

        try {
            // try converting the first TC - user can abort if fields are missing in JIRA
            convertTestCaseToTest(true, testCaseIds.get(0));

            // convert all other TC
            for (int i = 1; i < testCaseIds.size(); i++) {
                convertTestCaseToTest(false, testCaseIds.get(i));
                printStatus(i, totalTestCases);
            }
            Thread.sleep(oneSecond);
            logger.info(
                    String.format("Migration complete. Check %s for migration table", ExcelFileHandler.outputFileName));
        } catch (InterruptedException e) {
            logger.error("Migration interrupted !", e);
        } finally {
            if (!failedTestCases.isEmpty()) {
                logger.info(String.format("Failed to convert some test cases. Check \"%s\" output file",
                        ExcelFileHandler.failedTcListFileName));
                excelFileHandlerInstance.createFailedTcListFileIfNeeded(failedTestCases,
                        ExcelFileHandler.failedTcListFileName);
            }
        }
    }

    private static void convertTestCaseToTest(boolean validateFields, Integer testCaseId) throws InterruptedException {
        logger.debug(String.format("Fetching TC-%s from QPACK", testCaseId));
        QpackObject qpackObject;
        QpackWebObject qpackWebObject;
        try {
            qpackObject = qpackSoapClientInstance.getQpackObject(testCaseId);
            qpackWebObject = qpackSoapClientInstance.getQpackWebObject(testCaseId);
        } catch (QpackSoapClientException qex) {
            logger.error("Failed to contact QPACK", qex);
            testCaseConversionFailed(testCaseId.toString());
            return;
        }


        // build new path for Jira, and extract version from it:
        StringBuilder objectNamedPath = new StringBuilder();
        String actualVersion = "";
        String objectPath = qpackWebObject.getPath();
        String[] objectPathElements = objectPath.split("\\\\");
        for (int i = 1; i < objectPathElements.length; i++) {
            String pathElementName;
            try {
                pathElementName = String.format("\\%s",
                        qpackSoapClientInstance.getQpackObject(Integer.parseInt(objectPathElements[i]))
                                .getFieldValue(QpackFieldKey.NAME.value()));
            } catch (QpackSoapClientException qex) {
                logger.error("Failed to contact QPACK", qex);
                testCaseConversionFailed(testCaseId.toString());
                return;
            }

            if (pathElementName.contains("SR")) {
                Matcher m = createActualVersionRegexMatcher(pathElementName);
                if (m.find()) {
                    if (actualVersion.isEmpty()) {
                        actualVersion = m.group(1) + m.group(2);
                    }
                }
            }
            objectNamedPath.append(pathElementName);
        }
        if (actualVersion.isEmpty()) {
            actualVersion = jiraDefaultTestVersion;
        }


        // update qpackOjbect with new path field:
        qpackObject.addFieldToObjectFields(JiraFieldKey.PATH.value(), objectNamedPath.toString());

        // update qpackObject with new version field:
        qpackObject.addFieldToObjectFields(QpackFieldKey.ACTUAL_VERSION.value(), actualVersion);

        // update qpackObject with new component field:
        qpackObject.addFieldToObjectFields(JiraFieldKey.COMPONENT.value(), jiraTestDefaultComponent);

        // add TC steps from qpackWebObject
        if (qpackWebObject.getSteps() != null) {
            List<Step> xrayStepList = new ArrayList<>();
            for (QpackWebObject.Steps.Step step : qpackWebObject.getSteps().getStep()) {
                Step s = new Step();
                s.setIndex(step.getStepno());
                try {
                    s.setStep(converter.convertXHtmlToWikiMarkup(step.getDescription()));
                    s.setResult(converter.convertXHtmlToWikiMarkup(step.getExpectedresult()));
                } catch (Exception e) {
                    logger.error("Failed to convert QPACK HTML step description to JIRA markup", e);
                    testCaseConversionFailed(testCaseId.toString());
                    return;
                }
                xrayStepList.add(s);
            }
            XrayTestStepList xrayTestStepList = new XrayTestStepList();
            xrayTestStepList.setSteps(xrayStepList);
            QpackObjectField stepsField = new QpackObjectField();
            stepsField.setName(JiraFieldKey.MANUAL_TEST_STEPS.value());
            stepsField.setValue(xrayTestStepList.toJson());
            qpackObject.addFieldToObjectFields(JiraFieldKey.MANUAL_TEST_STEPS.value(), xrayTestStepList.toJson());
        }

        // add link to QPACK TC
        qpackObject.addFieldToObjectFields(JiraFieldKey.QPACK_LINK.value(),
                String.format(qpackTestcaseUrlPattern, qpackUrl, testCaseId));

        // convert description to jira markup
        List<String> imagesToUpload = new ArrayList<>();
        String qpackObjectDescriptionValue = qpackObject.getFieldValue(QpackFieldKey.DESCRIPTION.value());
        // convert once for xhtml
        qpackObjectDescriptionValue = converter.convertXHtmlToWikiMarkup(qpackObjectDescriptionValue);

        // if needed, download images from qpack and update description field
        String qpackImageLocationPrefix = String.format(qpackImageLocationPattern, qpackUrl);
        while (qpackObjectDescriptionValue.contains(qpackImageLocationPrefix)) {
            String[] splitDescription = qpackObjectDescriptionValue.split(qpackImageLocationPrefix, 2);
            // remove unnecessary chars from first string
            splitDescription[0] = splitDescription[0].substring(0, splitDescription[0].lastIndexOf("<p>") + 3);
            // extract image url
            String imageUrl = splitDescription[1].substring(0, splitDescription[1].indexOf("\""));
            imageUrl = qpackImageLocationPrefix.substring(5) + imageUrl;
            // remove unnecessary chars from second string:
            splitDescription[1] = splitDescription[1].substring(splitDescription[1].indexOf("</p>"));
            // download file to current folder, and add file name to downloaded files list
            String imageFileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            try {
                FileUtils.copyURLToFile(new URL(imageUrl), new File(imageFileName));
            } catch (IOException e) {
                logger.error("Failed to download attachment from QPACK", e);
                testCaseConversionFailed(testCaseId.toString());
                return;
            }
            imagesToUpload.add(imageFileName);
            // replace entire paragraph of image path in description to jira format
            qpackObjectDescriptionValue = String.format("%s !%s|thumbnail!%s", splitDescription[0], imageFileName,
                    splitDescription[1]);
        }
        // convert again for actual jira markup
        try {
            qpackObjectDescriptionValue = converter.convertXHtmlToWikiMarkup(qpackObjectDescriptionValue);
        } catch (Exception e) {
            logger.error("Failed to convert QPACK HTML description to JIRA markup", e);
            testCaseConversionFailed(testCaseId.toString());
            return;
        }

        // fix thumbnail escape characters
        qpackObjectDescriptionValue = qpackObjectDescriptionValue.replace("\\!", "!").replace("\\|", "|");

        // update qpackOjbect with new description field:
        qpackObject.removeFieldFromObjectFields(QpackFieldKey.DESCRIPTION.value());
        qpackObject.addFieldToObjectFields(QpackFieldKey.DESCRIPTION.value(), qpackObjectDescriptionValue);

        // convert fields to new names:
        qpackObject.renameField(QpackFieldKey.NAME.value(), JiraFieldKey.SUMMARY.value());
        qpackObject.renameField(QpackFieldKey.CATEGORY.value(), JiraFieldKey.TEST_CATEGORY.value());
        qpackObject.renameField(QpackFieldKey.TEST_TYPE.value(), JiraFieldKey.TEST_CLASSIFICATION.value());

        // remove unnecessary fields:
        qpackObject.removeFieldFromObjectFields(QpackFieldKey.DUE_DATE.value());
        qpackObject.removeFieldFromObjectFields(QpackFieldKey.STATUS.value());
        qpackObject.removeFieldFromObjectFields(QpackFieldKey.PRIORITY.value());

        // validate fields - happens only once:
        if (validateFields) {
            validateFields(qpackObject);
        }

        // create issue (of type test) in JIRA
        String jiraIssueKey;
        try {
            jiraIssueKey = jiraRestClientInstance.createIssue(qpackObject.getFields(), jiraTestIssueType);
        } catch (JiraRestClientException e) {
            logger.error(e.getMessage());
            testCaseConversionFailed(testCaseId.toString());
            return;
        }

        // upload all downloaded files as attachments, empty the list
        try {
            jiraRestClientInstance.uploadAttachmentsToIssue(imagesToUpload, jiraIssueKey);
        } catch (JiraRestClientException e) {
            logger.error(e.getMessage());
            testCaseConversionFailed(testCaseId.toString());
            return;
        }

        // delete images from local file system:
        for (String image : imagesToUpload) {
            File imageFile = new File(image);
            if (!imageFile.delete()) {
                logger.warn(String.format("Couldn't delete %s", image));
            }
        }
        imagesToUpload.clear();

        // update output file
        try {
            excelFileHandlerInstance.appendLineToOutputFile(ExcelFileHandler.outputFileName, testCaseId.toString(),
                    qpackObject.getFieldValue(JiraFieldKey.QPACK_LINK.value()), jiraIssueKey,
                    String.format(jiraIssueUrlPrefix, jiraUrl, jiraIssueKey));
        } catch (IOException e) {
            testCaseConversionFailed(testCaseId.toString());
            return;
        }
        logger.debug(String.format("QPACK TC-%s was converted to JIRA issue: ", testCaseId));
    }

    private static Matcher createActualVersionRegexMatcher(String pathElementName) {
        String re1 = ".*?";    // Non-greedy match on filler
        String re2 = "(SR)";    // "SR"
        String re3 = ".*?";    // Non-greedy match on filler
        String re4 = "([+-]?\\d*\\.\\d+)(?![-+0-9\\.])";    // any number that matches WX.YZ format
        Pattern p = Pattern.compile(re1 + re2 + re3 + re4, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        return p.matcher(pathElementName);
    }

    private static void validateFields(QpackObject qpackObject) throws InterruptedException {
        jiraRestClientInstance.validateFields(qpackObject.getFields());
        Thread.sleep(oneSecond);
        System.out.println("\nCheck ^missing fields^, press \"ENTER\" to continue ...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        System.out.println("Continuing migration ...");
    }

    private static void printStatus(int currentIndex, int totalTestCases) {
        if (currentIndex % statusInterval == 0) {
            logger.info(
                    String.format("[%s%%] %s test cases analyzed", currentIndex * 100 / totalTestCases, currentIndex));
        }
    }

    private static void testCaseConversionFailed(String testCaseId) {
        failedTestCases.add(String.format("TC-%s", testCaseId));
        logger.error(String.format("Failed to convert TC-%s", testCaseId));
    }

    private static void testValidationFailed(String testCaseId, Throwable e) {
        failedTests.add(testCaseId);
        logger.error(String.format("Failed to validate TC-%s\n", testCaseId), e);
    }


    private static void validateMigration() throws JiraRestClientException, IOException {
        // validate "path" field in JIRA due to inconsistency in QPACK DB

        logger.info("Starting migration validation ...");

        List<Pair<Integer, String>> qpackTestCaseToJiraTestMapping =
                excelFileHandlerInstance.getQpackTestCaseToJiraTestMapping();

        int totalTests = qpackTestCaseToJiraTestMapping.size();
        if (totalTests < 1) {
            logger.error("No tests found for validation, aborting validation ...");
            System.exit(1);
        }

        statusInterval = (int) (totalTests * (1.0f / 100.0f));
        statusInterval = (statusInterval == 0) ? 1 : statusInterval;

        logger.info(String.format("%s Jira tests will be validated", totalTests));

        logger.info(String.format("Validating \"%s\" field", JiraFieldKey.PATH.value()));

        String pathFieldId = "";
        List<JiraField> jiraFields = jiraRestClientInstance.getJiraFieldMapping();
        for (JiraField jiraField : jiraFields) {
            if (jiraField.getName().equals(JiraFieldKey.PATH.value())) {
                pathFieldId = jiraField.getId();
            }
        }

        if (pathFieldId.isEmpty()) {
            throw new JiraRestClientException(String.format("Couldn't find %s field in JIRA", JiraFieldKey.PATH));
        }

        try {
            for (int i = 0; i < qpackTestCaseToJiraTestMapping.size(); i++) {
                validateTestCase(qpackTestCaseToJiraTestMapping.get(i), pathFieldId);
                printStatus(i, totalTests);
            }
        } catch (Exception e) {
            logger.error("Validation failed:", e);
        } finally {
            if (!failedTests.isEmpty()) {
                logger.info(String.format("Failed to validate some tests. Check \"%s\" output file",
                        ExcelFileHandler.validationFailedTestListFileName));
                excelFileHandlerInstance.createFailedTcListFileIfNeeded(failedTests,
                        ExcelFileHandler.validationFailedTestListFileName);
            }
        }
        logger.info(String.format("Migration validation complete. Check %s for details",
                ExcelFileHandler.validationOutputFileName));
    }

    private static void validateTestCase(Pair<Integer, String> testCaseToTestMappingPair, String pathFieldId) {
        Integer testCaseID = testCaseToTestMappingPair.getKey();
        try {
            StringBuilder qpackObjectPath = new StringBuilder();
            QpackGuiObject qpackGuiObject = qpackSoapClientInstance.getQpackGuiObject(testCaseID);
            List<QpackGuiObject.Section.Path.Item> objectPathItems = qpackGuiObject.getSection().get(0).getPath()
                    .getItem();
            for (QpackGuiObject.Section.Path.Item objectPathItem : objectPathItems) {
                qpackObjectPath.append(String.format("\\%s", objectPathItem.getObjName()));
            }

            Issue jiraIssue = jiraRestClientInstance.getIssue(testCaseToTestMappingPair.getValue());
            Object jiraPathField = jiraIssue.getField(pathFieldId);
            String jiraObjectPath = jiraPathField.toString();

            if (!qpackObjectPath.toString().equals(jiraObjectPath)) {
                Issue.FluentUpdate issueUpdate = jiraIssue.update();

                // update jira path to path from qpack:
                issueUpdate.field(pathFieldId, qpackObjectPath);

                // fix versions in JIRA to relevant version:
                if (qpackObjectPath.toString().contains("SR")) {
                    String updatedVersion = "";
                    for (String pathElementName : qpackObjectPath.toString().split("\\\\")) {
                        if (pathElementName.contains("SR")) {
                            Matcher m = createActualVersionRegexMatcher(pathElementName);
                            if (m.find()) {
                                if (updatedVersion.isEmpty()) {
                                    updatedVersion = m.group(1) + m.group(2);
                                    break;
                                }
                            }
                        }
                    }

                    String currentVersion = jiraIssue.getFixVersions().get(0).getName();
                    if (!updatedVersion.isEmpty() && !currentVersion.equals(updatedVersion)) {
                        String finalUpdatedVersion = updatedVersion;
                        issueUpdate.field(Field.VERSIONS, new ArrayList<String>() {{
                            add(finalUpdatedVersion);
                        }});
                        issueUpdate.field(Field.FIX_VERSIONS, new ArrayList<String>() {{
                            add(finalUpdatedVersion);
                        }});
                    }
                }

                issueUpdate.execute();
                excelFileHandlerInstance.appendLineToOutputFile(ExcelFileHandler.validationOutputFileName,
                        testCaseID.toString(), qpackObjectPath.toString(), testCaseToTestMappingPair.getValue(),
                        jiraObjectPath);
            }
        } catch (Exception e) {
            testValidationFailed(testCaseID.toString(), e);
        }
    }
}
