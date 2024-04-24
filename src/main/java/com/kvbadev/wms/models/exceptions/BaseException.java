package com.kvbadev.wms.models.exceptions;

public class BaseException extends RuntimeException {
    public BaseException() {
        super();
    }
    public BaseException(Throwable throwable) {
        super(throwable);
    }
    public BaseException(String message) {
        super(message);
    }
    public BaseException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
