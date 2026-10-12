package org.example.crudjavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class CRUDEmpleadosApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CRUDEmpleadosApp.class.getResource("CRUDEmpleados-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        primaryStage.setTitle("˚₊‧꒰ა ☆ ໒꒱ ‧₊˚ Aplicación CRUDEmpleados JavaFX ˚₊‧꒰ა ☆ ໒꒱ ‧₊˚");
        // stage.centerOnScreen(); para centrar la pestaña manualmente
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icono.png")));
        // (*/icono.png*) // según la versión depende si lleva el / o no
        // import javafx.scene.image.Image;
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
