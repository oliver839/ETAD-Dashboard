package com.olivermandic.etad.controller;

import com.olivermandic.etad.model.Sensor;
import java.util.ArrayList;
import java.util.List;

/**
 * Der TelemetryMonitor überwacht die Sensoren und prüft auf Grenzwertüberschreitungen.
 */
public class TelemetryMonitor {

    private List<Sensor> sensors;
    private boolean isLogging;
    private double voltageLimit;
    private double temperatureLimit;
    private boolean alarmActive;

    public TelemetryMonitor() {
        this.sensors = new ArrayList<>();
        this.isLogging = false;
        this.alarmActive = false;

        // Standard-Limits (können später über die GUI geändert werden)
        this.voltageLimit = 14.5;
        this.temperatureLimit = 85.0;
    }

    public void addSensor(Sensor sensor) {
        sensors.add(sensor);
    }

    public void startLogging() {
        this.isLogging = true;
        this.alarmActive = false; // Alarm beim Starten zurücksetzen
        System.out.println("Telemetry logging started...");
    }

    public void stopLogging() {
        this.isLogging = false;
        System.out.println("Telemetry logging stopped.");
    }

    /**
     * Diese Methode wird später von der GUI (oder einem Timer) immer wieder aufgerufen,
     * um die neuesten Werte zu holen und zu prüfen.
     */
    public void checkLimits(double currentVoltage, double currentTemperature) {
        if (!isLogging) return; // Wenn wir nicht messen, prüfen wir auch nicht

        if (currentVoltage > voltageLimit || currentTemperature > temperatureLimit) {
            triggerAlarm();
        } else {
            // Wenn die Werte wieder normal sind, Alarm automatisch ausschalten
            alarmActive = false;
        }
    }

    private void triggerAlarm() {
        this.alarmActive = true;
        System.err.println("ALARM: Limit exceeded!");
    }

    // --- Getter und Setter ---

    public boolean isLogging() {
        return isLogging;
    }

    public boolean isAlarmActive() {
        return alarmActive;
    }

    public void setVoltageLimit(double voltageLimit) {
        this.voltageLimit = voltageLimit;
    }

    public void setTemperatureLimit(double temperatureLimit) {
        this.temperatureLimit = temperatureLimit;
    }
}
