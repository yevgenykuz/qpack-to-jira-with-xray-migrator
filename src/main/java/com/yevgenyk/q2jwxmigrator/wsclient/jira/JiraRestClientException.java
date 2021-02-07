package com.yevgenyk.q2jwxmigrator.wsclient.jira;

public class JiraRestClientException extends Exception {

    public JiraRestClientException(String message) {
        super(String.format("Jira client error:\n %s", message));
    }
}
