module hellofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires java.dotenv;

    opens com.example to javafx.fxml;
    exports com.example;
}