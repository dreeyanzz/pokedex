module com.example {
    // JavaFX dependencies
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.media; // Now supported by the pom.xml change
    requires javafx.fxml; // Included to match your pom.xml

    // Jackson dependencies for JSON parsing
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;

    // Export packages
    exports com.example;
    exports com.example.controller;
    exports com.example.view;
    exports com.example.model;

    // Open packages for reflection (required for Jackson)
    opens com.example.model to com.fasterxml.jackson.databind;
}