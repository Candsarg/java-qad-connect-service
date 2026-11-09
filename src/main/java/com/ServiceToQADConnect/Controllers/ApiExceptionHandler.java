package com.ServiceToQADConnect.Controllers;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.HttpMediaTypeNotSupportedException;

import com.ServiceToQADConnect.Exceptions.QadConnectionException;
import com.ServiceToQADConnect.Exceptions.QadExecutionException;
import org.json.JSONObject;

/**
 * Centralized JSON error responses. HTTP status and JSON "status" are always identical.
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(QadConnectionException.class)
    public ResponseEntity<String> handleConnection(QadConnectionException ex) {
        return error(HttpStatus.SERVICE_UNAVAILABLE, "QAD_APPSERVER_UNAVAILABLE",
            "Unable to connect to the OpenEdge AppServer. Check AppServer availability, connection limits and configuration.", ex);
    }

    @ExceptionHandler(QadExecutionException.class)
    public ResponseEntity<String> handleExecution(QadExecutionException ex) {
        HttpStatus status;
        String code;
        if (containsTimeout(ex)) {
            status = HttpStatus.GATEWAY_TIMEOUT;
            code = "QAD_APPSERVER_TIMEOUT";
        } else if (isUnavailable(ex)) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
            code = "QAD_APPSERVER_CAPACITY_OR_UNAVAILABLE";
        } else {
            status = HttpStatus.BAD_GATEWAY;
            code = "QAD_PROCEDURE_FAILED";
        }
        return error(status, code, ex.getMessage(), ex);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleStatus(ResponseStatusException ex) {
        HttpStatus status = HttpStatus.resolve(ex.getRawStatusCode());
        if (status == null) status = HttpStatus.INTERNAL_SERVER_ERROR;
        return error(status, status.name(), ex.getReason(), ex);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleBadJson(HttpMessageNotReadableException ex) {
        return error(HttpStatus.BAD_REQUEST, "INVALID_JSON",
            "Request body is missing or contains invalid JSON / field types.", ex);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleMethod(HttpRequestMethodNotSupportedException ex) {
        return error(HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED",
            "HTTP method is not supported for this endpoint.", ex);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<String> handleMedia(HttpMediaTypeNotSupportedException ex) {
        return error(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "UNSUPPORTED_MEDIA_TYPE",
            "Use Content-Type: application/json.", ex);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParameter(MissingServletRequestParameterException ex) {
        return error(HttpStatus.BAD_REQUEST, "MISSING_PARAMETER", ex.getMessage(), ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnexpected(Exception ex) {
        HttpStatus status = containsTimeout(ex) ? HttpStatus.GATEWAY_TIMEOUT : HttpStatus.INTERNAL_SERVER_ERROR;
        return error(status, status == HttpStatus.GATEWAY_TIMEOUT ? "REQUEST_TIMEOUT" : "INTERNAL_SERVER_ERROR",
            "Unexpected Java service error: " + safeMessage(ex), ex);
    }

    private ResponseEntity<String> error(HttpStatus status, String code, String message, Exception ex) {
        String safe = (message == null || message.trim().isEmpty()) ? status.getReasonPhrase() : message;
        JSONObject body = new JSONObject();
        body.put("success", false);
        body.put("status", status.value());
        body.put("error", code);
        body.put("message", safe);
        System.err.println("[HTTP " + status.value() + "] " + code + " - " + safe);
        if (ex != null) ex.printStackTrace();
        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body(body.toString());
    }

    private boolean isUnavailable(Throwable ex) {
        Throwable current = ex;
        while (current != null) {
            String message = current.getMessage();
            if (message != null) {
                String lower = message.toLowerCase();
                if (lower.contains("max client connections") ||
                    lower.contains("no servers available") ||
                    lower.contains("noavailablesessions") ||
                    lower.contains("session pool") ||
                    lower.contains("connection refused") ||
                    lower.contains("unable to establish")) return true;
            }
            current = current.getCause();
        }
        return false;
    }

    private boolean containsTimeout(Throwable ex) {
        Throwable current = ex;
        while (current != null) {
            if (current instanceof SocketTimeoutException || current instanceof TimeoutException) return true;
            current = current.getCause();
        }
        return false;
    }

    private String safeMessage(Throwable ex) {
        return ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
    }
}
