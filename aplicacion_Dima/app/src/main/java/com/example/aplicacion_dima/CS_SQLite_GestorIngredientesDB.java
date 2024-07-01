package com.example.aplicacion_dima;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.annotation.SuppressLint;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;
public class CS_SQLite_GestorIngredientesDB {

    public SQLiteDatabase getConn(Context context){
        CS_SQLite_Conexion conn = new CS_SQLite_Conexion(context);
        return conn.getWritableDatabase();
    }
    public int insertaIngrediente(Context context, CS_RV_valores_ingredientes dto){
        int res = 0;
        try (SQLiteDatabase db = this.getConn(context)) {
            ContentValues values = new ContentValues();
            values.put("nombre", dto.getNombre());
            values.put("descripcion", dto.getDescripcion());
            // Aquí inserta la imagen como bytes en la base de datos
            values.put("imagen", dto.getImagenBytes());

            // Insertar el nuevo ingrediente y obtener el ID asignado por la base de datos
            long id = db.insert("ingredientes", null, values);
            dto.setId((int) id); // Asignar el ID al objeto CS_RV_valores_ingredientes
            res = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Cerrar la conexión con la base de datos después de usarla
        return res;
    }
    public List<CS_RV_valores_ingredientes> obtenerTodosIngredientes(Context context) {
        SQLiteDatabase db = this.getConn(context);
        List<CS_RV_valores_ingredientes> listaIngredientes = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.query("ingredientes", null, null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id_ingrediente")); // Obtener el ID del ingrediente
                    @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
                    @SuppressLint("Range") String descripcion = cursor.getString(cursor.getColumnIndex("descripcion"));
                    @SuppressLint("Range") byte[] imagen = cursor.getBlob(cursor.getColumnIndex("imagen"));

                    CS_RV_valores_ingredientes ingrediente = new CS_RV_valores_ingredientes();
                    ingrediente.setId(id); // Asignar el ID al objeto CS_RV_valores_ingredientes
                    ingrediente.setNombre(nombre);
                    ingrediente.setDescripcion(descripcion);
                    ingrediente.setImagenBytes(imagen);

                    listaIngredientes.add(ingrediente);
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

        return listaIngredientes;
    }
    public CS_RV_valores_ingredientes obtenerIngredientePorId(Context context, int idIngrediente) {
        SQLiteDatabase db = this.getConn(context);
        Cursor cursor = null;
        CS_RV_valores_ingredientes ingrediente = null;

        try {
            // Consultar la base de datos para obtener el ingrediente con el ID proporcionado
            String[] projection = {"nombre", "descripcion", "imagen"};
            String selection = "id_ingrediente = ?";
            String[] selectionArgs = {String.valueOf(idIngrediente)};
            cursor = db.query("ingredientes", projection, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Obtener los datos del cursor
                @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
                @SuppressLint("Range") String descripcion = cursor.getString(cursor.getColumnIndex("descripcion"));
                @SuppressLint("Range") byte[] imagen = cursor.getBlob(cursor.getColumnIndex("imagen"));

                // Crear un objeto CS_RV_valores_ingredientes con los datos obtenidos
                ingrediente = new CS_RV_valores_ingredientes(idIngrediente, nombre, descripcion, imagen);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close(); // Cerrar la conexión con la base de datos después de usarla
        }

        return ingrediente;
    }


    public int modificarIngrediente(Context context, CS_RV_valores_ingredientes dto) {
        int res = 0;
        try (SQLiteDatabase db = this.getConn(context)) {
            ContentValues values = new ContentValues();
            values.put("nombre", dto.getNombre());
            values.put("descripcion", dto.getDescripcion());
            values.put("imagen", dto.getImagenBytes());

            String[] whereArgs = {String.valueOf(dto.getId())}; // Utilizar el id como criterio
            int numRowsUpdated = db.update("ingredientes", values, "id_ingrediente = ?", whereArgs);

            if (numRowsUpdated > 0) {
                res = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Cerrar la conexión con la base de datos después de usarla
        return res;
    }

    public int eliminarIngrediente(Context context, int idIngrediente) {
        int res = 0;
        SQLiteDatabase db = null;
        try {
            db = this.getConn(context);
            db.beginTransaction(); // Iniciar una transacción para garantizar la integridad de los datos

            // Eliminar el ingrediente de la tabla ingredientes
            String[] whereArgs = {String.valueOf(idIngrediente)};
            int numRowsDeleted = db.delete("ingredientes", "id_ingrediente = ?", whereArgs);

            if (numRowsDeleted > 0) {
                // Si se eliminó el ingrediente correctamente, eliminar todas las entradas correspondientes en la tabla receta_ingredientes
                int numRowsDeletedFromRecetaIngredientes = db.delete("receta_ingredientes", "id_ingrediente = ?", whereArgs);

                // Comprobar si se eliminaron todas las entradas correctamente en receta_ingredientes
                if (numRowsDeletedFromRecetaIngredientes == numRowsDeleted) {
                    res = 1; // Indicar éxito si se eliminaron todas las entradas correctamente
                }
            }

            // Marcar la transacción como exitosa
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Finalizar la transacción y cerrar la conexión con la base de datos después de usarla
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return res;
    }

}