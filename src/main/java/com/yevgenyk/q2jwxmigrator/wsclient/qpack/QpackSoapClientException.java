package com.yevgenyk.q2jwxmigrator.wsclient.qpack;

public class QpackSoapClientException extends Exception {

    public QpackSoapClientException(String message) {
        super(String.format("QPACK client error:\n %s", message));
    }
}
