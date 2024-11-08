module com.alesandro.productos {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.alesandro.productos to javafx.fxml;
    exports com.alesandro.productos;
    exports com.alesandro.productos.controller;
    opens com.alesandro.productos.controller to javafx.fxml;
}