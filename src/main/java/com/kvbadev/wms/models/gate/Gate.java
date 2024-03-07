package com.kvbadev.wms.models.gate;

import jakarta.persistence.*;

@Entity
@Table(name = "gates")
public class Gate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    private String name;
    @Transient
    private State state;

    @Transient
    private GateManager gateManager;

    void open() throws GateAccessException {
        try {
            gateManager.unlockGate();
        } catch (Exception e) {
            throw new GateAccessException("Only Staff can manage the gate.");
        }
    };
    void close() throws GateAccessException, GateConnectionException {

    };
}
