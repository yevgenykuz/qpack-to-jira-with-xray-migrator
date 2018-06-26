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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QpackToJiraWithXrayMigrator {
    private static final Logger logger = Logger.getLogger(QpackToJiraWithXrayMigrator.class.getSimpleName());
    private static final String qpackTestcaseUrlPattern = "%s/QPack/Web/1022/QPackItems/View?Item=T_CASE&ItemId=%s";
    private static final String qpackImageLocationPattern = "src=\"%s/QpackFTP/PROJECT_";

    private static final String jiraIssueUrlPrefix = "%s/browse/%s";
    private static final String jiraTestIssueType = "Test";
    private static final String jiraTestDefaultComponent = "SVG Test Repository";

    private static final String jiraDefaultTestVersion = "SR_General";

    public static void main(String[] args) throws QpackSoapClientException, JiraRestClientException, IOException {
        logger.info("Starting migration...");

        ConfigurationManager configurationManagerInstance = ConfigurationManager.getInstance();
        ExcelFileHandler excelFileHandlerInstance = ExcelFileHandler.getInstance();
        QpackSoapClient qpackSoapClientInstance = QpackSoapClient.getInstance();
        JiraRestClient jiraRestClientInstance = JiraRestClient.getInstance();
        DefaultWysiwygConverter converter = new DefaultWysiwygConverter();

        String qpackUrl = configurationManagerInstance.getConfigurationValue("qpackUrl");
        String jiraUrl = configurationManagerInstance.getConfigurationValue("jiraUrl");

        boolean validateFields = true;

        List<Integer> testCaseIds = excelFileHandlerInstance.getTcListFromInputFile();
        int totalTestCases = testCaseIds.size();
        logger.info(String.format("%s QPACK Test Cases will be migrated", totalTestCases));

        //        for (Integer testCaseId : ProgressBar.wrap(testCaseIds, "Migration")) {
        for (Integer testCaseId : testCaseIds) {

            logger.fine(String.format("Fetching TC-%s from QPACK", testCaseId));
            QpackObject qpackObject = qpackSoapClientInstance.getQpackObject(testCaseId);
            QpackWebObject qpackWebObject = qpackSoapClientInstance.getQpackWebObject(testCaseId);

            // build new path for Jira, and extract version from it:
            StringBuilder objectNamedPath = new StringBuilder();
            String actualVersion = "";
            String objectPath = qpackWebObject.getPath();
            String[] objectPathElements = objectPath.split("\\\\");
            for (int i = 1; i < objectPathElements.length; i++) {
                String pathElementName = String.format("\\%s", qpackSoapClientInstance.getQpackObject(Integer.parseInt
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
                splitDescription[1] = splitDescription[1].substring(5);
                String imageUrl = splitDescription[1].substring(0, splitDescription[1].indexOf("\""));
                // remove unnecessary chars from second string:
                splitDescription[1] = splitDescription[1].substring(splitDescription[1].indexOf("</p>"));
                // download file to current folder, and add file name to downloaded files list
                String imageFileName = imageUrl.substring(imageUrl.lastIndexOf("/" + 1));
                FileUtils.copyURLToFile(new URL(imageUrl), new File(imageFileName));
                imagesToUpload.add(imageFileName);
                // replace entire paragraph of image path in description to jira format
                qpackObjectDescriptionValue = String.format("%s !%s|thumbnail!%s", splitDescription[0],
                        imageFileName, splitDescription[1]);
            }
            // convert again for actual jira markup
            qpackObjectDescriptionValue = converter.convertXHtmlToWikiMarkup(qpackObjectDescriptionValue);

            // update qpackOjbect with new description field:
            qpackObject.removeFieldFromObjectFields(QpackFieldKey.DESCRIPTION.value());
            qpackObject.addFieldToObjectFields(QpackFieldKey.DESCRIPTION.value(), qpackObjectDescriptionValue);

            // convert fields to new names:
            qpackObject.renameField(QpackFieldKey.NAME.value(), JiraFieldKey.SUMMARY.value());
            qpackObject.renameField(QpackFieldKey.CATEGORY.value(), JiraFieldKey.TEST_CATEGORY.value());
            qpackObject.renameField(QpackFieldKey.TEST_TYPE.value(), JiraFieldKey.TEST_CLASSIFICATION.value());

            // validate fields - happens only once:
            if (validateFields) {
                jiraRestClientInstance.validateFields(qpackObject.getFields());
                validateFields = false;
            }

            // create issue (of type test) in JIRA
            String jiraIssueKey = jiraRestClientInstance.createIssue(qpackObject.getFields(), jiraTestIssueType);

            // upload all downloaded files as attachments, empty the list
            jiraRestClientInstance.uploadAttachmentsToIssue(imagesToUpload, jiraIssueKey);

            // delete images from local file system:
            for (String image : imagesToUpload) {
                File imageFile = new File(image);
                if (!imageFile.delete()) {
                    logger.warning(String.format("Couldn't delete %s", image));
                }
            }
            imagesToUpload.clear();

            // update output file
            excelFileHandlerInstance.appendLineToOutputFile(testCaseId.toString(), qpackObject.getFieldValue
                    (JiraFieldKey.QPACK_LINK.value()), jiraIssueKey, String.format(jiraIssueUrlPrefix, jiraUrl,
                    jiraIssueKey));
            logger.fine(String.format("QPACK TC-%s was converted to JIRA issue: ", testCaseId));
        }

        logger.info(String.format("Migration complete. Check %s for migration table", ExcelFileHandler
                .outputFileName));
    }
}
