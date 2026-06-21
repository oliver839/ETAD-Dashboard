package com.olivermandic.etad.model;

public class TemperatureSensor implements Sensor{

    private double currentTemperature = 20.0; // Startwert für die Temperatur

    @Override
    public double readValue() {
        //ausgeben des aktuellen Wertes mit einer langsamen Schwankung, um die Temperaturänderung zu simulieren
        double change = (Math.random() - 0.5) * 2.0; // Wert zwischen -1.0 und +1.0
        currentTemperature += change;
        return Math.round(currentTemperature * 10.0) / 10.0;
    }

    @Override
    public String getUnit() {
        return "°C";
    }

    @Override
    public String getName() {
        return "ECU Core Temperature";
    }
}
