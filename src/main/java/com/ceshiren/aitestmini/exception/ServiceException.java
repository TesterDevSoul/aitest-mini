package com.ceshiren.aitestmini.exception;


public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 4564124491192825748L;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
