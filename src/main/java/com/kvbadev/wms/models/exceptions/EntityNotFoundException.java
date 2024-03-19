package com.kvbadev.wms.models.exceptions;

public class EntityNotFoundException extends BaseException {
    private static final String message = "Could not find %s with id: %d";
    public EntityNotFoundException(Class<?> classname, int id) {
        super(String.format(message, classname.getSimpleName(), id));
    }
    public EntityNotFoundException(Throwable throwable, Class<?> classname, int id) {
        super(String.format(message, classname.getSimpleName(), id), throwable);
    }
}
