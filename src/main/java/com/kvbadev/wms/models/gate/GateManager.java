package com.kvbadev.wms.models.gate;

public interface GateManager {
    void connect(String name) throws GateAccessException, GateConnectionException;
    void unlockGate() throws GateAccessException, GateConnectionException;
    void lockGate() throws GateAccessException, GateConnectionException;
}
