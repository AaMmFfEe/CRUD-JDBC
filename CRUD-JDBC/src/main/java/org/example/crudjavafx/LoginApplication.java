package org.example.crudjavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 540, 360);
        primaryStage.setTitle("˗ˏˋ✩ˎˊ˗ Login ˗ˏˋ✩ˎˊ˗");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icono2.png")));
        primaryStage.setScene(scene);
        primaryStage.show();

        // controlador del login
        LoginController controller= fxmlLoader.getController();
        controller.setPrimaryStage(primaryStage);
    }

    public static void main(String[] args) {
        launch();
    }
}