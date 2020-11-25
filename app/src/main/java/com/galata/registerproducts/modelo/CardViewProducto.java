package com.galata.registerproducts.modelo;

public class CardViewProducto
{
    private String clave;
    private String nombre;
    private String precio;
    private String inicial;
    private String detalle;
    private String cantidad;
    private int color;
    private long id;

    public CardViewProducto()
    {

    }

    public CardViewProducto(String clave, String nombre, String precio, String inicial, String detalle, String cantidad, int color, long id) {
        this.clave = clave;
        this.nombre = nombre;
        this.precio = precio;
        this.inicial = inicial;
        this.detalle = detalle;
        this.cantidad = cantidad;
        this.color = color;
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getInicial() {
        return inicial;
    }

    public void setInicial(String inicial) {
        this.inicial = inicial;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
