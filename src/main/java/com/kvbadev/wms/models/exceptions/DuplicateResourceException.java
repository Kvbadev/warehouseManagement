package com.kvbadev.wms.models.exceptions;

import com.kvbadev.wms.presentation.controllers.BaseController;

public class DuplicateResourceException extends BaseException {
    private static final String message = "%s with %s: %s already exists";
    public DuplicateResourceException(Class<?> classname, String predicate, String identifier) {
        super(String.format(message, classname.getSimpleName(), predicate, identifier));
    }
}
