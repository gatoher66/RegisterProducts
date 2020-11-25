package com.galata.registerproducts.utilidades;

public class Utilidades
{
    //datos producto
    public static final String TABLA_PRODUCTOS = "productos";
    public static final String CAMPO_CLAVE = "clave";
    public static final String CAMPO_NOMBRE = "nombre";
    public static final String CAMPO_PRECIO = "precio";
    public static final String CAMPO_DESC = "descripcion";
    public static final String CAMPO_CANTIDAD = "cantidad";

    public static final String CREAR_TABLA_PRODUCTOS = "CREATE TABLE "+TABLA_PRODUCTOS+
            "("+CAMPO_CLAVE+" TEXT PRIMARY KEY, " + CAMPO_NOMBRE+" TEXT, "+
                        CAMPO_PRECIO + " FLOAT, " + CAMPO_DESC + " TEXT, " + CAMPO_CANTIDAD +
                         " INTEGER)";
    //datos card_producto
    public static final String TABLA_CARD_PRODUCTO = "cardProduct";
    public static final String CAMPO_ID_CARDP = "id_cardP";
    public static final String CAMPO_INICIAL = "inicial";
    public static final String CAMPO_COLOR = "color";

    public static final String CREAR_TABLA_CARD_PRODUCTO = "CREATE TABLE "+TABLA_CARD_PRODUCTO+
            "("+CAMPO_ID_CARDP+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            CAMPO_INICIAL+" TEXT, "+ CAMPO_COLOR + " INTEGER)";

}
