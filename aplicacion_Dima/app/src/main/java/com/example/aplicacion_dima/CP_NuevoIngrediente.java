package com.example.aplicacion_dima;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class CP_NuevoIngrediente extends AppCompatActivity implements CS_GestorImagenes.OnImageSelectedListener {
    private EditText nombreIngrediente, descripcionIngrediente;
    private Button botonCancelar;
    private ImageView fotoIngredienteNuevo;
    private byte[] imagenSeleccionada; // Variable para almacenar la imagen seleccionada
    private CS_GestorImagenes CSGestorImagenes; // Instancia de ImageSelector
    private boolean modificar,seleccionador;
    private int id,idReceta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cp_activity_nuevo_ingrediente);
        seleccionador=false;
        // Tool Bar
        new CS_Desplegable_ToolBar(this);

        // Referencias a los elementos del layout
        nombreIngrediente = findViewById(R.id.nombre_ingrediente_nuevo);
        descripcionIngrediente = findViewById(R.id.descripcion_ingrediente_nuevo);
        fotoIngredienteNuevo = findViewById(R.id.foto_ingrediente_nuevo);
        Button botonGuardarIngrediente = findViewById(R.id.boton_guardar_nuevo_ingrediente);
        botonCancelar = findViewById(R.id.boton_cancelar_nuevo_ingrediente);
        Button botonEliminar = findViewById(R.id.boton_eliminar_nuevo_ingrediente);
        botonEliminar.setVisibility(View.GONE);




        // Inicializar la instancia de ImageSelector
        CSGestorImagenes = new CS_GestorImagenes();
        // Poner en apagado el indicador de modificación una vez entramos en actividad
        modificar = getIntent().getBooleanExtra("modificar", false);

        // Acción al hacer click en el botón de Cancelar
        botonCancelar.setOnClickListener(V -> {
            startActivity(new Intent(CP_NuevoIngrediente.this, CP_MisIngredientes.class));
            botonCancelar.setVisibility(View.GONE);
        });

        // Obtener los datos pasados como extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            botonEliminar.setVisibility(View.VISIBLE);
            id = extras.getInt("id_ingrediente");
            // Mostrar un Toast con el ID del ingrediente recibido
            String nombre = extras.getString("nombre", "");
            String descripcion = extras.getString("descripcion", "");
            byte[] imagen = extras.getByteArray("imagen");

            // Rellenar los campos con los datos recibidos
            nombreIngrediente.setText(nombre);
            descripcionIngrediente.setText(descripcion);
            if (imagen != null) {
                // Mostrar la imagen en el ImageView si existe
                fotoIngredienteNuevo.setImageBitmap(BitmapFactory.decodeByteArray(imagen, 0, imagen.length));
                // Guardar la imagen seleccionada
                imagenSeleccionada = imagen;
            }
        }
        else{botonCancelar.setVisibility(View.VISIBLE);}

        // Acción al hacer click en el botón de Eliminar
        botonEliminar.setOnClickListener(v -> {
            // Llamar al método para guardar o modificar el ingrediente en la base de datos
            CS_SQLite_GestorIngredientesDB db = new CS_SQLite_GestorIngredientesDB();
            int resultado;
            resultado = db.eliminarIngrediente(CP_NuevoIngrediente.this, id);
            // Mostrar un mensaje dependiendo del resultado
                Toast.makeText(getApplicationContext(), "¡Ingrediente Eliminado Correctamente!", Toast.LENGTH_SHORT).show();
                // Ir a la actividad CP_MisIngredientes
                Intent intentEliminar = new Intent(CP_NuevoIngrediente.this, CP_MisIngredientes.class);
                startActivity(intentEliminar);
                finish();

        });

        // Acción al hacer clic en el botón de guardar
        botonGuardarIngrediente.setOnClickListener(v -> {
            // Obtener los datos ingresados por el usuariod =
            String nombre = nombreIngrediente.getText().toString();
            String descripcion = descripcionIngrediente.getText().toString();

            // Crear un objeto CS_RV_valores_ingredientes con los datos
            CS_RV_valores_ingredientes ingrediente = new CS_RV_valores_ingredientes();
            ingrediente.setId(id);  // Establecer el ID del ingrediente
            // Mostrar un Toast con el ID del ingrediente recibido
            ingrediente.setNombre(nombre);
            ingrediente.setDescripcion(descripcion);
            ingrediente.setImagenBytes(imagenSeleccionada);

            // Llamar al método para guardar o modificar el ingrediente en la base de datos
            CS_SQLite_GestorIngredientesDB db = new CS_SQLite_GestorIngredientesDB();
            int resultado;
            if (modificar) {
                resultado = db.modificarIngrediente(CP_NuevoIngrediente.this, ingrediente);
            } else {
                resultado = db.insertaIngrediente(CP_NuevoIngrediente.this, ingrediente);
            }

            // Mostrar un mensaje dependiendo del resultado
            if (resultado == 1) {
                // Éxito
                Toast.makeText(CP_NuevoIngrediente.this, "¡Ingrediente guardado correctamente!", Toast.LENGTH_SHORT).show();
                // Ir a la actividad CP_MisIngredientes o Nueva Receta
                Intent intent = getIntent();
                idReceta = intent.getIntExtra("id_receta", -1);
                seleccionador = intent.getBooleanExtra("seleccionador",false);
                Intent intentGuardar = new Intent(CP_NuevoIngrediente.this, CP_MisIngredientes.class);
                if(seleccionador){
                    // Elementos transferidos desde MisIngredientes
                    intentGuardar.putExtra("id_receta",idReceta);
                    intentGuardar.putExtra("direccionador", CP_MisIngredientes.DESDE_NUEVA_RECETA);

                }
                startActivity(intentGuardar);

                finish();
            } else {
                // Error
                Toast.makeText(CP_NuevoIngrediente.this, "Error al guardar el ingrediente", Toast.LENGTH_SHORT).show();
            }
        });
        // Acción al hacer clic en la imagen para seleccionar una nueva imagen
        fotoIngredienteNuevo.setOnClickListener(v -> {
            // Lógica para seleccionar una imagen
            CSGestorImagenes.seleccionarImagen(CP_NuevoIngrediente.this, fotoIngredienteNuevo.getWidth(), fotoIngredienteNuevo.getHeight(), CP_NuevoIngrediente.this);
        });
        // Acción al hacer clic en la imagen para seleccionar una nueva imagen
        fotoIngredienteNuevo.setOnClickListener(v -> {
            // Lógica para seleccionar una imagen
            CSGestorImagenes.seleccionarImagen(CP_NuevoIngrediente.this, fotoIngredienteNuevo.getWidth(), fotoIngredienteNuevo.getHeight(), CP_NuevoIngrediente.this);
        });
    }

    // Método para manejar la imagen seleccionada
    @Override
    public void onImageSelected(byte[] imageData) {
        // Guardar la imagen seleccionada
        imagenSeleccionada = Arrays.copyOf(imageData, imageData.length);

        // Mostrar la imagen en el ImageView
        fotoIngredienteNuevo.setImageBitmap(BitmapFactory.decodeByteArray(imageData, 0, imageData.length));
    }

    // Método para manejar el resultado de la selección de la imagen
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Llama al método mostrarResultado de la instancia de ImageSelector
        CSGestorImagenes.mostrarResultado(requestCode, resultCode, data, this);
    }
}