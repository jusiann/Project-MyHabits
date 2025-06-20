module com.adil.ProjectMyHabits {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires javafx.graphics;

    opens com.adil.ProjectMyHabits to javafx.fxml, com.google.gson;
    exports com.adil.ProjectMyHabits;
}
