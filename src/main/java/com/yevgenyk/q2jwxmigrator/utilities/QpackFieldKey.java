package com.yevgenyk.q2jwxmigrator.utilities;

public enum QpackFieldKey {

    NAME("Name"),
    CATEGORY("Category"),
    TEST_TYPE("Test Type"),
    DESCRIPTION("Description"),
    ACTUAL_VERSION("Actual Version"),
    DUE_DATE("Due Date"),
    STATUS("Status"),
    PRIORITY("Priority");

    private final String value;

    QpackFieldKey(String v) {
        value = v;
    }

    public String value() {
        return value;
    }
}
