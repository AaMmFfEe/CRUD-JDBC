package org.example.crudjavafx;

import java.io.*;
import java.util.ArrayList;

public class ManejadorArrayListEmpleado {
    //Clase para administrar funciones CRUD en un arreglo

    private ArrayList<Empleado> empleados;
    private int siguienteId; // contador global de IDs

    private final String ARCHIVO_CSV = "empleados.csv"; // archivo default

    public int getSiguienteId() {
        return siguienteId;
    }

    public ManejadorArrayListEmpleado() {

        empleados = new ArrayList<>();
        siguienteId = 1;
        cargarCSV(); // carga automática al iniciar

    }

    //metodo para insertar un valor en el arreglo
    public void insertar(String nombre, String puesto, double salario) {

        empleados.add(new Empleado(siguienteId, nombre, puesto, salario));
        siguienteId++;
        System.out.println("Empleado agregado.");
        guardarCSV();
    }

    //metodo para leer los valores en el arrayList
    public ArrayList<Empleado> getEmpleados() {
        return empleados;

    }

    // metodo para actualizar un valor en el arrayList
    public void actualizar(int id, String nuevoNombre, String nuevoPuesto, double nuevoSalario) {

        Empleado empleado = buscarPorId(id);
        if (empleado != null) {
            empleado.setNombre(nuevoNombre);
            empleado.setPuesto(nuevoPuesto);
            empleado.setSalario(nuevoSalario);

            System.out.println("Empleado actualizado");
            guardarCSV();

        } else {
            System.out.println("ID no encontrada");
        }

    }

    // metodo para borrar un valor en el arrayList
    public void eliminar(int id) {
        Empleado empleado = buscarPorId(id);
        if (empleado != null) {
            empleados.remove(empleado);
            System.out.println("Empleado eliminado");
            guardarCSV();

        } else {
            System.out.println("ID no encontrada");
        };
    }

    // metodo para buscar empleado por Id
    public Empleado buscarPorId(int id) {
        for (Empleado empleado : empleados) {
            if (empleado.getId() == id) {
                return empleado;
            }
        }

        // en caso de no encontrar el Id indicado, devuelve null
        return null;
    }

    // metodo para saber si el arrayList está vacio
    public boolean estaVacio() {
        return empleados.isEmpty();
    }

    // Persistencia de datos en archivo CSV
    private void guardarCSV() {

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(ARCHIVO_CSV));
            writer.println("id, nombre, puesto, salario");
            for (Empleado empleado : empleados) {
                writer.println(
                        empleado.getId() + "," +
                        empleado.getNombre() + "," +
                        empleado.getPuesto() + "," +
                        empleado.getSalario()
                );
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private void cargarCSV() {
        File archivo = new File(ARCHIVO_CSV);
        if (!archivo.exists()) return;

        empleados.clear();
        siguienteId = 1;

        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_CSV))){

            String linea;
            boolean primera = true;
            while ((linea = br.readLine()) != null){
                if (primera) {
                    primera = false;
                    continue;
                }
                String[] partes = linea.split(",");
                int id = Integer.parseInt(partes[0]);
                String nombre = partes[1];
                String puesto = partes[2];
                double salario = Double.parseDouble(partes[3]);
                empleados.add(new Empleado(id, nombre, puesto, salario));
                if (id >= siguienteId){
                    siguienteId = id+1;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
