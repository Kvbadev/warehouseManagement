package com.kvbadev.wms.models.gate;

public class GateAccessException extends Exception{
    public GateAccessException(String errMessage){
        super(errMessage);
    }
}
