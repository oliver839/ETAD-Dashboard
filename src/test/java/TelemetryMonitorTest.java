package com.olivermandic.etad.controller;

import com.olivermandic.etad.model.Sensor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TelemetryMonitorTest {

    private TelemetryMonitor monitor;
    private Sensor mockVoltageSensor;
    private Sensor mockTempSensor;

    @BeforeEach
    void setUp() {
        monitor = new TelemetryMonitor();
        mockVoltageSensor = org.mockito.Mockito.mock(Sensor.class);
        mockTempSensor = org.mockito.Mockito.mock(Sensor.class);

        monitor.addSensor(mockVoltageSensor);
        monitor.addSensor(mockTempSensor);
    }

    @Test
    void testOvervoltageTriggersAlarm() {
        monitor.setVoltageLimit(14.5);
        monitor.startLogging();

        org.mockito.Mockito.when(mockVoltageSensor.readValue()).thenReturn(15.0);
        org.mockito.Mockito.when(mockTempSensor.readValue()).thenReturn(40.0);

        monitor.checkLimits(mockVoltageSensor.readValue(), mockTempSensor.readValue());

        assertTrue(monitor.isAlarmActive(), "Der Alarm hätte auslösen müssen, da 15.0V > 14.5V ist!");
    }

    @Test
    void testNormalValuesDoNotTriggerAlarm() {
        monitor.setVoltageLimit(14.5);
        monitor.startLogging();

        org.mockito.Mockito.when(mockVoltageSensor.readValue()).thenReturn(12.5);
        org.mockito.Mockito.when(mockTempSensor.readValue()).thenReturn(40.0);

        monitor.checkLimits(mockVoltageSensor.readValue(), mockTempSensor.readValue());

        assertFalse(monitor.isAlarmActive(), "Bei normalen Werten darf kein Alarm ausgelöst werden!");
    }
}
