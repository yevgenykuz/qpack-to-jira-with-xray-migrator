package com.yevgenyk.q2jwxmigrator.utilities;

public enum JiraFieldKey {

    SUMMARY("Summary"),
    TEST_CATEGORY("Test Category"),
    TEST_CLASSIFICATION("Test Classification"),
    PATH("Path"),
    QPACK_LINK("QPackLink"),
    MANUAL_TEST_STEPS("Manual Test Steps"),
    COMPONENT("Component/s");

    private final String value;

    JiraFieldKey(String v) {
        value = v;
    }

    public String value() {
        return value;
    }
}
