module com.example {
    // JavaFX dependencies
    requires javafx.controls;
    requires javafx.graphics;

    // Jackson dependencies for JSON parsing
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;

    // Export packages (makes them accessible to other modules)
    exports com.example;
    exports com.example.controller;
    exports com.example.view;
    exports com.example.model;

    // Open packages for reflection (required for Jackson to deserialize)
    opens com.example.model to com.fasterxml.jackson.databind;
}