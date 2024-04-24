package com.kvbadev.wms.models.exceptions;

public class EmptyRequestParamException extends BaseException{
    private static final String message = "Query parameter %s has not been set";
    public EmptyRequestParamException(String queryParamName) {
        super(String.format(message, queryParamName));
    }
    public EmptyRequestParamException(Throwable throwable, String queryParamName) {
        super(String.format(message, queryParamName), throwable);
    }
}
