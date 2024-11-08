module com.alesandro.productos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.alesandro.productos to javafx.fxml;
    exports com.alesandro.productos;
    exports com.alesandro.productos.controller;
    exports com.alesandro.productos.model;
    exports com.alesandro.productos.dao;
    exports com.alesandro.productos.db;
    opens com.alesandro.productos.controller to javafx.fxml;
}