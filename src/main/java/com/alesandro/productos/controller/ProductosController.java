package com.alesandro.productos.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.net.URL;
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
    private TableColumn<?, ?> colCodigo; // Value injected by FXMLLoader

    @FXML // fx:id="colDisponible"
    private TableColumn<?, ?> colDisponible; // Value injected by FXMLLoader

    @FXML // fx:id="colNombre"
    private TableColumn<?, ?> colNombre; // Value injected by FXMLLoader

    @FXML // fx:id="colPrecio"
    private TableColumn<?, ?> colPrecio; // Value injected by FXMLLoader

    @FXML // fx:id="imagen"
    private ImageView imagen; // Value injected by FXMLLoader

    @FXML // fx:id="tabla"
    private TableView<?> tabla; // Value injected by FXMLLoader

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
        //
    }

    @FXML
    void actualizar(ActionEvent event) {

    }

    @FXML
    void crear(ActionEvent event) {

    }

    @FXML
    void limpiar(ActionEvent event) {

    }

    @FXML
    void seleccionarImagen(ActionEvent event) {

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