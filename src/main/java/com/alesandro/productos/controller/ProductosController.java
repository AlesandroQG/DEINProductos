package com.alesandro.productos.controller;

import com.alesandro.productos.ProductosApplication;
import com.alesandro.productos.dao.DaoProducto;
import com.alesandro.productos.model.Producto;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Clase que controla los eventos de la ventana principal
 */
public class ProductosController implements Initializable {
    private Blob imagenProducto;

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
        this.imagenProducto = null;
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
        // Añadir listener para cuando se selecciona un item de la tabla
        tabla.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Producto>() {
            @Override
            public void changed(ObservableValue<? extends Producto> observableValue, Producto oldValue, Producto newValue) {
                if (newValue != null) {
                    txtCodigo.setText(newValue.getCodigo());
                    txtCodigo.setDisable(true);
                    txtNombre.setText(newValue.getNombre());
                    txtPrecio.setText(newValue.getPrecio() + "");
                    cbDisponible.setSelected(newValue.isDisponible());
                    imagenProducto = newValue.getImagen();
                    if (newValue.getImagen() != null) {
                        try {
                            InputStream image = newValue.getImagen().getBinaryStream();
                            imagen.setImage(new Image(image));
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    btnCrear.setDisable(true);
                    btnActualizar.setDisable(false);
                }
            }
        });
        // Cargar productos
        cargarTabla();
    }

    /**
     * Función que carga los productos de la base de datos a la tabla
     */
    public void cargarTabla() {
        tabla.getItems().clear();
        limpiar(null);
        tabla.setItems(DaoProducto.cargarListado());
    }

    /**
     * Función para mostrar la imagen de un producto
     *
     * @param actionEvent
     */
    private void verImagen(ActionEvent actionEvent) {
        Producto producto = tabla.getSelectionModel().getSelectedItem();
        if (producto == null) {
            alerta("No hay ningún producto seleccionado");
        } else {
            if (producto.getImagen() == null) {
                alerta("Ese producto no tiene imagen");
            } else {
                try {
                    Window ventana = tabla.getScene().getWindow();
                    InputStream image = producto.getImagen().getBinaryStream();
                    VBox root = new VBox();
                    ImageView imagen = new ImageView(new Image(image));
                    imagen.setFitWidth(300);
                    imagen.setFitHeight(300);
                    root.getChildren().add(imagen);
                    Scene scene = new Scene(root, 300, 300);
                    Stage stage = new Stage();
                    stage.setResizable(false);
                    stage.setScene(scene);
                    stage.setTitle("Imagen");
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initOwner(ventana);
                    stage.getIcons().add(new Image(ProductosApplication.class.getResourceAsStream("images/carrito.png")));
                    stage.showAndWait();
                } catch (SQLException e) {
                    //e.printStackTrace();
                    alerta("No se ha podido cargar la imagen");
                }
            }
        }
    }

    /**
     * Función para eliminar un producto
     *
     * @param actionEvent
     */
    private void eliminar(ActionEvent actionEvent) {
        Producto producto = tabla.getSelectionModel().getSelectedItem();
        if (producto == null) {
            alerta("No hay ningún producto seleccionado");
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initOwner(txtNombre.getScene().getWindow());
            alert.setHeaderText(null);
            alert.setTitle("Eliminar producto");
            alert.setContentText("¿Estás seguro que quieres eliminar ese producto?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (DaoProducto.eliminar(producto)) {
                    confirmacion("Producto eliminado correctamente");
                    cargarTabla();
                } else {
                    alerta("No se ha podido eliminar ese producto de la base de datos");
                }
            }
        }
    }

    /**
     * Función que se ejecuta cuando se pulsa el botón "Actualizar". Actualiza un producto
     *
     * @param event
     */
    @FXML
    void actualizar(ActionEvent event) {
        String error = validar();
        if (!error.isEmpty()) {
            alerta(error);
        } else {
            Producto producto = DaoProducto.getProducto(txtCodigo.getText());
            producto.setNombre(txtNombre.getText());
            producto.setPrecio(Float.parseFloat(txtPrecio.getText()));
            producto.setDisponible(cbDisponible.isSelected());
            producto.setImagen(imagenProducto);
            if (DaoProducto.modificar(producto)) {
                confirmacion("Producto actualizado correctamente");
                cargarTabla();
            } else {
                alerta("No se ha podido actualizar ese producto en la base de datos");
            }
        }
    }

    /**
     * Función que se ejecuta cuando se pulsa el botón "Crear". Crea un producto
     *
     * @param event
     */
    @FXML
    void crear(ActionEvent event) {
        if (txtCodigo.getText().isEmpty()) {
            alerta("El código no puede estar vacío");
        } else if (txtCodigo.getText().length() != 5) {
            alerta("El código debe tener 5 caracteres");
        } else {
            Producto p = DaoProducto.getProducto(txtCodigo.getText());
            if (p != null) {
                alerta("Ya existe un producto con ese código");
            } else {
                String error = validar();
                if (!error.isEmpty()) {
                    alerta(error);
                } else {
                    Producto producto = new Producto();
                    producto.setCodigo(txtCodigo.getText());
                    producto.setNombre(txtNombre.getText());
                    producto.setPrecio(Float.parseFloat(txtPrecio.getText()));
                    producto.setDisponible(cbDisponible.isSelected());
                    producto.setImagen(imagenProducto);
                    if (DaoProducto.insertar(producto)) {
                        confirmacion("Producto creado correctamente");
                        cargarTabla();
                    } else {
                        alerta("No se ha podido crear ese producto en la base de datos");
                    }
                }
            }
        }
    }

    /**
     * Función que valida los campos del formulario
     *
     * @return un string con posibles errores
     */
    public String validar() {
        String error = "";
        if (txtNombre.getText().isEmpty()) {
            error += "El nombre no puede estar vacío\n";
        }
        if (txtPrecio.getText().isEmpty()) {
            error += "El precio no puede estar vacío\n";
        } else {
            try {
                Float.parseFloat(txtPrecio.getText());
            } catch (NumberFormatException e) {
                error += "El precio debe ser un número decimal\n";
            }
        }
        return error;
    }

    /**
     * Función que se ejecuta cuando se pulsa el botón "Acerca de…". Muestra información sobre la aplicación
     *
     * @param event
     */
    @FXML
    void acerca(ActionEvent event) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("INFO");
        String info = "Gestión de productos 1.0\n";
        info += "Autor: Alesandro Quirós Gobbato";
        alerta.setContentText(info);
        Stage alertaStage = (Stage) alerta.getDialogPane().getScene().getWindow();
        alertaStage.getIcons().add(new Image(ProductosApplication.class.getResourceAsStream("images/carrito.png")));
        alerta.showAndWait();
    }

    /**
     * Función que se ejecuta cuando se pulsa el botón "Limpiar". Vacía el formulario
     *
     * @param event
     */
    @FXML
    void limpiar(ActionEvent event) {
        imagenProducto = null;
        txtCodigo.setText("");
        txtCodigo.setDisable(false);
        txtNombre.setText("");
        txtPrecio.setText("");
        cbDisponible.setSelected(false);
        imagen.setImage(null);
        btnCrear.setDisable(false);
        btnActualizar.setDisable(true);
        tabla.getSelectionModel().clearSelection();
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
                imagenProducto = blob;
                imagen.setImage(new Image(image));
                imagen.setDisable(false);
            }
        } catch (IOException | NullPointerException e) {
            //e.printStackTrace();
            System.out.println("Imagen no seleccionada");
        } catch (SQLException e) {
            //e.printStackTrace();
            alerta("No se ha podido convertir la imagen al formato Blob");
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