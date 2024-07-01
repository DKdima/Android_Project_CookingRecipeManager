package com.example.aplicacion_dima;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CP_NuevaReceta_Ingrediente extends AppCompatActivity {
    int idReceta, idIngrediente;
    Button botonGuardar,botonCancelar,botonEliminar;
    EditText cantidadIngrediente;
    ImageView fotoIngrediente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cp_activity_nueva_receta_ingrediente);
        boolean seleccionador = false;
        fotoIngrediente = findViewById(R.id.foto_ingrediente_nuevo_ii);
        TextView nombreIngrediente = findViewById(R.id.nombre_ingrediente_nuevo_ii);
        cantidadIngrediente = findViewById(R.id.cantidad_ingrediente_nuevo_ii);
        botonGuardar = findViewById(R.id.boton_guardar_nuevo_ingrediente_ii);
        botonCancelar = findViewById(R.id.boton_cancelar_nuevo_ingrediente_ii);
        botonEliminar = findViewById(R.id.boton_eliminar_nuevo_ingrediente_ii);
        botonCancelar.setVisibility(View.GONE);
        botonEliminar.setVisibility(View.GONE);

        // Tool Bar
        new CS_Desplegable_ToolBar(this);

        // Elementos transferidos desde MisIngredientes
        Intent intent = getIntent();
        idReceta = intent.getIntExtra("id_receta", -1);
        idIngrediente = intent.getIntExtra("id_ingrediente", -1);
        cantidadIngrediente.setText(intent.getStringExtra("cantidad"));
        seleccionador = intent.getBooleanExtra("seleccionador", false);



        // Obtener el nombre del ingrediente por su ID y mostrarlo en el TextView correspondiente
        obtenerYMostrarIngrediente(idIngrediente, nombreIngrediente, fotoIngrediente);

        if(seleccionador) {
            botonEliminar.setVisibility(View.VISIBLE);
            botonEliminar.setOnClickListener (v -> {
                CS_SQLite_GestorRI_DB db = new CS_SQLite_GestorRI_DB();
                int resultado = db.eliminarIngredienteDeReceta(CP_NuevaReceta_Ingrediente.this, idReceta, idIngrediente);
                if (resultado == 1) {
                    // La eliminación fue exitosa
                    Toast.makeText(CP_NuevaReceta_Ingrediente.this, "Ingrediente eliminado de la receta", Toast.LENGTH_SHORT).show();
                    Intent eliminarIntent = new Intent(CP_NuevaReceta_Ingrediente.this, CP_NuevaReceta.class);
                    eliminarIntent.putExtra("id_receta", idReceta);
                    eliminarIntent.putExtra("modificar", true);
                    startActivity(eliminarIntent); // Iniciar la nueva actividad
                    finish();
                } else {
                    // La eliminación falló
                    Toast.makeText(CP_NuevaReceta_Ingrediente.this, "Error al eliminar ingrediente", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            botonCancelar.setVisibility(View.VISIBLE);
            //Botón Cancelar
            botonCancelar.setOnClickListener(v -> {
                // Crear un Intent para volver a la actividad Mis_Ingredientes y pasar el idReceta como extra
                Intent cancelIntent = new Intent(CP_NuevaReceta_Ingrediente.this, CP_MisIngredientes.class);
                cancelIntent.putExtra("id_receta", idReceta);
                cancelIntent.putExtra("direccionador", CP_MisIngredientes.DESDE_NUEVA_RECETA);
                startActivity(cancelIntent); // Iniciar la nueva actividad
                finish();
            });
        }
        botonGuardar.setOnClickListener(v -> {
            // Obtener la cantidad del ingrediente del EditText
            String cantidad = cantidadIngrediente.getText().toString();

            // Crear una instancia del GestorRI
            CS_SQLite_GestorRI_DB gestorRI = new CS_SQLite_GestorRI_DB();

            // Verificar si ya existe un ingrediente con esa receta
            boolean ingredienteExistente = gestorRI.existeIngredienteReceta(CP_NuevaReceta_Ingrediente.this, idReceta, idIngrediente);

            // Si el ingrediente ya existe, actualizar la cantidad; de lo contrario, insertar un nuevo ingrediente
            int resultado;
            if (ingredienteExistente) {
                resultado = gestorRI.actualizarCantidadRecetaIngrediente(CP_NuevaReceta_Ingrediente.this, idReceta, idIngrediente, cantidad);
                finish();
            } else {
                resultado = gestorRI.insertarRecetaIngrediente(CP_NuevaReceta_Ingrediente.this, idReceta, idIngrediente, cantidad);
                finish();
            }

            // Mostrar un mensaje dependiendo del resultado
            if (resultado == 1) {
                // Mostrar un mensaje de éxito
                Toast.makeText(CP_NuevaReceta_Ingrediente.this, "Ingrediente añadido o actualizado correctamente", Toast.LENGTH_SHORT).show();

                // Crear un Intent para volver a la actividad CP_NuevaReceta y pasar el idReceta como extra
                Intent intent1 = new Intent(CP_NuevaReceta_Ingrediente.this, CP_NuevaReceta.class);
                intent1.putExtra("id_receta", idReceta);
                // Pasar un indicador para identificar si se está modificando un ingrediente existente
                intent1.putExtra("modificar", true);
                // Iniciar la actividad CP_NuevaReceta
                startActivity(intent1);
                finish();
            } else {
                // Mostrar un mensaje de error si la inserción o actualización falló
                Toast.makeText(CP_NuevaReceta_Ingrediente.this, "Error al añadir o actualizar el ingrediente en la receta", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Obtener y mostrar el nombre y la imagen del ingrediente por su ID
    @SuppressLint("SetTextI18n")
    private void obtenerYMostrarIngrediente(int idIngrediente, TextView textView, ImageView imageView) {
        CS_SQLite_GestorIngredientesDB gestorIngredientesDB = new CS_SQLite_GestorIngredientesDB();
        CS_RV_valores_ingredientes ingrediente = gestorIngredientesDB.obtenerIngredientePorId(this, idIngrediente);

        if (ingrediente != null) {
            // Mostrar el nombre del ingrediente en el TextView
            textView.setText(ingrediente.getNombre());

            // Crear una instancia de CS_GestorImagenes con el tamaño deseado
            CS_GestorImagenes gestorImagenes = new CS_GestorImagenes(100, 100);

            // Obtener y mostrar la imagen del ingrediente en el ImageView
            mostrarImagenDeIngrediente(ingrediente.getImagenBytes(), gestorImagenes, imageView);
        } else {
            // Mostrar un mensaje de error si no se pudo obtener el ingrediente
            textView.setText("Error al obtener el ingrediente");
        }
    }


    // Método para mostrar la imagen del ingrediente en el ImageView
    private void mostrarImagenDeIngrediente(byte[] imageData, CS_GestorImagenes gestorImagenes, ImageView imageView) {
        // Mostrar la imagen en el ImageView
        gestorImagenes.mostrarImagen(imageData, imageView);
    }

    //Botón Volver
    @Override
    public void onBackPressed() {
        // Creamos un Intent para iniciar la actividad deseada
        Intent intent = new Intent(this, CP_MisIngredientes.class);
        intent.putExtra("id_receta", idReceta);
        // Pasar un indicador para identificar si se está modificando un ingrediente existente
        intent.putExtra("direccionador", CP_MisIngredientes.DESDE_NUEVA_RECETA);
        startActivity(intent);
        // Finalizamos esta actividad
        finish();
    }

}
