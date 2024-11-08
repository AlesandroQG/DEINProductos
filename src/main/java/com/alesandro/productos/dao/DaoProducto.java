package com.alesandro.productos.dao;

import com.alesandro.productos.db.DBConnect;
import com.alesandro.productos.model.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;

/**
 * Clase donde se ejecuta las consultas para la tabla Producto
 */
public class DaoProducto {
    /**
     * Metodo que busca un producto por medio de su codigo
     *
     * @param codigo del producto a buscar
     * @return producto o null
     */
    public static Producto getProducto(String codigo) {
        DBConnect connection;
        Producto producto = null;
        try {
            connection = new DBConnect();
            String consulta = "SELECT codigo,nombre,precio,disponible,imagen FROM Producto WHERE codigo = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, codigo);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String codigo_db = rs.getString("codigo");
                String nombre = rs.getString("nombre");
                float precio = rs.getFloat("precio");
                boolean disponible = rs.getBoolean("disponible");
                Blob imagen = rs.getBlob("imagen");
                producto = new Producto(codigo_db,nombre,precio,disponible,imagen);
            }
            rs.close();
            connection.closeConnection();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return producto;
    }

    /**
     * Función que convierto un objeto File a Blob
     *
     * @param file fichero imagen
     * @return blob
     * @throws SQLException
     * @throws IOException
     */
    public static Blob convertFileToBlob(File file) throws SQLException, IOException {
        DBConnect connection = new DBConnect();
        // Open a connection to the database
        try (Connection conn = connection.getConnection();
             FileInputStream inputStream = new FileInputStream(file)) {

            // Create Blob
            Blob blob = conn.createBlob();
            // Write the file's bytes to the Blob
            byte[] buffer = new byte[1024];
            int bytesRead;

            try (var outputStream = blob.setBinaryStream(1)) {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            return blob;
        }
    }

    /**
     * Metodo que carga los datos de la tabla Productos y los devuelve para usarlos en un listado de productos
     *
     * @return listado de productos para cargar en un tableview
     */
    public static ObservableList<Producto> cargarListado() {
        DBConnect connection;
        ObservableList<Producto> productos = FXCollections.observableArrayList();
        try{
            connection = new DBConnect();
            String consulta = "SELECT codigo,nombre,precio,disponible,imagen FROM Producto";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String codigo = rs.getString("codigo");
                String nombre = rs.getString("nombre");
                float precio = rs.getFloat("precio");
                boolean disponible = rs.getBoolean("disponible");
                Blob imagen = rs.getBlob("imagen");
                Producto producto = new Producto(codigo,nombre,precio,disponible,imagen);
                productos.add(producto);
            }
            rs.close();
            connection.closeConnection();
        }catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return productos;
    }

    /**
     * Metodo que modifica los datos de un producto en la BD
     *
     * @param producto		Instancia del producto con datos a modificar
     * @return			true/false
     */
    public static boolean modificar(Producto producto) {
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "UPDATE Producto SET nombre = ?,precio = ?,disponible = ?,imagen = ? WHERE codigo = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, producto.getNombre());
            pstmt.setFloat(2, producto.getPrecio());
            pstmt.setBoolean(3, producto.isDisponible());
            pstmt.setBlob(4, producto.getImagen());
            pstmt.setString(5, producto.getCodigo());
            int filasAfectadas = pstmt.executeUpdate();
            System.out.println("Actualizado producto");
            pstmt.close();
            connection.closeConnection();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Metodo que CREA un nuevo producto en la BD
     *
     * @param producto		Instancia del modelo producto con datos nuevos
     * @return			id/-1
     */
    public  static int insertar(Producto producto) {
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "INSERT INTO Producto (codigo,nombre,precio,disponible,imagen) VALUES (?,?,?,?,?) ";
            pstmt = connection.getConnection().prepareStatement(consulta, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, producto.getCodigo());
            pstmt.setString(2, producto.getNombre());
            pstmt.setFloat(3, producto.getPrecio());
            pstmt.setBoolean(4, producto.isDisponible());
            pstmt.setBlob(5, producto.getImagen());
            int filasAfectadas = pstmt.executeUpdate();
            System.out.println("Nueva entrada en producto");
            if (filasAfectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    pstmt.close();
                    connection.closeConnection();
                    return id;
                }
            }
            pstmt.close();
            connection.closeConnection();
            return -1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return -1;
        }
    }

    /**
     * Elimina un producto en función del modelo Producto que le hayamos pasado
     *
     * @param producto Producto a eliminar
     * @return a boolean
     */
    public static boolean eliminar(Producto producto) {
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "DELETE FROM Producto WHERE codigo = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, producto.getCodigo());
            int filasAfectadas = pstmt.executeUpdate();
            pstmt.close();
            connection.closeConnection();
            System.out.println("Eliminado con éxito");
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
