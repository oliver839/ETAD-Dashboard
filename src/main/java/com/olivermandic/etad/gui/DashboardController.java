package com.olivermandic.etad.gui;

import com.olivermandic.etad.controller.TelemetryMonitor;
import com.olivermandic.etad.model.TemperatureSensor;
import com.olivermandic.etad.model.VoltageSensor;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class DashboardController {

    // --- Verknüpfungen zur FXML ---
    @FXML private TextField txtVoltageLimit;
    @FXML private TextField txtTempLimit;
    @FXML private Label lblVoltage;
    @FXML private Label lblTemp;
    @FXML private Label lblStatus;
    @FXML private Button btnStart;
    @FXML private Button btnStop;
    @FXML private LineChart<Number, Number> voltageChart;
    @FXML private NumberAxis xAxis;

    // --- Backend Logik ---
    private TelemetryMonitor monitor;
    private VoltageSensor voltageSensor;
    private TemperatureSensor tempSensor;

    // --- Für den Graphen ---
    private XYChart.Series<Number, Number> series;
    private double timeSeconds = 0;
    private AnimationTimer timer;
    private long lastUpdate = 0;

    @FXML
    public void initialize() {
        // 1. Backend initialisieren
        monitor = new TelemetryMonitor();
        voltageSensor = new VoltageSensor();
        tempSensor = new TemperatureSensor();

        monitor.addSensor(voltageSensor);
        monitor.addSensor(tempSensor);

        // 2. Graphen einrichten
        series = new XYChart.Series<>();
        series.setName("Live Voltage");
        voltageChart.getData().add(series);

        // 3. Den "Herzschlag" der App einrichten (aktualisiert alle 100ms)
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Nur ca. alle 100ms ein Update machen
                if (now - lastUpdate >= 100_000_000) {
                    updateTelemetry();
                    lastUpdate = now;
                }
            }
        };
    }

    @FXML
    private void onStartLogging() {
        monitor.startLogging();
        timer.start();
        btnStart.setDisable(true);
        btnStop.setDisable(false);
        lblStatus.setText("LOGGING ACTIVE");
        lblStatus.setTextFill(Color.BLUE);
    }

    @FXML
    private void onStopLogging() {
        monitor.stopLogging();
        timer.stop();
        btnStart.setDisable(false);
        btnStop.setDisable(true);
        lblStatus.setText("SYSTEM STOPPED");
        lblStatus.setTextFill(Color.GRAY);
    }

    @FXML
    private void onUpdateLimits() {
        try {
            double vLimit = Double.parseDouble(txtVoltageLimit.getText());
            double tLimit = Double.parseDouble(txtTempLimit.getText());
            monitor.setVoltageLimit(vLimit);
            monitor.setTemperatureLimit(tLimit);
            System.out.println("Limits geupdatet: V=" + vLimit + ", T=" + tLimit);
        } catch (NumberFormatException e) {
            lblStatus.setText("ERROR: Bitte nur Zahlen als Limit eingeben!");
            lblStatus.setTextFill(Color.RED);
        }
    }

    private void updateTelemetry() {
        // Werte auslesen
        double currentV = voltageSensor.readValue();
        double currentT = tempSensor.readValue();

        // An die GUI schicken
        lblVoltage.setText(currentV + " V");
        lblTemp.setText(currentT + " °C");

        // Graph updaten
        timeSeconds += 0.1;
        series.getData().add(new XYChart.Data<>(timeSeconds, currentV));

        // Graph wandern lassen, wenn er über 30 Sekunden hinausgeht
        if (timeSeconds > 30) {
            series.getData().remove(0);
            xAxis.setLowerBound(timeSeconds - 30);
            xAxis.setUpperBound(timeSeconds);
        }

        // --- HIER KOMMT DEIN GETESTETER LOGIK-CONTROLLER INS SPIEL ---
        monitor.checkLimits(currentV, currentT);

        if (monitor.isAlarmActive()) {
            lblStatus.setText("ALARM: LIMIT EXCEEDED!");
            lblStatus.setTextFill(Color.RED);
            // Optional: Fieser roter Hintergrund für die Labels
            lblVoltage.setStyle("-fx-text-fill: red;");
        } else {
            lblStatus.setText("LOGGING ACTIVE - OK");
            lblStatus.setTextFill(Color.GREEN);
            lblVoltage.setStyle("-fx-text-fill: black;");
        }
    }
}