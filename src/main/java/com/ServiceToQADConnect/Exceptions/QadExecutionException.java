package com.ServiceToQADConnect.Exceptions;

/** Indicates that the AppServer connection was established but the requested procedure failed. */
public class QadExecutionException extends RuntimeException {
    public QadExecutionException(String message, Throwable cause) { super(message, cause); }
    public QadExecutionException(String message) { super(message); }
}
