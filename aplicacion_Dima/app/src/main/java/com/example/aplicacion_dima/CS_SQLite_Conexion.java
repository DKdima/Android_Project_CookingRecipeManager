package com.example.aplicacion_dima;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CS_SQLite_Conexion extends SQLiteOpenHelper {

    final String TBL_INGREDIENTE = "ingredientes";
    final String TBL_RECETA = "recetas";
    final String TBL_RECETA_INGREDIENTE = "receta_ingredientes";

    public CS_SQLite_Conexion(Context context) {
        super(context, "recetas_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableIngredientes = "CREATE TABLE " + TBL_INGREDIENTE + " (id_ingrediente INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, descripcion TEXT, imagen BLOB)";
        String createTableRecetas = "CREATE TABLE " + TBL_RECETA + " (id_receta INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, descripcion TEXT, imagen BLOB)";
        String createTableRecetaIngredientes = "CREATE TABLE " + TBL_RECETA_INGREDIENTE + " (id_completo INTEGER PRIMARY KEY AUTOINCREMENT, id_receta INTEGER, id_ingrediente INTEGER, cantidad TEXT, FOREIGN KEY (id_receta) REFERENCES " + TBL_RECETA + "(id_receta), FOREIGN KEY (id_ingrediente) REFERENCES " + TBL_INGREDIENTE + "(id_ingrediente))";


        db.execSQL(createTableIngredientes);
        db.execSQL(createTableRecetas);
        db.execSQL(createTableRecetaIngredientes);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
