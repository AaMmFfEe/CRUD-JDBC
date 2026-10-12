module org.example.crudjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;


    opens org.example.crudjavafx to javafx.fxml;
    exports org.example.crudjavafx;
}