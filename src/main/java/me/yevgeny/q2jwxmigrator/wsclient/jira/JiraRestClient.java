package me.yevgeny.q2jwxmigrator.wsclient.jira;

import me.yevgeny.q2jwxmigrator.utilities.ConfigurationManager;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

public class JiraRestClient {
    private static final Logger logger = Logger.getLogger(JiraRestClient.class.getSimpleName());
    private static JiraRestClient ourInstance;

    private String jiraUrl;
    private String jiraUsername;
    private String jiraPassword;


    public static JiraRestClient getInstance() throws JiraRestClientException {
        if (null == ourInstance) {
            ourInstance = new JiraRestClient();
            try {
                ourInstance.jiraUrl = ConfigurationManager.getInstance().getConfigurationValue("jiraUrl");
                ourInstance.jiraUsername = ConfigurationManager.getInstance().getConfigurationValue("jiraUsername");
                ourInstance.jiraPassword = ConfigurationManager.getInstance().getConfigurationValue("jiraPassword");
            } catch (FileNotFoundException e) {
                throw new JiraRestClientException(e.getMessage());
            }
        }

        return ourInstance;
    }

    // Create issue of given type with given parameters

    // Upload attachment to given issue

    private JiraRestClient() {
    }
}
