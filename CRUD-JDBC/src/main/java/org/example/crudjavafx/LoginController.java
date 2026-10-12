package org.example.crudjavafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class LoginController {

    @FXML private TextField urlTextField;
    @FXML private TextField userTextField;
    @FXML private TextField passwTextField;

    private Stage primaryStage;
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    protected void ingresar() {
        try {

            ManejadorEmpleadoDB test = new ManejadorEmpleadoDB
                    (urlTextField.getText(), userTextField.getText(), passwTextField.getText());

            if(!test.probarConexion()) {
                // no hubo conexión
                mostrarAlerta("No se pudo conectar a la base de datos ... (╥﹏╥)⚠ " +
                        "\n Verifica la red o los datos de conexión ", ".ᐟ.ᐟ ─── ☆ ─── ⚠ Error de conexión ⚠ ─── ☆  ─── .ᐟ.ᐟ", Alert.AlertType.ERROR);
                return; // termina el método y no inicializa la ventana
            }

            FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("CRUDEmpleados-view.fxml"));
            Scene mainScene = new Scene(fxmlLoader.load(), 600, 600);

            CRUDEmpleadosController mainController = fxmlLoader.getController();
            mainController.initData("jdbc:mysql://localhost:3306/empresaDS3", "DS3","ds3");

            // crear subventana
            Stage mainStage = new Stage();
            mainStage.getIcons().add(new Image(getClass().getResourceAsStream("icono.png")));
            mainStage.setTitle("˚₊‧꒰ა ☆ ໒꒱ ‧₊˚ Aplicación CRUD Empleados JavaFX con JDBC ˚₊‧꒰ა ☆ ໒꒱ ‧₊˚");
            mainStage.setScene(mainScene);
            mainStage.setOnCloseRequest((WindowEvent_event) -> {

                primaryStage.show();

            });

            mainStage.setX(primaryStage.getX());
            mainStage.setY(primaryStage.getY());

            //mainStage.initModality(Modality.WINDOW_MODAL);
            //mainStage.initOwner(tablaEmpleados.getScene().getWindow());

            mainStage.show();
            primaryStage.hide(); // oculta el login

        } catch (IOException e) {
            e.printStackTrace();
            // mostrarAlerta("No se pudo abrir la subventana... (╥﹏╥)⚠", "ᐟ.ᐟ ─── ☆ ─── ⚠ Error ⚠ ─── ☆  ─── .ᐟ.ᐟ", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String mensaje, String titulo, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        // obtener la ventana principal para centrar la alerta respecto a la ventana
        Stage stage = (Stage) primaryStage;
        if (stage != null) {
            alerta.initOwner(stage);
        }

        //alerta.show();
        alerta.showAndWait();
    }

}