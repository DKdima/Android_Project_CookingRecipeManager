package com.example.aplicacion_dima;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class CS_SQLite_GestorRI_DB {

    public SQLiteDatabase getConn(Context context) {
        CS_SQLite_Conexion conn = new CS_SQLite_Conexion(context);
        return conn.getWritableDatabase();
    }

        // Método para insertar un nuevo registro en la tabla receta_ingredientes
        public int insertarRecetaIngrediente(Context context, int idReceta, int idIngrediente, String cantidad) {
            int res = 0;
            try (SQLiteDatabase db = this.getConn(context)) {
                // Insertar el nuevo registro
                ContentValues values = new ContentValues();
                values.put("id_receta", idReceta);
                values.put("id_ingrediente", idIngrediente);
                values.put("cantidad", cantidad);

                long id = db.insert("receta_ingredientes", null, values);

                if (id != -1) {
                    res = 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Cerrar la conexión con la base de datos después de usarla
            return res;
        }
    // Método para actualizar la cantidad de un registro existente en la tabla receta_ingredientes
    public int actualizarCantidadRecetaIngrediente(Context context, int idReceta, int idIngrediente, String nuevaCantidad) {
        int res = 0;
        try (SQLiteDatabase db = this.getConn(context)) {
            // Actualizar la cantidad del registro existente
            ContentValues values = new ContentValues();
            values.put("cantidad", nuevaCantidad);

            int numRowsUpdated = db.update("receta_ingredientes", values, "id_receta = ? AND id_ingrediente = ?", new String[]{String.valueOf(idReceta), String.valueOf(idIngrediente)});

            if (numRowsUpdated > 0) {
                res = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Cerrar la conexión con la base de datos después de usarla
        return res;
    }

    public List<CS_RV_valores_receta> obtenerIngredientesReceta(Context context, int idReceta) {
        SQLiteDatabase db = this.getConn(context);
        List<CS_RV_valores_receta> listaIngredientesReceta = new ArrayList<>();
        Cursor cursor = null;

        try {
            // Consulta para obtener los ingredientes de la receta con sus imágenes y nombres
            String query = "SELECT ingredientes.id_ingrediente AS id_ingrediente, ingredientes.nombre, ingredientes.imagen, receta_ingredientes.cantidad " +
                    "FROM receta_ingredientes " +
                    "INNER JOIN ingredientes ON receta_ingredientes.id_ingrediente = ingredientes.id_ingrediente " +
                    "WHERE receta_ingredientes.id_receta = ?";

            cursor = db.rawQuery(query, new String[]{String.valueOf(idReceta)});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Obtener los datos de cada ingrediente de la receta
                    @SuppressLint("Range") int idIngrediente = cursor.getInt(cursor.getColumnIndex("id_ingrediente"));
                    @SuppressLint("Range") String nombreIngrediente = cursor.getString(cursor.getColumnIndex("nombre"));
                    @SuppressLint("Range") byte[] imagenIngrediente = cursor.getBlob(cursor.getColumnIndex("imagen"));
                    @SuppressLint("Range") String cantidad = cursor.getString(cursor.getColumnIndex("cantidad"));

                    // Crear un objeto CS_RV_valores_receta con los datos del ingrediente de la receta
                    CS_RV_valores_receta ingredienteReceta = new CS_RV_valores_receta();
                    ingredienteReceta.setId(idIngrediente);
                    ingredienteReceta.setNombre(nombreIngrediente);
                    ingredienteReceta.setImagenBytes(imagenIngrediente);
                    ingredienteReceta.setCantidad(cantidad);

                    listaIngredientesReceta.add(ingredienteReceta);
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

        return listaIngredientesReceta;
    }
    public int eliminarIngredienteDeReceta(Context context, int idReceta, int idIngrediente) {
        int res = 0;
        try (SQLiteDatabase db = this.getConn(context)) {
            // Eliminar el ingrediente de la receta
            int numRowsDeleted = db.delete("receta_ingredientes", "id_receta = ? AND id_ingrediente = ?", new String[]{String.valueOf(idReceta), String.valueOf(idIngrediente)});
            if (numRowsDeleted > 0) {
                res = 1; // Se eliminó correctamente
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return res;
    }
    // Método para verificar si ya existe un ingrediente asociado a una receta
    public boolean existeIngredienteReceta(Context context, int idReceta, int idIngrediente) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        boolean existe = false;

        try {
            db = this.getConn(context);
            if (db != null) {
                cursor = db.rawQuery("SELECT COUNT(*) FROM receta_ingredientes WHERE id_receta = ? AND id_ingrediente = ?",
                        new String[]{String.valueOf(idReceta), String.valueOf(idIngrediente)});
                if (cursor != null && cursor.moveToFirst()) {
                    int count = cursor.getInt(0);
                    existe = count > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return existe;
    }

    // Método para obtener la lista de ingredientes de una receta y construir un String con el formato especificado
    public String obtenerStringIngredientesReceta(Context context, int idReceta) {
        // Obtener la lista de ingredientes de la receta
        List<CS_RV_valores_receta> ingredientesReceta = obtenerIngredientesReceta(context, idReceta);
        // Crear un StringBuilder para construir el String
        StringBuilder stringBuilder = new StringBuilder();
        // Iterar sobre la lista de ingredientes
        for (CS_RV_valores_receta ingrediente : ingredientesReceta) {
            // Agregar el nombre del ingrediente y la cantidad al StringBuilder
            stringBuilder.append(ingrediente.getNombre())
                    .append(" - ")
                    .append(ingrediente.getCantidad())
                    .append(", ");
        }
        // Eliminar la última coma y el espacio
        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 2);
        }
        // Devolver el String resultante
        return stringBuilder.toString();
    }
}
