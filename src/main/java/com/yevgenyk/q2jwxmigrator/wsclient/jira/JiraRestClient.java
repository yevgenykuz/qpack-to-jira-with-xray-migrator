package com.yevgenyk.q2jwxmigrator.wsclient.jira;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yevgenyk.q2jwxmigrator.model.jirafield.JiraField;
import com.yevgenyk.q2jwxmigrator.model.jirafield.Schema;
import com.yevgenyk.q2jwxmigrator.utilities.JiraFieldKey;
import com.yevgenyk.q2jwxmigrator.model.qpackobject.QpackObjectField;
import com.yevgenyk.q2jwxmigrator.utilities.ConfigurationManager;
import com.yevgenyk.q2jwxmigrator.utilities.QpackFieldKey;
import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.Component;
import net.rcarz.jiraclient.CustomFieldOption;
import net.rcarz.jiraclient.Field;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

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
                // skip empty values:
                if (null == qpackField.getValue() || qpackField.getValue().isEmpty()) {
                    continue;
                }

                // set affected and fix versions:
                if (qpackField.getName().equals(QpackFieldKey.ACTUAL_VERSION.value())) {
                    issue.field(Field.VERSIONS, new ArrayList<String>() {{
                        add(qpackField.getValue());
                    }});
                    issue.field(Field.FIX_VERSIONS, new ArrayList<String>() {{
                        add(qpackField.getValue());
                    }});
                }

                for (JiraField jiraField : jiraFieldMapping) {
                    if (jiraField.getName().equals(qpackField.getName())) {
                        // these fields require special values:
                        if (qpackField.getName().equals(JiraFieldKey.COMPONENT.value())) {
                            List<Component> testComponents = client.getComponentsAllowedValues(jiraProjectKey,
                                    issueType);
                            boolean foundComponent = false;
                            for (Component testComponent : testComponents) {
                                if (testComponent.getName().equals(qpackField.getValue())) {
                                    issue.field(jiraField.getId(), new ArrayList<Component>() {{
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
                            // these fields are single choice fields that accept string for the key "value":
                            if (jiraField.getSchema().getType().equals("option")) {
                                issue.field(jiraField.getId(), new Field.ValueTuple("value", qpackField.getValue()));
                            } else { // these fields are text fields that accept string value:
                                issue.field(jiraField.getId(), qpackField.getValue());
                            }
                            StringJoiner sj = new StringJoiner(System.lineSeparator());
                            sj.add("Field info:");
                            sj.add(String.format("[%s] Key:%s <-> Value:%s", jiraField.getId(), qpackField.getName(),
                                    qpackField.getValue()));
                            List<CustomFieldOption> customFieldAllowedValues = client.getCustomFieldAllowedValues
                                    (jiraField.getId(), jiraProjectKey, issueType);
                            sj.add("Allowed values:" + Arrays.toString(customFieldAllowedValues.toArray()));
                            Schema schema = jiraField.getSchema();
                            sj.add("Type: " + schema.getType());
                            logger.debug(sj.toString());
                        }
                        break;
                    }
                }
            }

            Issue createdIssue = issue.execute();
            return createdIssue.getKey();
        } catch (JiraException e) {
            e.printStackTrace();
            throw new JiraRestClientException(e.getMessage() + ". Please, make sure all fields are assigned to " +
                    "relevant screens, and that all options were added to option fields");
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
        StringJoiner sj = new StringJoiner(System.lineSeparator());
        for (QpackObjectField field : fields) {
            boolean foundField = false;
            for (JiraField jiraField : jiraFieldMapping) {
                if (jiraField.getName().equals(field.getName())) {
                    foundField = true;
                    break;
                }
            }
            if (!foundField) {
                sj.add(field.getName());

            }
        }
        logger.warn("The following QPACK fields were not found in JIRA:\n" + sj.toString());
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
