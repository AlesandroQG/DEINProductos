package com.alesandro.productos.model;

import java.sql.Blob;
import java.util.Objects;

/**
 * Clase Producto
 */
public class Producto {
    private String codigo;
    private String nombre;
    private float precio;
    private boolean disponible;
    private Blob imagen;

    /**
     * Constructor con parámetros de producto
     *
     * @param codigo del producto
     * @param nombre del producto
     * @param precio del producto
     * @param disponible del producto
     * @param imagen del producto
     */
    public Producto(String codigo, String nombre, float precio, boolean disponible, Blob imagen) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.disponible = disponible;
        this.imagen = imagen;
    }

    /**
     * Constructor vacío de producto
     */
    public Producto() {}

    /**
     * Getter para el codigo del producto
     *
     * @return codigo del producto
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Setter para el codigo del producto
     *
     * @param codigo nuevo codigo del producto
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Getter para el nombre del producto
     *
     * @return nombre del producto
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Setter para el nombre del producto
     *
     * @param nombre nuevo nombre del producto
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Getter para el precio del producto
     *
     * @return precio del producto
     */
    public float getPrecio() {
        return precio;
    }

    /**
     * Setter para el precio del producto
     *
     * @param precio nuevo precio del producto
     */
    public void setPrecio(float precio) {
        this.precio = precio;
    }

    /**
     * Getter para la disponibilidad del producto
     *
     * @return si el producto esta disponible o no
     */
    public boolean isDisponible() {
        return disponible;
    }

    /**
     * Setter para la disponibilidad del producto
     *
     * @param disponible nueva disponibilidad del producto
     */
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    /**
     * Getter para la imagen del producto
     *
     * @return imagen del producto
     */
    public Blob getImagen() {
        return imagen;
    }

    /**
     * Setter para la imagen id del producto
     *
     * @param imagen nueva imagen del producto
     */
    public void setImagen(Blob imagen) {
        this.imagen = imagen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return Objects.equals(codigo, producto.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(codigo);
    }
}
