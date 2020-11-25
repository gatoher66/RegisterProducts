package com.galata.registerproducts.modelo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.galata.registerproducts.utilidades.Utilidades;

public class ConexionSQLite extends SQLiteOpenHelper
{

    public ConexionSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase bd)
    {
        bd.execSQL(Utilidades.CREAR_TABLA_PRODUCTOS);
        bd.execSQL(Utilidades.CREAR_TABLA_CARD_PRODUCTO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int versionAntigua, int versionNueva)
    {
        bd.execSQL( "DROP TABLE IF EXISTS " + Utilidades.TABLA_PRODUCTOS );
        bd.execSQL( "DROP TABLE IF EXISTS " + Utilidades.TABLA_CARD_PRODUCTO );
        onCreate(bd);
    }
}
