package com.kvbadev.wms.models.exceptions;

public class EntityNotFoundException extends BaseException {
    private static final String message = "Could not find %s with %s: %s";
    private static final String messageWithId = "Could not find %s with id: %d";
    public EntityNotFoundException(Class<?> classname, String predicate, String identifier) {
        super(String.format(message, classname.getSimpleName(), predicate, identifier));
    }
    public EntityNotFoundException(Class<?> classname, int id) {
        super(String.format(messageWithId, classname.getSimpleName(), id));
    }
}
