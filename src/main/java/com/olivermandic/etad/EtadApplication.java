package com.olivermandic.etad;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EtadApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Lädt die FXML Datei, die wir gerade erstellt haben
        FXMLLoader fxmlLoader = new FXMLLoader(EtadApplication.class.getResource("dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);

        stage.setTitle("ETAD - ECU Telemetry & Test Automation Dashboard");
        stage.setScene(scene);
        stage.setResizable(false); // Fenstergröße fixieren
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}