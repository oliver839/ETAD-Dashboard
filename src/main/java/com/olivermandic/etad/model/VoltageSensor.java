package com.olivermandic.etad.model;

public class VoltageSensor implements Sensor {
    // Grundspannung bleibt als Konstante/Feld
    double baseVoltage = 12.0;

    @Override
    public double readValue() {
        // Das Rauschen muss bei JEDEM Ablesen neu berechnet werden!
        double noise = Math.random() * 2.5;
        // gibt den wert mit rausch zurück, auf 2 nachkommastellen gerundet
        return Math.round((baseVoltage + noise) * 100.0) / 100.0;
    }

    @Override
    public String getUnit() {
        return "V";
    }

    @Override
    public String getName() {
        return "Main battery Voltage";
    }
}
