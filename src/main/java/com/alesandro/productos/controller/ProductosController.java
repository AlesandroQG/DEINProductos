package com.alesandro.productos.controller;

import com.alesandro.productos.dao.DaoProducto;
import com.alesandro.productos.model.Producto;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Clase que controla los eventos de la ventana principal
 */
public class ProductosController implements Initializable {
    @FXML // fx:id="btnActualizar"
    private Button btnActualizar; // Value injected by FXMLLoader

    @FXML // fx:id="btnCrear"
    private Button btnCrear; // Value injected by FXMLLoader

    @FXML // fx:id="cbDisponible"
    private CheckBox cbDisponible; // Value injected by FXMLLoader

    @FXML // fx:id="colCodigo"
    private TableColumn<Producto, String> colCodigo; // Value injected by FXMLLoader

    @FXML // fx:id="colDisponible"
    private TableColumn<Producto, Boolean> colDisponible; // Value injected by FXMLLoader

    @FXML // fx:id="colNombre"
    private TableColumn<Producto, String> colNombre; // Value injected by FXMLLoader

    @FXML // fx:id="colPrecio"
    private TableColumn<Producto, Float> colPrecio; // Value injected by FXMLLoader

    @FXML // fx:id="imagen"
    private ImageView imagen; // Value injected by FXMLLoader

    @FXML // fx:id="tabla"
    private TableView<Producto> tabla; // Value injected by FXMLLoader

    @FXML // fx:id="txtCodigo"
    private TextField txtCodigo; // Value injected by FXMLLoader

    @FXML // fx:id="txtNombre"
    private TextField txtNombre; // Value injected by FXMLLoader

    @FXML // fx:id="txtPrecio"
    private TextField txtPrecio; // Value injected by FXMLLoader

    /**
     * Función que se ejecuta cuando se inicia la ventana
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Columnas de la tabla
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colDisponible.setCellValueFactory(cellData -> {
            Producto p = cellData.getValue();
            boolean v = p.isDisponible();
            return new ReadOnlyBooleanWrapper(v);
        });
        colDisponible.setCellFactory(CheckBoxTableCell.<Producto>forTableColumn(colDisponible));
        // ContextMenu
        ContextMenu contextMenu = new ContextMenu();
        MenuItem verImagenItem = new MenuItem("Ver imagen");
        MenuItem borrarItem = new MenuItem("Eliminar");
        contextMenu.getItems().addAll(verImagenItem,borrarItem);
        verImagenItem.setOnAction(this::verImagen);
        borrarItem.setOnAction(this::eliminar);
        tabla.setRowFactory(tv -> {
            TableRow<Producto> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    tabla.getSelectionModel().select(row.getItem());
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });
            return row;
        });
        // Cargar productos
        cargarTabla();
    }

    /**
     * Función que carga los productos de la base de datos a la tabla
     */
    public void cargarTabla() {
        tabla.setItems(DaoProducto.cargarListado());
    }

    /**
     * Función para mostrar la imagen de un producto
     *
     * @param actionEvent
     */
    private void verImagen(ActionEvent actionEvent) {

    }

    /**
     * Función para eliminar un producto
     *
     * @param actionEvent
     */
    private void eliminar(ActionEvent actionEvent) {

    }

    /**
     * Función que se ejecuta cuando se pulsa el botón "Actualizar". Actualiza un producto
     *
     * @param event
     */
    @FXML
    void actualizar(ActionEvent event) {

    }

    /**
     * Función que se ejecuta cuando se pulsa el botón "Crear". Crea un producto
     *
     * @param event
     */
    @FXML
    void crear(ActionEvent event) {

    }

    /**
     * Función que se ejecuta cuando se pulsa el botón "Limpiar". Vacía el formulario
     *
     * @param event
     */
    @FXML
    void limpiar(ActionEvent event) {

    }

    /**
     * Función que se ejecuta cuando se pulsa el botón "Seleccionar". Abre un FileChooser para seleccionar una imagen
     *
     * @param event
     */
    @FXML
    void seleccionarImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione una imagen");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files","*.jpg", "*.jpeg","*.png"));
        fileChooser.setInitialDirectory(new File("."));
        File file = fileChooser.showOpenDialog(null);
        try {
            double kbs = (double) file.length() / 1024;
            if (kbs > 64) {
                alerta("");
            } else {
                InputStream image = new FileInputStream(file);
                Blob blob = DaoProducto.convertFileToBlob(file);
                imagen.setImage(new Image(image));
                imagen.setDisable(false);
            }
        } catch (IOException | NullPointerException e) {
            //e.printStackTrace();
            System.out.println("Imagen no seleccionada");
        } catch (SQLException e) {
            e.printStackTrace();
            alerta("No se ha podido ");
        }
    }

    /**
     * Función que muestra un mensaje de alerta al usuario
     *
     * @param texto contenido de la alerta
     */
    public void alerta(String texto) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(null);
        alerta.setTitle("Error");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }

    /**
     * Función que muestra un mensaje de confirmación al usuario
     *
     * @param texto contenido del mensaje
     */
    public void confirmacion(String texto) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Info");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }
}