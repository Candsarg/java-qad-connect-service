package com.ServiceToQADConnect.Exceptions;

/** Indicates that a connection to the configured OpenEdge AppServer could not be established. */
public class QadConnectionException extends RuntimeException {
    public QadConnectionException(String message, Throwable cause) { super(message, cause); }
}
