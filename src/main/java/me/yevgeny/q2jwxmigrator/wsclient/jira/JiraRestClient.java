package me.yevgeny.q2jwxmigrator.wsclient.jira;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.yevgeny.q2jwxmigrator.model.jirafield.JiraField;
import me.yevgeny.q2jwxmigrator.model.qpackobject.QpackObjectField;
import me.yevgeny.q2jwxmigrator.utilities.ConfigurationManager;
import me.yevgeny.q2jwxmigrator.utilities.JiraFieldKey;
import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.Component;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;
import net.sf.json.JSON;
import net.sf.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class JiraRestClient {
    private static final Logger logger = Logger.getLogger(JiraRestClient.class.getSimpleName());
    private static JiraRestClient ourInstance;

    private JiraClient client;
    private String jiraUrl;
    private String jiraUsername;
    private String jiraPassword;
    private String jiraProjectKey;
    private List<JiraField> jiraFieldMapping;


    public static JiraRestClient getInstance() throws JiraRestClientException {
        if (null == ourInstance) {
            ourInstance = new JiraRestClient();
            try {
                ourInstance.jiraUrl = ConfigurationManager.getInstance().getConfigurationValue("jiraUrl");
                ourInstance.jiraUsername = ConfigurationManager.getInstance().getConfigurationValue("jiraUsername");
                ourInstance.jiraPassword = ConfigurationManager.getInstance().getConfigurationValue("jiraPassword");
                BasicCredentials credentials = new BasicCredentials(ourInstance.jiraUsername, ourInstance.jiraPassword);
                ourInstance.client = new JiraClient(ourInstance.jiraUrl, credentials);
                ourInstance.jiraProjectKey = ConfigurationManager.getInstance().getConfigurationValue("jiraProjectKey");
                ourInstance.jiraFieldMapping = ourInstance.getJiraFields();
            } catch (Exception e) {
                throw new JiraRestClientException(e.getMessage());
            }
        }

        return ourInstance;
    }

    public Issue getIssue(String issueKey) throws JiraRestClientException {
        try {
            return client.getIssue(issueKey);
        } catch (JiraException e) {
            throw new JiraRestClientException(e.getMessage());
        }
    }

    public String createIssue(List<QpackObjectField> fields, String issueType) throws JiraRestClientException {
        try {
            Issue.FluentCreate issue = client.createIssue(jiraProjectKey, issueType);
            List<JiraField> jiraFieldMapping = getJiraFieldMapping();
            for (QpackObjectField qpackField : fields) {
                for (JiraField jiraField : jiraFieldMapping) {
                    if (jiraField.getName().equals(qpackField.getName())) {
                        // these fields require special values:
                        if (qpackField.getName().equals(JiraFieldKey.COMPONENT.value())) {
                            List<Component> testComponents = client.getComponentsAllowedValues(jiraProjectKey,
                                    issueType);
                            boolean foundComponent = false;
                            for (Component testComponent : testComponents) {
                                if (testComponent.getName().equals(qpackField.getValue())) {
                                    issue.field(jiraField.getId(), new ArrayList() {{
                                        add(testComponent);
                                    }});
                                    foundComponent = true;
                                    break;
                                }
                            }
                            if (!foundComponent) {
                                throw new JiraRestClientException(String.format("Couldn't find component: %s",
                                        qpackField.getValue()));
                            }
                        } else {
                            // these fields accept string value:
                            issue.field(jiraField.getId(), qpackField.getValue());
                        }
                        break;
                    }
                }
            }

            Issue createdIssue = issue.execute();
            return createdIssue.getKey();
        } catch (JiraException e) {
            e.printStackTrace();
            throw new JiraRestClientException(e.getMessage() + ". Make sure all fields are assigned to relevant " +
                    "screens");
        }
    }

    public void uploadAttachmentsToIssue(List<String> attachmentsToUpload, String issueKey) throws
            JiraRestClientException {
        try {
            Issue issue = client.getIssue(issueKey);
            for (String attachment : attachmentsToUpload) {
                issue.addAttachment(new File(attachment));
            }
        } catch (JiraException e) {
            throw new JiraRestClientException(e.getMessage());
        }
    }

    public void validateFields(List<QpackObjectField> fields) {
        List<JiraField> jiraFieldMapping = getJiraFieldMapping();
        for (QpackObjectField field : fields) {
            boolean foundField = false;
            for (JiraField jiraField : jiraFieldMapping) {
                if (jiraField.getName().equals(field.getName())) {
                    foundField = true;
                    break;
                }
            }
            if (!foundField) {
                logger.warning(String.format("QPACK \"%s\" field was not found in JIRA", field.getName()));
            }
        }
    }

    public List<JiraField> getJiraFieldMapping() {
        return jiraFieldMapping;
    }

    private List<JiraField> getJiraFields() throws JiraRestClientException {
        try {

            JSON fieldsAsJson = client.getRestClient().get("/rest/api/2/field");
            if (!fieldsAsJson.isArray()) {
                throw new JiraRestClientException("Couldn't get JIRA fields");
            }

            JSONArray fieldsArray = JSONArray.fromObject(fieldsAsJson);
            List<JiraField> fields = new ArrayList<>(fieldsArray.size());
            final ObjectMapper mapper = new ObjectMapper();

            for (int i = 0; i < fieldsArray.size(); i++) {
                fields.add(mapper.readValue(fieldsArray.getJSONObject(i).toString(), JiraField.class));
            }

            return fields;
        } catch (Exception e) {
            throw new JiraRestClientException(e.getMessage());
        }
    }

    private JiraRestClient() {
    }
}
