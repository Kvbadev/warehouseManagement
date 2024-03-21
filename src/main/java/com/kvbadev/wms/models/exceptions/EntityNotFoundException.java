package com.kvbadev.wms.models.exceptions;

public class EntityNotFoundException extends BaseException {
    private static final String message = "Could not find %s with %s: %d";
    private static final String messageWithId = "Could not find %s with id: %d";
    public EntityNotFoundException(Class<?> classname, String predicate, int id) {
        super(String.format(message, classname.getSimpleName(), predicate, id));
    }
    public EntityNotFoundException(Class<?> classname, int id) {
        super(String.format(messageWithId, classname.getSimpleName(), id));
    }
}
