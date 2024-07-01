package com.example.aplicacion_dima;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class CS_SQLite_GestorRecetasDB {

    public SQLiteDatabase getConn(Context context){
        CS_SQLite_Conexion conn = new CS_SQLite_Conexion(context);
        return conn.getWritableDatabase();
    }

    public int insertaReceta(Context context, CS_RV_valores_receta receta) {
        int res = 0;
        try (SQLiteDatabase db = this.getConn(context)) {
            ContentValues values = new ContentValues();
            values.put("nombre", receta.getNombre());
            values.put("descripcion", receta.getDescripcion());
            values.put("imagen", receta.getImagenBytes());

            // Insertar la receta y obtener el ID asignado por la base de datos
            long id = db.insert("recetas", null, values);
            receta.setId((int) id); // Asignar el ID a la receta
            res = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Cerrar la conexión con la base de datos después de usarla
        return res;
    }

    public List<CS_RV_valores_receta> obtenerTodasRecetas(Context context) {
        SQLiteDatabase db = this.getConn(context);
        List<CS_RV_valores_receta> listaRecetas = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.query("recetas", null, null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id_receta")); // Obtener el ID de la receta
                    @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
                    @SuppressLint("Range") String descripcion = cursor.getString(cursor.getColumnIndex("descripcion"));
                    @SuppressLint("Range") byte[] imagen = cursor.getBlob(cursor.getColumnIndex("imagen"));

                    CS_RV_valores_receta receta = new CS_RV_valores_receta();
                    receta.setId(id); // Asignar el ID a la receta
                    receta.setNombre(nombre);
                    receta.setDescripcion(descripcion);
                    receta.setImagenBytes(imagen);

                    listaRecetas.add(receta);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close(); // Cerrar la conexión con la base de datos después de usarla
        }

        return listaRecetas;
    }

    public int modificarReceta(Context context, CS_RV_valores_receta receta) {
        int res = 0;
        try (SQLiteDatabase db = this.getConn(context)) {
            ContentValues values = new ContentValues();
            values.put("nombre", receta.getNombre());
            values.put("descripcion", receta.getDescripcion());
            values.put("imagen", receta.getImagenBytes());

            String[] whereArgs = {String.valueOf(receta.getId())}; // Utilizar el id como criterio
            int numRowsUpdated = db.update("recetas", values, "id_receta = ?", whereArgs);

            if (numRowsUpdated > 0) {
                res = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Cerrar la conexión con la base de datos después de usarla
        return res;
    }

    public int eliminarReceta(Context context, int idReceta) {
        int res = 0;
        try (SQLiteDatabase db = this.getConn(context)) {
            db.beginTransaction(); // Iniciar transacción

            try {
                String[] whereArgs = {String.valueOf(idReceta)}; // Utilizar el id como criterio

                // Eliminar los datos de la tabla TBL_RECETA_INGREDIENTE para la receta especificada
                db.delete("receta_ingredientes", "id_receta = ?", whereArgs);

                // Eliminar la receta de la tabla recetas
                int numRowsDeleted = db.delete("recetas", "id_receta = ?", whereArgs);

                // Si se eliminaron ingredientes o recetas, se considera como éxito
                if (numRowsDeleted > 0) {
                    res = 1;
                    db.setTransactionSuccessful(); // Marcar la transacción como exitosa
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction(); // Finalizar transacción
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Cerrar la conexión con la base de datos después de usarla
        return res;
    }
    public CS_RV_valores_receta mostrarReceta(Context context, int idReceta) {
        CS_RV_valores_receta receta = null;
        SQLiteDatabase db = this.getConn(context);
        Cursor cursor = null;

        try {
            // Consulta para obtener la receta con el ID proporcionado
            String query = "SELECT * FROM recetas WHERE id_receta = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(idReceta)});

            if (cursor != null && cursor.moveToFirst()) {
                // Obtener los datos de la receta
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id_receta"));
                @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
                @SuppressLint("Range") String descripcion = cursor.getString(cursor.getColumnIndex("descripcion"));
                @SuppressLint("Range") byte[] imagenBytes = cursor.getBlob(cursor.getColumnIndex("imagen"));

                // Crear un objeto CS_RV_valores_receta con los datos obtenidos
                receta = new CS_RV_valores_receta(id, nombre, descripcion, imagenBytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close(); // Cerrar la conexión con la base de datos después de usarla
        }

        return receta;
    }

}
