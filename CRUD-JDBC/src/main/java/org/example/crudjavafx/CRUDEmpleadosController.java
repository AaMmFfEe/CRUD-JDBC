package org.example.crudjavafx;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.stage.*;

import java.sql.*;

public class CRUDEmpleadosController {

    @FXML
    private TextField idText;
    @FXML
    private TextField nombreText;
    @FXML
    private TextField puestoText;
    @FXML
    private TextField salarioText;

    @FXML
    private TableView<Empleado> tablaEmpleados;
    @FXML
    private TableColumn<Empleado, Integer> columnaId;
    @FXML
    private TableColumn<Empleado, String> columnaNombre;
    @FXML
    private TableColumn<Empleado, String> columnaPuesto;
    @FXML
    private TableColumn<Empleado, Double> columnaSalario;
    @FXML
    private CheckBox callCheckBox;

    private ObservableList<Empleado> listaObservable;
    private ManejadorEmpleadoDB manejadorEmpleadosDB;

    public void initData(String url, String user, String password) {
        manejadorEmpleadosDB = new ManejadorEmpleadoDB(url, user, password);
        // manejadorEmpleadosDB = new ManejadorEmpleadoDB("jdbc:mysql://localhost:3306/empresaDS3", "DS3","ds3");
        listaObservable = FXCollections.observableArrayList(manejadorEmpleadosDB.getEmpleadosPS());
        tablaEmpleados.setItems(listaObservable);

        tablaEmpleados.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                cargarSeleccionado(newSel);
            }
        });
        limpiarForm();
    }

    @FXML
    public void initialize() {
        // import javafx.scene.control.cell.PropertyValueFactory;
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaPuesto.setText("Puesto"); // encabezado
        columnaPuesto.setCellValueFactory(new PropertyValueFactory<>("puesto")); // atributo al que mapea
        columnaSalario.setText("Salario"); // encabezado
        columnaSalario.setCellValueFactory(new PropertyValueFactory<>("salario")); // atributo al que mapea

        //recargarDatos();
    }

    @FXML
    private void guardarEmpleado() {
        String nombre = nombreText.getText();
        String puesto = puestoText.getText();
        String salarioStr = salarioText.getText();

        if (nombre.isEmpty()) {
            mostrarAlerta("Debe ingresar un nombre ... (╥﹏╥)⚠", ".ᐟ.ᐟ ─── ☆ ─── ⚠ Error ⚠ ─── ☆  ─── .ᐟ.ᐟ", Alert.AlertType.ERROR);
            nombreText.requestFocus();
            return;
        } else if (puesto.isEmpty()) {
            mostrarAlerta("Debe ingresar un puesto ... (╥﹏╥)⚠", ".ᐟ.ᐟ ─── ☆ ─── ⚠ Error ⚠ ─── ☆  ─── .ᐟ.ᐟ", Alert.AlertType.ERROR);
            puestoText.requestFocus();
            return;
        }
        double salario;

        try {
            salario = Double.parseDouble(salarioStr);
        } catch (NumberFormatException e) {
            mostrarAlerta("El salario debe ser un número válido ... (╥﹏╥)⚠", ".ᐟ.ᐟ ─── ☆ ─── ⚠ Error ⚠ ─── ☆  ─── .ᐟ.ᐟ", Alert.AlertType.ERROR);
            salarioText.requestFocus();
            return;
        }

        int idGuardar = Integer.parseInt(idText.getText());

        int resultado;
        Empleado empleado = new Empleado(idGuardar, nombre, puesto, salario);
        if (idGuardar == 0) {
            if (callCheckBox.isSelected()) {
                //TODO mandar llamar el CallableStatement
                resultado = 0;
            } else {
                resultado = manejadorEmpleadosDB.insertarPS(empleado);
            }
            if (resultado > 0) { // el Insert afectó a 1 o más renglones
                mostrarAlerta("Empleado agregado correctamente.ᐟ.ᐟ ദ്ദി ˉ꒳ˉ )✧",
                        "Acción realizada correctamente", Alert.AlertType.INFORMATION);

            } else { // el insert no afectó ningún renglón
                mostrarAlerta("No se pudo agregar al empleado ... (╥﹏╥)⚠",
                        ".ᐟ.ᐟ ─── ☆ ─── ⚠ Error ⚠ ─── ☆  ─── .ᐟ.ᐟ", Alert.AlertType.ERROR);
            }

        } else { // significa que está actualizando a un empleado existente

            if (callCheckBox.isSelected()) {
                //TODO mandar llamar el CallableStatement
                resultado = 0;
            } else {
                resultado = manejadorEmpleadosDB.actualizarPS(empleado);
            }
            if (resultado > 0) { // el Update afectó a 1 o más renglones
                mostrarAlerta("Empleado agregado correctamente.ᐟ.ᐟ ദ്ദി ˉ꒳ˉ )✧",
                        "Acción realizada correctamente", Alert.AlertType.INFORMATION);

            } else { // el Update no afectó ningún renglón
                mostrarAlerta("No se pudo actualizar al empleado ... (╥﹏╥)⚠",
                        ".ᐟ.ᐟ ─── ☆ ─── ⚠ Error ⚠ ─── ☆  ─── .ᐟ.ᐟ", Alert.AlertType.ERROR);
            }
        }
        recargarDatos();
    }

    @FXML
    private void recargarDatos() {
        listaObservable.setAll(manejadorEmpleadosDB.getEmpleadosPS());
        limpiarForm();
    }

    private void limpiarForm() {
        // limpiar el texto de los TextField
        idText.setText(String.valueOf(0));
        nombreText.setText("");
        puestoText.setText("");
        salarioText.setText("");

        // deseleccionar el renglón seleccionado en la tabla
        tablaEmpleados.getSelectionModel().clearSelection();

    }

    private void cargarSeleccionado(Empleado empleado) {
        idText.setText(String.valueOf(empleado.getId()));
        nombreText.setText(empleado.getNombre());
        puestoText.setText(empleado.getPuesto());
        salarioText.setText(String.valueOf(empleado.getSalario()));

    }

    private void mostrarAlerta(String mensaje, String titulo, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        // obtener la ventana principal para centrar la alerta respecto a la ventana
        Stage stage = (Stage) tablaEmpleados.getScene().getWindow();
        if (stage != null) {
            alerta.initOwner(stage);
        }

        //alerta.show();
        alerta.showAndWait();
    }

    @FXML
    private void filtrarEmpleados(){
        double salario = 0;
        String nombre = nombreText.getText();
        String puesto = puestoText.getText();

        if ( ! salarioText.getText().isEmpty()){
            salario = Double.parseDouble(salarioText.getText());

        }

        if (callCheckBox.isSelected()){
            listaObservable.setAll(manejadorEmpleadosDB.getEmpleadosPorFiltroCS(nombre, puesto, salario));
        }

        else {
            listaObservable.setAll(manejadorEmpleadosDB.getEmpleadosPorFiltroPS(nombre, puesto, salario));
        }

        if (listaObservable.isEmpty()){
            mostrarAlerta("No se encontraron empleados con los filtros proporcionados.", "Aviso", Alert.AlertType.INFORMATION);

            recargarDatos();
        }
    }

    @FXML
    private void eliminarEmpleado(){
        int resultado;
        Empleado elim = tablaEmpleados.getSelectionModel().getSelectedItem();
        if (callCheckBox.isSelected()){
            // TODO mandar llaman el CallableStatement
            elim = null;
        }

        else{
            if (callCheckBox.isSelected()) {
                //TODO mandar llamar el CallableStatement
                resultado = 0;
            } else {
                resultado = manejadorEmpleadosDB.eliminarPS(elim);
            }
            if (resultado > 0) { // el Delete afectó a 1 o más renglones
                mostrarAlerta("Empleado eliminado correctamente.ᐟ.ᐟ ദ്ദി ˉ꒳ˉ )✧",
                        "Acción realizada correctamente", Alert.AlertType.INFORMATION);

            } else { // el Delete no afectó ningún renglón
                mostrarAlerta("No se pudo eliminar al empleado ... (╥﹏╥)⚠",
                        ".ᐟ.ᐟ ─── ☆ ─── ⚠ Error ⚠ ─── ☆  ─── .ᐟ.ᐟ", Alert.AlertType.ERROR);
            }
        }
        recargarDatos();
    }

    //5.3

    @FXML
    private void probarConexion() {

        Connection conn = manejadorEmpleadosDB.abrirConexion();
        if (conn != null) {
            mostrarAlerta("Conexión exitosa.ᐟ.ᐟ ദ്ദി ˉ꒳ˉ )✧", "ᐟ.ᐟ ─── ☆ ─── Éxito ─── ☆  ─── .ᐟ.ᐟ", Alert.AlertType.INFORMATION);
            manejadorEmpleadosDB.cerrarConexion(conn);
            mostrarAlerta("Se cerró la conexión.ᐟ.ᐟ ദ്ദി ˉ꒳ˉ )✧", "ᐟ.ᐟ ─── ☆ ─── Conexión cerrada ─── ☆  ─── .ᐟ.ᐟ", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("No se pudo conectar a la base de datos ... (╥﹏╥)⚠", "ᐟ.ᐟ ─── ☆ ─── ⚠ Error de conexión ⚠ ─── ☆  ─── .ᐟ.ᐟ", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void contarEmpleados(){
        String puesto = "Dev";
        int total = manejadorEmpleadosDB.contarEmpleadosPorPuestoCS(puesto);
        System.out.println("Total de empleados en el puesto "+puesto+": "+total);
    }

    @FXML
    private void aumentarPuesto(){
        //concatenar
        String puesto = "Dev";
        String nuevo = manejadorEmpleadosDB.aumentarPuestoCS(puesto, "Senior");
        System.out.println("Nuevo puesto: " + nuevo);
    }

    @FXML
    private void contarComboBox() {
        // Crear alerta para seleccionar el puesto
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Contar empleados por puesto");
        alerta.setHeaderText("Selecciona el puesto que deseas contar:");

        // Crear ComboBox con los puestos disponibles desde la BD
        ComboBox<String> comboPuestos = new ComboBox<>();
        comboPuestos.getItems().addAll(manejadorEmpleadosDB.obtenerPuestos());
        comboPuestos.setPromptText("Selecciona un puesto");

        alerta.getDialogPane().setContent(comboPuestos);

        // Centrar la alerta respecto a la ventana principal
        Stage stage = (Stage) tablaEmpleados.getScene().getWindow();
        if (stage != null) {
            alerta.initOwner(stage);
        }

        // Mostrar alerta y esperar respuesta del usuario
        alerta.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                String puestoSeleccionado = comboPuestos.getValue();

                if (puestoSeleccionado == null || puestoSeleccionado.isEmpty()) {
                    mostrarAlerta("Debes seleccionar un puesto.", "Advertencia", Alert.AlertType.WARNING);
                    return;
                }

                // Usar el procedimiento almacenado con CallableStatement
                int total = manejadorEmpleadosDB.contarEmpleadosPorPuestoCS(puestoSeleccionado);

                mostrarAlerta("Hay " + total + " empleados con el puesto '" + puestoSeleccionado + "'.",
                        "Resultado del conteo", Alert.AlertType.INFORMATION);
            }
        });
    }

}


