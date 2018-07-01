package me.yevgeny.q2jwxmigrator;

import com.atlassian.renderer.wysiwyg.converter.DefaultWysiwygConverter;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
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
import java.util.StringJoiner;
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

    private static StringJoiner failedTestCases = new StringJoiner(" ");


    public static void main(String[] args) throws QpackSoapClientException, JiraRestClientException, IOException,
            InterruptedException {
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
        failedTestCases.add("Failed to convert the following test cases:\n");

        // try converting the first TC - user can abort if fields are missing in JIRA
        convertTestCaseToTest(true, testCaseIds.get(0));

        ProgressBarBuilder pbb = new ProgressBarBuilder()
                .setInitialMax(totalTestCases)
                .setTaskName("Migration")
                .setStyle(ProgressBarStyle.ASCII)
                .setUpdateIntervalMillis(oneSecond);

        // convert all other TC
        for (Integer testCaseId : ProgressBar.wrap(testCaseIds.subList(1, totalTestCases), pbb)) {
            convertTestCaseToTest(false, testCaseId);
        }

        Thread.sleep(oneSecond);
        logger.info(String.format("Migration complete. Check %s for migration table", ExcelFileHandler.outputFileName));
        logger.info(failedTestCases.toString());
    }

    private static void convertTestCaseToTest(boolean validateFields, Integer testCaseId) throws
            QpackSoapClientException, IOException, InterruptedException, JiraRestClientException {
        logger.debug(String.format("Fetching TC-%s from QPACK", testCaseId));
        QpackObject qpackObject;
        QpackWebObject qpackWebObject;
        try {
            qpackObject = qpackSoapClientInstance.getQpackObject(testCaseId);
            qpackWebObject = qpackSoapClientInstance.getQpackWebObject(testCaseId);
        } catch (QpackSoapClientException qex) {
            failedTestCases.add(testCaseId.toString());
            return;
        }

        // build new path for Jira, and extract version from it:
        StringBuilder objectNamedPath = new StringBuilder();
        String actualVersion = "";
        String objectPath = qpackWebObject.getPath();
        String[] objectPathElements = objectPath.split("\\\\");
        for (int i = 1; i < objectPathElements.length; i++) {
            String pathElementName = String.format("\\%s", qpackSoapClientInstance.getQpackObject(Integer
                    .parseInt

                            (objectPathElements[i])).getFieldValue(QpackFieldKey.NAME.value()));
            if (pathElementName.contains("SR")) {
                String versionRegex = "(\\\\SR\\d+\\.\\d+)";
                Pattern p = Pattern.compile(versionRegex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                Matcher m = p.matcher(pathElementName);
                if (m.find()) {
                    String word = m.group(1);
                    actualVersion = word.substring(1);
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
        List<Step> xrayStepList = new ArrayList<>();
        for (QpackWebObject.Steps.Step step : qpackWebObject.getSteps().getStep()) {
            Step s = new Step();
            s.setIndex(step.getStepno());
            s.setStep(converter.convertXHtmlToWikiMarkup(step.getDescription()));
            s.setResult(converter.convertXHtmlToWikiMarkup(step.getExpectedresult()));
            xrayStepList.add(s);
        }
        XrayTestStepList xrayTestStepList = new XrayTestStepList();
        xrayTestStepList.setSteps(xrayStepList);
        QpackObjectField stepsField = new QpackObjectField();
        stepsField.setName(JiraFieldKey.MANUAL_TEST_STEPS.value());
        stepsField.setValue(xrayTestStepList.toJson());
        qpackObject.addFieldToObjectFields(JiraFieldKey.MANUAL_TEST_STEPS.value(), xrayTestStepList.toJson());

        // add link to QPACK TC
        qpackObject.addFieldToObjectFields(JiraFieldKey.QPACK_LINK.value(), String.format
                (qpackTestcaseUrlPattern, qpackUrl, testCaseId));

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
            FileUtils.copyURLToFile(new URL(imageUrl), new File(imageFileName));
            imagesToUpload.add(imageFileName);
            // replace entire paragraph of image path in description to jira format
            qpackObjectDescriptionValue = String.format("%s !%s|thumbnail!%s", splitDescription[0],
                    imageFileName, splitDescription[1]);
        }
        // convert again for actual jira markup
        qpackObjectDescriptionValue = converter.convertXHtmlToWikiMarkup(qpackObjectDescriptionValue);

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
        String jiraIssueKey = jiraRestClientInstance.createIssue(qpackObject.getFields(), jiraTestIssueType);

        // upload all downloaded files as attachments, empty the list
        jiraRestClientInstance.uploadAttachmentsToIssue(imagesToUpload, jiraIssueKey);

        // delete images from local file system:
        for (String image : imagesToUpload) {
            File imageFile = new File(image);
            if (!imageFile.delete()) {
                logger.warn(String.format("Couldn't delete %s", image));
            }
        }
        imagesToUpload.clear();

        // update output file
        excelFileHandlerInstance.appendLineToOutputFile(testCaseId.toString(), qpackObject.getFieldValue
                (JiraFieldKey.QPACK_LINK.value()), jiraIssueKey, String.format(jiraIssueUrlPrefix, jiraUrl,
                jiraIssueKey));
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
}
