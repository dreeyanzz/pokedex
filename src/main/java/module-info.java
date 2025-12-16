module com.example {
    requires javafx.controls;
    requires javafx.fxml;

    // ADD THESE LINES:
    requires com.fasterxml.jackson.databind;

    opens com.example to javafx.fxml;

    // ALLOW JACKSON TO SEE YOUR CODE (for JSON parsing):
    exports com.example;
}