package me.yevgeny.q2jwxmigrator;

import com.atlassian.renderer.wysiwyg.converter.DefaultWysiwygConverter;
import me.yevgeny.q2jwxmigrator.model.jiraxrayteststeplist.Step;
import me.yevgeny.q2jwxmigrator.model.jiraxrayteststeplist.XrayTestStepList;
import me.yevgeny.q2jwxmigrator.model.qpackobject.QpackObject;
import me.yevgeny.q2jwxmigrator.model.qpackobject.QpackObjectField;
import me.yevgeny.q2jwxmigrator.model.qpackwebobject.QpackWebObject;
import me.yevgeny.q2jwxmigrator.utilities.ConfigurationManager;
import me.yevgeny.q2jwxmigrator.utilities.ExcelFileHandler;
import me.yevgeny.q2jwxmigrator.utilities.JiraFieldKey;
import me.yevgeny.q2jwxmigrator.utilities.QpackFieldKey;
import me.yevgeny.q2jwxmigrator.wsclient.jira.JiraRestClient;
import me.yevgeny.q2jwxmigrator.wsclient.jira.JiraRestClientException;
import me.yevgeny.q2jwxmigrator.wsclient.qpack.QpackSoapClient;
import me.yevgeny.q2jwxmigrator.wsclient.qpack.QpackSoapClientException;
import org.apache.commons.io.FileUtils;
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

    private static ExcelFileHandler excelFileHandlerInstance;
    private static QpackSoapClient qpackSoapClientInstance;
    private static JiraRestClient jiraRestClientInstance;
    private static DefaultWysiwygConverter converter;

    private static String qpackUrl;
    private static String jiraUrl;

    private static int statusInterval;
    private static List<String> failedTestCases = new ArrayList<>();


    public static void main(String[] args) throws QpackSoapClientException, JiraRestClientException, IOException {
        logger.info("Starting migration...");

        ConfigurationManager configurationManagerInstance = ConfigurationManager.getInstance();
        excelFileHandlerInstance = ExcelFileHandler.getInstance();
        qpackSoapClientInstance = QpackSoapClient.getInstance();
        jiraRestClientInstance = JiraRestClient.getInstance();
        converter = new DefaultWysiwygConverter();

        qpackUrl = configurationManagerInstance.getConfigurationValue("qpackUrl");
        jiraUrl = configurationManagerInstance.getConfigurationValue("jiraUrl");

        List<Integer> testCaseIds = excelFileHandlerInstance.getTcListFromInputFile();
        int totalTestCases = testCaseIds.size();
        if (totalTestCases < 1) {
            logger.error("No test cases found in input file, aborting...");
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
            logger.info(String.format("Migration complete. Check %s for migration table", ExcelFileHandler
                    .outputFileName));
        } catch (InterruptedException e) {
            logger.error("Migration interrupted !", e);
        } finally {
            if (!failedTestCases.isEmpty()) {
                logger.info(String.format("Failed to convert some test cases. Check \"%s\" output file",
                        ExcelFileHandler.failedTcListFileName));
                excelFileHandlerInstance.createFailedTcListFileIfNeeded(failedTestCases);
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
            testCaseConvertionFailed(testCaseId.toString());
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
                pathElementName = String.format("\\%s", qpackSoapClientInstance.getQpackObject(Integer.parseInt
                        (objectPathElements[i])).getFieldValue(QpackFieldKey.NAME.value()));
            } catch (QpackSoapClientException qex) {
                logger.error("Failed to contact QPACK", qex);
                testCaseConvertionFailed(testCaseId.toString());
                return;
            }

            if (pathElementName.contains("SR")) {
                String re1 = ".*?";    // Non-greedy match on filler
                String re2 = "(SR)";    // "SR"
                String re3 = ".*?";    // Non-greedy match on filler
                String re4 = "([+-]?\\d*\\.\\d+)(?![-+0-9\\.])";    // any number that matches WX.YZ format
                Pattern p = Pattern.compile(re1 + re2 + re3 + re4, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                Matcher m = p.matcher(pathElementName);
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
                    testCaseConvertionFailed(testCaseId.toString());
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
        qpackObject.addFieldToObjectFields(JiraFieldKey.QPACK_LINK.value(), String.format(qpackTestcaseUrlPattern,
                qpackUrl, testCaseId));

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
                testCaseConvertionFailed(testCaseId.toString());
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
            testCaseConvertionFailed(testCaseId.toString());
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
        String jiraIssueKey = null;
        try {
            jiraIssueKey = jiraRestClientInstance.createIssue(qpackObject.getFields(), jiraTestIssueType);
        } catch (JiraRestClientException e) {
            logger.error(e.getMessage());
            testCaseConvertionFailed(testCaseId.toString());
            return;
        }

        // upload all downloaded files as attachments, empty the list
        try {
            jiraRestClientInstance.uploadAttachmentsToIssue(imagesToUpload, jiraIssueKey);
        } catch (JiraRestClientException e) {
            logger.error(e.getMessage());
            testCaseConvertionFailed(testCaseId.toString());
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
            excelFileHandlerInstance.appendLineToOutputFile(testCaseId.toString(), qpackObject.getFieldValue
                    (JiraFieldKey.QPACK_LINK.value()), jiraIssueKey, String.format(jiraIssueUrlPrefix, jiraUrl,
                    jiraIssueKey));
        } catch (IOException e) {
            testCaseConvertionFailed(testCaseId.toString());
            return;
        }
        logger.debug(String.format("QPACK TC-%s was converted to JIRA issue: ", testCaseId));
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
            logger.info(String.format("[%s%%] %s test cases analyzed", currentIndex * 100 / totalTestCases,
                    currentIndex));
        }
    }

    private static void testCaseConvertionFailed(String testCaseId) {
        failedTestCases.add(String.format("TC-%s", testCaseId));
        logger.error(String.format("Failed to convert TC-%s", testCaseId));
    }
}
