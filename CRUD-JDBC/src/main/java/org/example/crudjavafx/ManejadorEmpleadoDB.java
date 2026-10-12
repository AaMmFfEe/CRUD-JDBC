package org.example.crudjavafx;

import java.sql.*;
import java.util.ArrayList;

public class ManejadorEmpleadoDB {

    private String url;
    // jdbc:mysql://10.10.248.11:3306/empresaDS3
    private String user;
    private String password;

    public ManejadorEmpleadoDB(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    // métodos para realizar la conexión a la base de datos
    public Connection abrirConexion() {
        /* try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } */
        try {

            DriverManager.setLoginTimeout(10); // máximo de 10 segundos para una conexión local
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void cerrarConexion(Connection conn) {
        try {
            if (conn != null) { // significa que hay una conexión
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean probarConexion() {

        Connection conn = abrirConexion();
        if (conn != null) {
            cerrarConexion(conn);
            return true;
        } else {
            return false;
        }
    }
    // métodos CRUD

    // C - Create (INSERT)

    public int insertar(Empleado empleado) {
        String query = "INSERT INTO empleados (nombre, puesto, salario) VALUES ('" + empleado.getNombre() + "', '" + empleado.getPuesto() + "', " + empleado.getSalario() + ")";

        try (Connection conn = abrirConexion();
             Statement stmt = conn.createStatement()) {
            return stmt.executeUpdate(query); // devuelve el num de registros agregados en la base de datos

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

    }

    // C - Create (INSERT) - PreparedStatement

    public int insertarPS(Empleado empleado) {
        String query = "INSERT INTO empleados (nombre, puesto, salario) VALUES (?, ?, ?)";

        try (Connection conn = abrirConexion();
             //Statement stmt = conn.createStatement()
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, empleado.getNombre());
            ps.setString(2, empleado.getPuesto());
            ps.setDouble(3, empleado.getSalario());
            return ps.executeUpdate(); // devuelve el num de registros agregados en la base de datos

        } catch (
                SQLException e) {
            e.printStackTrace();
            return 0;
        }

    }

    public int insertarCS( Empleado empleado){

        String call = "{CALL insertar_empleado(?,?,?)}";

        try(Connection conn = abrirConexion();
            //Statement stmt = conn.createStatement())
            //PreparedStatement ps = conn.prepareStatement(query)){
            CallableStatement cs = conn.prepareCall(call)){
            //return stmt.executeUpdate(query); // devuelve el número de registros agregados en la base de datos

            cs.setString(1, empleado.getNombre());
            cs.setString(2, empleado.getPuesto());
            cs.setDouble(3, empleado.getSalario());
            System.out.println(call);
            return cs.executeUpdate();

        } catch(SQLException e){
            e.printStackTrace();
            return 0;
        }

    }

// R - Read (SELECT)

    public ArrayList<Empleado> getEmpleados() {
        ArrayList<Empleado> empleados = new ArrayList<>();
        String query = "SELECT * FROM empleados";

        try (Connection conn = abrirConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) { // recorrer cada línea del ResultSet
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String puesto = rs.getString("puesto");
                double salario = rs.getInt("salario");
                empleados.add(new Empleado(id, nombre, puesto, salario));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return empleados;
    }

// R - Read (SELECT) - PreparedStatement

    public ArrayList<Empleado> getEmpleadosPS() {
        ArrayList<Empleado> empleados = new ArrayList<>();
        String query = "SELECT * FROM empleados";

        try (Connection conn = abrirConexion();
             PreparedStatement ps = conn.prepareStatement(query);
             // Statement stmt = conn.createStatement();
             // ResultSet rs = stmt.executeQuery(query)) {
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) { // recorrer cada línea del ResultSet
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String puesto = rs.getString("puesto");
                double salario = rs.getInt("salario");
                empleados.add(new Empleado(id, nombre, puesto, salario));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return empleados;
    }

    // R - Read (Select) - CallableStatement
    public ArrayList<Empleado> getEmpleadosPorFiltroCS(String nombreFiltro, String puestoFiltro, double salarioFiltro) {
        ArrayList<Empleado> empleados = new ArrayList<>();
        String call = "{CALL obtener_empleados_filtro(?, ?, ?)}";

        try (Connection conn = abrirConexion();
             CallableStatement cs = conn.prepareCall(call)) {

            if (nombreFiltro != null)
                cs.setString(1, nombreFiltro);
            else
                cs.setNull(1, Types.VARCHAR);

            if (puestoFiltro != null)
                cs.setString(2, puestoFiltro);
            else
                cs.setNull(2, Types.VARCHAR);

            cs.setDouble(3, salarioFiltro);

            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                empleados.add(new Empleado(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("puesto"),
                        rs.getDouble("salario")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return empleados;
    }

    // U - Update (UPDATE)
    public int actualizar(Empleado empleado) {
        String query = "UPDATE empleados SET " +
                "nombre='" + empleado.getNombre() + "'," +
                "puesto='" + empleado.getPuesto() + "'," +
                "salario=" + empleado.getSalario() + " " +
                "WHERE id=" + empleado.getId();

        try (Connection conn = abrirConexion();
             Statement stmt = conn.createStatement()) {
            return stmt.executeUpdate(query); // devuelve el num de registros agregados en la base de datos

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

    }

    // U - Update (UPDATE) - PreparedStatement
    public int actualizarPS(Empleado empleado) {

        String query = "UPDATE empleados SET " +
                "nombre= ? ," +
                "puesto= ? ," +
                "salario= ? " +
                "WHERE id = ? ";

        try (Connection conn = abrirConexion();
             // Statement stmt = conn.createStatement())
             PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1, empleado.getNombre());
            ps.setString(2, empleado.getPuesto());
            ps.setDouble(3, empleado.getSalario());
            ps.setInt(4, empleado.getId());
            return ps.executeUpdate(); // devuelve el número de registros agregados en la base de datos

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // D - Delete (DELETE)
    public int eliminar(Empleado empleado) {

        String query = "DELETE FROM empleados WHERE id ="+ empleado.getId();

        try (Connection conn = abrirConexion();
             Statement stmt = conn.createStatement()) {
            return stmt.executeUpdate(query); // devuelve el número de registros agregados en la base de datos

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // D - Delete (DELETE) - PreparedStatement
    public int eliminarPS(Empleado empleado) {

        String query = "DELETE FROM empleados WHERE id = ?";

        try (Connection conn = abrirConexion();
             // Statement stmt = conn.createStatement()) {
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, empleado.getId());
            return ps.executeUpdate(); // devuelve el número de registros agregados en la base de datos

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // FILTRO
    public ArrayList<Empleado> getEmpleadosPorFiltro(String nombreFiltro, String puestoFiltro, double salarioFiltro) {

        ArrayList<Empleado> empleados = new ArrayList<>();
        String query = "SELECT * FROM empleados WHERE 1=1";

        if (nombreFiltro != null) {
            query += " AND nombre = '" + nombreFiltro + "'";
        }

        if (puestoFiltro != null) {
            query += " AND puesto = '" + puestoFiltro + "'";
        }

        if (salarioFiltro != 0) {
            query += " AND salario = '" + salarioFiltro + "'";
        }

        try(Connection conn = abrirConexion();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)){

            while(rs.next()){ // recorrer cada linea del ResultSet
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String puesto = rs.getString("puesto");
                double salario = rs.getDouble("salario");

                empleados.add(new Empleado(id, nombre, puesto, salario));
            }

        } catch(SQLException e){
            e.printStackTrace();
        }
        return empleados;
    }

    // FILTRO - PreparedStatement
    public ArrayList<Empleado> getEmpleadosPorFiltroPS(String nombreFiltro, String puestoFiltro, double salarioFiltro) {
        ArrayList<Empleado> empleados = new ArrayList<>();
        String query = "SELECT * FROM empleados WHERE 1=1";

        if (nombreFiltro != null) {
            query += " AND nombre = ?";
        }

        if (puestoFiltro != null) {
            query += " AND puesto = ?";
        }

        if (salarioFiltro != 0) {
            query += " AND salario = ?";
        }

        try (Connection conn = abrirConexion();
             //Statement statement = conn.createStatement();
             PreparedStatement ps = conn.prepareStatement(query);
             //ResultSet resultSet = statement.executeQuery(query)
             ) {

            int index = 1;

            if (nombreFiltro != null){
                ps.setString(index,nombreFiltro);
                index++;
            }

            if (puestoFiltro != null){
                ps.setString(index,puestoFiltro);
                index++;
            }

            if (salarioFiltro != 0){
                ps.setDouble(index,salarioFiltro);
            }

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {   //recorrer cada línea del resultSet
                int id = resultSet.getInt("id");
                String nombre = resultSet.getNString("nombre");
                String puesto = resultSet.getNString("puesto");
                double salario = resultSet.getDouble("salario");
                empleados.add(new Empleado(id, nombre, puesto, salario));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return empleados;

    }

    // metodo para llamar el Procedure IN y OUT
    public int contarEmpleadosPorPuestoCS(String puesto) {
        String call = "{CALL contar_empleados_por_puesto(?, ?)}";

        try (Connection conn = abrirConexion();
             CallableStatement cs = conn.prepareCall(call)) {

            cs.setString(1, puesto);               // Parámetro de entrada
            cs.registerOutParameter(2, Types.INTEGER); // Parámetro de salida

            cs.execute(); // Ejecuta el procedimiento

            int total = cs.getInt(2); // Recupera el valor del parámetro OUT
            return total;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Devuelve -1 si ocurre un error
        }
    }


    // metodo para llamar un Procedure IN-OUT
    public String aumentarPuestoCS(String puesto, String sufijo) {
        String call = "{CALL aumentar_puesto(?, ?)}";

        try (Connection conn = abrirConexion();
             CallableStatement cs = conn.prepareCall(call)) {

            // Parámetro INOUT
            cs.setString(1, puesto);
            cs.registerOutParameter(1, Types.VARCHAR);

            // Parámetro IN
            cs.setString(2, sufijo);

            cs.execute();

            // Recupera el valor actualizado del parámetro INOUT
            String nuevoPuesto = cs.getString(1);
            return nuevoPuesto;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // para cargar todos los puestos en la base de datos, util para ponerlos en un combobox
    public ArrayList<String> obtenerPuestos() {
        ArrayList<String> puestos = new ArrayList<>();
        String query = "SELECT DISTINCT puesto FROM empleados ORDER BY puesto ASC";

        try (Connection conn = abrirConexion();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                puestos.add(rs.getString("puesto"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return puestos;
    }

}

