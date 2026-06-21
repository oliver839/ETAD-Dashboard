module com.olivermandic.etad {
    requires javafx.controls;
    requires javafx.fxml;

    // Keine Test-Requires hier eintragen!

    // Erlaubt JavaFX den Zugriff auf die Hauptklasse
    opens com.olivermandic.etad to javafx.fxml;
    exports com.olivermandic.etad;

    // Erlaubt JavaFX den Zugriff auf unseren Controller und die FXML-Datei
    opens com.olivermandic.etad.gui to javafx.fxml;
    exports com.olivermandic.etad.gui;
}