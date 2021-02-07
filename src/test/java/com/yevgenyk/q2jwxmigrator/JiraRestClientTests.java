package com.yevgenyk.q2jwxmigrator;

import com.yevgenyk.q2jwxmigrator.model.jirafield.JiraField;
import com.yevgenyk.q2jwxmigrator.wsclient.jira.JiraRestClient;
import com.yevgenyk.q2jwxmigrator.wsclient.jira.JiraRestClientException;
import net.rcarz.jiraclient.Issue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JiraRestClientTests {
    @Test
    @DisplayName("Test get JIRA issue")
    void testGetJiraIssue() throws JiraRestClientException {
        String issueKey = "KP-6";
        Issue jiraIssue = JiraRestClient.getInstance().getIssue(issueKey);
        assertEquals(issueKey, jiraIssue.getKey());
    }

    @Test
    @DisplayName("Test get JIRA fields")
    void testGetJiraFields() throws JiraRestClientException {
        String qpackLinkJiraFieldName = "QPackLink";
        int i = 0;
        List<JiraField> jiraFields = JiraRestClient.getInstance().getJiraFieldMapping();
        for (JiraField jiraField : jiraFields) {
            System.out.println("###");
            System.out.println(jiraField.getName());
            System.out.println(jiraField.getId());
            if (jiraField.getName().equals(qpackLinkJiraFieldName)) {
                i++;
            }
        }
        assertTrue(i == 1);
    }

    @Test
    @DisplayName("Test get JIRA test path")
    void testGetJiraTestPath() throws JiraRestClientException {
        String issueKey = "SIR-12358";
        String expectedObjectPath = "\\LTE eNB\\Templates\\How to Write Test plan - Methodology\\[SR XX.XX] Feature " +
                "Name\\Feature Cross Functional\\Multi-cell Cross Feature Testing";
        String pathFieldKey = "Path";
        String pathFieldId = "";
        List<JiraField> jiraFields = JiraRestClient.getInstance().getJiraFieldMapping();
        for (JiraField jiraField : jiraFields) {
            if (jiraField.getName().equals(pathFieldKey)) {
                pathFieldId = jiraField.getId();
            }
        }

        Issue jiraIssue = JiraRestClient.getInstance().getIssue(issueKey);
        Object field = jiraIssue.getField(pathFieldId);

        assertEquals(expectedObjectPath, field.toString());
    }
}
