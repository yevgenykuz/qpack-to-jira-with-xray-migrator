package me.yevgeny.q2jwxmigrator;

import com.atlassian.renderer.wysiwyg.converter.DefaultWysiwygConverter;
import me.yevgeny.q2jwxmigrator.model.qpackObject.QpackObject;
import me.yevgeny.q2jwxmigrator.model.qpackWebObject.QpackWebObject;
import me.yevgeny.q2jwxmigrator.utilities.ConfigurationManager;
import me.yevgeny.q2jwxmigrator.utilities.ExcelFileHandler;
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

public class QpackToJiraWithXrayMigrator {
    private static final Logger logger = Logger.getLogger(QpackToJiraWithXrayMigrator.class.getSimpleName());
    private static final String qpackTestcaseUrlPattern = "/QPack/Web/1022/QPackItems/View?Item=T_CASE&ItemId=";
    private static final String qpackImageLocationPrefix = "src=\"http://FS28//QpackFTP/PROJECT_";

    private static final String qpackObjectPathFieldName = "Path";
    private static final String qpackObjectQpackLinkFieldName = "QPackLink";
    private static final String qpackObjectDescriptionFieldName = "Description";

    // qpack fields to rename when moving to jira:
    private static final String qpackObjectNameFieldName = "Name";
    private static final String qpackObjectCategoryFieldName = "Category";
    private static final String qpackObjectTestTypeFieldName = "Test Type";


    public static void main(String[] args) throws QpackSoapClientException, JiraRestClientException, IOException {
        logger.info("Starting migration...");

        ConfigurationManager configurationManagerInstance = ConfigurationManager.getInstance();
        ExcelFileHandler excelFileHandlerInstance = ExcelFileHandler.getInstance();
        QpackSoapClient qpackSoapClientInstance = QpackSoapClient.getInstance();
        JiraRestClient jiraRestClientInstance = JiraRestClient.getInstance();
        DefaultWysiwygConverter converter = new DefaultWysiwygConverter();

        List<Integer> TestCaseIds = excelFileHandlerInstance.getTcListFromInputFile();
        for (Integer testCaseId : TestCaseIds) {
            QpackObject qpackObject = qpackSoapClientInstance.getQpackObject(testCaseId);
            QpackWebObject qpackWebObject = qpackSoapClientInstance.getQpackWebObject(testCaseId);

            List<String> imagesToUpload = new ArrayList<>();

            // build new path for Jira:
            String objectNamedPath = new String();
            String objectPath = qpackWebObject.getPath();
            String[] objectPathElements = objectPath.split("\\\\");
            for (String objectPathElement : objectPathElements) {
                objectNamedPath += String.format("\\%s", qpackSoapClientInstance.getQpackObject(Integer.parseInt
                        (objectPathElement)).getFieldValue(qpackObjectNameFieldName));
            }

            // update qpackOjbect with new path field:
            qpackObject.addFieldToObjectFields(qpackObjectPathFieldName, objectNamedPath);

            // add link to QPACK TC
            qpackObject.addFieldToObjectFields(qpackObjectQpackLinkFieldName, configurationManagerInstance
                    .getConfigurationValue
                            ("qpackUrl") + qpackTestcaseUrlPattern + testCaseId);

            // convert description to jira markup
            String qpackObjectDescriptionValue = qpackObject.getFieldValue(qpackObjectDescriptionFieldName);
            // convert once for xhtml
            qpackObjectDescriptionValue = converter.convertXHtmlToWikiMarkup(qpackObjectDescriptionValue);
            // if needed, download images from qpack and update description field
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

            // update qpackOjbect with new path field:
            qpackObject.removeFieldFromObjectFields(qpackObjectDescriptionFieldName);
            qpackObject.addFieldToObjectFields(qpackObjectDescriptionFieldName, qpackObjectDescriptionValue);

            // convert fields to new names:
            qpackObject.renameField(qpackObjectNameFieldName, "Summary");
            qpackObject.renameField(qpackObjectCategoryFieldName, "Test Category");
            qpackObject.renameField(qpackObjectTestTypeFieldName, "Test Classification");

            // TODO - create issue (of type test) in JIRA
            String jiraId = new String();
            String jiraLink = new String();

            // TODO - upload all downloaded files as attachments, empty the list

            // update output file
            excelFileHandlerInstance.appendLineToOutputFile(testCaseId.toString(), qpackObject.getFieldValue
                    (qpackObjectQpackLinkFieldName), jiraId, jiraLink);
        }

        logger.info(String.format("Migration complete. Check %s for migration table", excelFileHandlerInstance
                .outputFileName));
    }
}
