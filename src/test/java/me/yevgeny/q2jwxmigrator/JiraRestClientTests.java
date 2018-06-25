package me.yevgeny.q2jwxmigrator;

import me.yevgeny.q2jwxmigrator.model.jirafield.JiraField;
import me.yevgeny.q2jwxmigrator.wsclient.jira.JiraRestClient;
import me.yevgeny.q2jwxmigrator.wsclient.jira.JiraRestClientException;
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
}
