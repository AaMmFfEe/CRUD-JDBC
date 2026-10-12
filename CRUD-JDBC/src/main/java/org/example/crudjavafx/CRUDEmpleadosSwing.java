package org.example.crudjavafx;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CRUDEmpleadosSwing {

    private JPanel panelPrincipal;
    private JTable tablaEmpleados;
    private JTextField idText;
    private JTextField nombreText;
    private JTextField puestoText;
    private JTextField salarioText;
    private JButton guardarButton;
    private JButton eliminarButton;
    private JButton limpiarButton;
    private DefaultTableModel tablaModelo;
    private JFrame frame;

    private ManejadorArrayListEmpleado manejadorEmpleados;

    public CRUDEmpleadosSwing() {

        manejadorEmpleados = new ManejadorArrayListEmpleado();

        guardarButton.addActionListener(e -> guardarEmpleado());

        eliminarButton.addActionListener(e -> eliminarEmpleado());

        limpiarButton.addActionListener(e -> limpiarForm());
        tablaEmpleados.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarSeleccionado();
            }
        });
        recargarDatos();


        // listener que toma 2 acciones al presionar ENTER en el botón guardar:
            // guarda un empleado nuevo y devuelve el focus al nombre del empleado otra vez
        /* guardarButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    guardarEmpleado();
                    nombreText.requestFocus();
                    super.keyPressed(e);
                }
            }

        }); */

        nombreText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)  {
                    puestoText.requestFocus();
                    super.keyPressed(e);
                }
            }
        });
        puestoText.addActionListener(e -> salarioText.requestFocus()
        );
        salarioText.addActionListener(e -> guardarEmpleado()
        );
    }

    private void cargarSeleccionado() {
        var renglon = tablaEmpleados.getSelectedRow();
        if (renglon != -1) {
            // -1 significa que no se seleccionó ningún renglón
            var id = tablaEmpleados.getModel().getValueAt(renglon, 0).toString();
            var nombre = tablaEmpleados.getModel().getValueAt(renglon, 1).toString();
            var puesto = tablaEmpleados.getModel().getValueAt(renglon, 2).toString();
            var salario = tablaEmpleados.getModel().getValueAt(renglon, 3).toString();
            idText.setText(id);
            nombreText.setText(nombre);
            puestoText.setText(puesto);
            salarioText.setText(salario);
        }
    }

    private void limpiarForm() {
        // limpiar el texto de los TextField
        idText.setText(String.valueOf(manejadorEmpleados.getSiguienteId()));
        nombreText.setText("");
        puestoText.setText("");
        salarioText.setText("");

        // Deseleccionar el renglón seleccionado en la tabla
        tablaEmpleados.getSelectionModel().clearSelection();


    }

    private void guardarEmpleado() {
        String nombre = nombreText.getText();
        String puesto = puestoText.getText();
        String salarioStr = salarioText.getText();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Debe ingresar un nombre.", "Error", JOptionPane.ERROR_MESSAGE);
            nombreText.requestFocus();
            return;
        } else if (puesto.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Debe ingresar un puesto.", "Error", JOptionPane.ERROR_MESSAGE);
            puestoText.requestFocus();
            return;
        }
        double salario;

        try {
            salario = Double.parseDouble(salarioStr);
        }catch (NumberFormatException e){
            JOptionPane.showMessageDialog(frame, "El salario debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            salarioText.requestFocus();
            return;
        }

        int idGuardar = Integer.parseInt(idText.getText());
        if (idGuardar == manejadorEmpleados.getSiguienteId()) {
            manejadorEmpleados.insertar (nombre, puesto, salario);

        }

        else { // significa que se está actualizando un empleado existente
            manejadorEmpleados.actualizar(idGuardar, nombre, puesto, salario);
        }
        recargarDatos();
    }

    private void recargarTabla() {
        tablaModelo.setRowCount(0);
        for (Empleado empleado : manejadorEmpleados.getEmpleados()) {
            Object[] fila = {
                    empleado.getId(),
                    empleado.getNombre(),
                    empleado.getPuesto(),
                    empleado.getSalario()};

            tablaModelo.addRow(fila);
        }
    }

    private void recargarDatos() {
        recargarTabla();
        limpiarForm();
    }

    private void eliminarEmpleado() {

        int idEliminar = Integer.parseInt(idText.getText());
        if (idEliminar != manejadorEmpleados.getSiguienteId()) {
            manejadorEmpleados.eliminar(idEliminar);
            recargarDatos();

        } else {
            JOptionPane.showMessageDialog(frame, "Seleccione un empleado para eliminar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void mostrar() {

        frame = new JFrame("⋆˙⟡₍^. .^₎⟆ Aplicación Empleados CRUD ₍^. .^₎⟆⋆˙⟡"); //crear la ventana con un título
        frame.setContentPane(panelPrincipal); // se agrega el panelPrincipal a la ventana
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.pack(); ajusta la ventana al tamaño de los componentes
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null); // centra la ventana
        frame.setVisible(true);
        ImageIcon Icono = new ImageIcon(CRUDEmpleadosSwing.class.getResource("/icono.png"));
        frame.setIconImage(Icono.getImage());

    }

    public static void main(String[] args) {

        new CRUDEmpleadosSwing().mostrar();
    }

    private void createUIComponents() {
        this.tablaModelo = new DefaultTableModel(0, 4) {

            @Override // evitar la edición directa en la tabla
            public boolean isCellEditable(int row, int column) {
                return false; // bloquea la edición

            }

        };
        String[] encabezados = {"ID", "Nombre", "Puesto", "Salario"};
        this.tablaModelo.setColumnIdentifiers(encabezados);
        this.tablaEmpleados = new JTable(tablaModelo);
        this.tablaEmpleados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

       /* Formato para agregar datos manualmente a la tabla
        String[] datos = {"1", "Félix", "Estudiante", "0"};
        tablaModelo.addRow(datos);
        tablaModelo.addRow(datos);
        */
    }
}
