package com.example.aplicacion_dima;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CP_NuevaReceta extends AppCompatActivity implements CS_GestorImagenes.OnImageSelectedListener, CS_RV_Interfaz{

    private Button botonCancelar;
    private EditText nombreReceta,descripcionReceta;
    private ImageView fotoReceta;
    private byte[] imagenSeleccionada; // Variable para almacenar la imagen seleccionada
    private CS_GestorImagenes CSGestorImagenes; // Instancia de ImageSelector
    private boolean modificar;
    private int idReceta;
    public ArrayList<CS_RV_valores_receta> ingredientes = new ArrayList<>();

    //Mi Main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cp_activity_nueva_receta);

        // Tool Bar
        new CS_Desplegable_ToolBar(this);

        // Referencias a los elementos del layout
        nombreReceta = findViewById(R.id.nombre_receta_nueva);
        descripcionReceta = findViewById(R.id.descripcion_receta_nueva);
        fotoReceta = findViewById(R.id.foto_receta_nueva);
        Button botonGuardarReceta = findViewById(R.id.boton_guardar_receta_nueva);
        botonCancelar = findViewById(R.id.boton_cancelar_receta_nueva);
        Button botonEliminar = findViewById(R.id.boton_eliminar_receta_nueva);
        botonEliminar.setVisibility(View.GONE);
        Button botonInsertarIngrediente = findViewById(R.id.boton_insertar_ingrediente);

        // Inicializar la instancia de ImageSelector
        CSGestorImagenes = new CS_GestorImagenes();

        // Poner en apagado el indicador de modificación una vez entramos en actividad
        modificar = getIntent().getBooleanExtra("modificar", false);

        // Acción al hacer click en el botón de Cancelar
        botonCancelar.setOnClickListener(V -> {
            startActivity(new Intent(CP_NuevaReceta.this, CP_MisRecetas.class));
            botonCancelar.setVisibility(View.GONE);
        });

        // Botón al hacer click en el boton Añadir Ingrediente a la Receta
        botonInsertarIngrediente.setOnClickListener(v -> {
            int resultado = guardarReceta();

            // Crear un Intent para iniciar la actividad CP_MisIngredientes
            Intent intent = new Intent(CP_NuevaReceta.this, CP_MisIngredientes.class);
            // Pasar el ID de la receta como extra al intent
            intent.putExtra("id_receta", idReceta);
            intent.putExtra("direccionador", CP_MisIngredientes.DESDE_NUEVA_RECETA);
            // Mostrar un mensaje dependiendo del resultado
            if (resultado == 1) {
                // Éxito
                Toast.makeText(CP_NuevaReceta.this, "¡Se ha guardado la receta para añadir ingredientes!", Toast.LENGTH_SHORT).show();
            } else {
                // Error
                Toast.makeText(CP_NuevaReceta.this, "Error al guardar la receta", Toast.LENGTH_SHORT).show();
            }
            // Iniciar la actividad
            startActivity(intent);

        });


        // Obtener los datos pasados como extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            botonEliminar.setVisibility(View.VISIBLE);
            idReceta = extras.getInt("id_receta");
            // Llamar al método mostrarReceta para obtener los datos de la receta
            CS_SQLite_GestorRecetasDB gestorRecetasDB = new CS_SQLite_GestorRecetasDB();
            CS_RV_valores_receta receta = gestorRecetasDB.mostrarReceta(this, idReceta);
            if (receta != null) {
                String nombre = receta.getNombre();
                String descripcion = receta.getDescripcion();
                byte[] imagen = receta.getImagenBytes();

            // Rellenar los campos con los datos recibidos
            nombreReceta.setText(nombre);
            descripcionReceta.setText(descripcion);
            if (imagen != null) {
                // Mostrar la imagen en el ImageView si existe
                fotoReceta.setImageBitmap(BitmapFactory.decodeByteArray(imagen, 0, imagen.length));
                // Guardar la imagen seleccionada
                imagenSeleccionada = imagen;
            }
            }
        }
        else{botonCancelar.setVisibility(View.VISIBLE);}

    // Acción al hacer clic en el botón de Eliminar
            botonEliminar.setOnClickListener(v -> {
                // Mostrar alerta de confirmación
                AlertDialog.Builder builder = new AlertDialog.Builder(CP_NuevaReceta.this);
                builder.setMessage("¿Estás seguro o segura de que deseas eliminar esta receta? Se va a eliminar de forma permanente.")
                        .setCancelable(false)
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Llamar al método para eliminar la receta de la base de datos
                                CS_SQLite_GestorRecetasDB db = new CS_SQLite_GestorRecetasDB();
                                int resultado = db.eliminarReceta(CP_NuevaReceta.this, idReceta);
                                // Mostrar un mensaje dependiendo del resultado
                                if (resultado == 1) {
                                    // Éxito
                                    Toast.makeText(getApplicationContext(), "¡Receta eliminada correctamente!", Toast.LENGTH_SHORT).show();
                                    // Ir a la actividad CP_MisRecetas
                                    Intent intent = new Intent(CP_NuevaReceta.this, CP_MisRecetas.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Error
                                    Toast.makeText(CP_NuevaReceta.this, "Error al eliminar la receta", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Si el usuario hace clic en "No", se cancela la acción
                                dialog.cancel();
                            }
                        });
                // Crear y mostrar la alerta
                AlertDialog alert = builder.create();
                alert.show();
            });


        // Acción al hacer clic en el botón de guardar
        botonGuardarReceta.setOnClickListener(v -> {
            int resultado = guardarReceta();

            // Mostrar un mensaje dependiendo del resultado
            if (resultado == 1) {
                // Éxito
                // Ir a la actividad CP_MisIngredientes
                Intent intent = new Intent(CP_NuevaReceta.this, CP_MisRecetas.class);
                startActivity(intent);
            } else {
                // Error
                Toast.makeText(CP_NuevaReceta.this, "Error al guardar la receta", Toast.LENGTH_SHORT).show();
            }
        });
    // Acción al hacer clic en la imagen para seleccionar una nueva imagen
        fotoReceta.setOnClickListener(v -> {
            // Lógica para seleccionar una imagen
            CSGestorImagenes.seleccionarImagen(CP_NuevaReceta.this, fotoReceta.getWidth(), fotoReceta.getHeight(), CP_NuevaReceta.this);
        });

        //Recycler View
        RecyclerView recyclerView = findViewById (R.id.mRecyclerView);
        mostrarIngredientes();

        CS_RV_adaptador_recetas adapter = new CS_RV_adaptador_recetas(this, ingredientes,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //Recycler View
    private void mostrarIngredientes(){
        ArrayList <Integer> id_rv=new ArrayList<>();
        ArrayList <String> nombres_rv=new ArrayList<>();
        ArrayList <String> cantidad_rv=new ArrayList<>();
        ArrayList <byte[]> imagen_rv=new ArrayList<>();

        CS_SQLite_GestorRI_DB CSGestorRI = new CS_SQLite_GestorRI_DB();
        List<CS_RV_valores_receta> listaIngredientes = CSGestorRI.obtenerIngredientesReceta(this,idReceta);

        if (listaIngredientes != null && !listaIngredientes.isEmpty()) {
            for (CS_RV_valores_receta ingrediente : listaIngredientes) {
                // Crear un ListElement con los datos del ingrediente
                int id = ingrediente.getId();
                id_rv.add(id);
                byte[] imagenBytes = ingrediente.getImagenBytes(); // Obtener los bytes de la imagen
                imagen_rv.add(imagenBytes);
                String nombre = ingrediente.getNombre();
                nombres_rv.add(nombre);
                String descripcion = ingrediente.getCantidad();
                cantidad_rv.add(descripcion);
            }
        }
        for(int i=0; i<nombres_rv.size();i++){
            ingredientes.add(new CS_RV_valores_receta(id_rv.get(i),nombres_rv.get(i),cantidad_rv.get(i),imagen_rv.get(i)));
        }
    }

    // Guardar Receta
    private int guardarReceta() {
        // Obtener los datos ingresados por el usuario
        String nombre = nombreReceta.getText().toString();
        String descripcion = descripcionReceta.getText().toString();

        // Crear un objeto CS_RV_valores_ingredientes con los datos
        CS_RV_valores_receta receta = new CS_RV_valores_receta();
        receta.setId(idReceta);  // Establecer el ID del receta
        receta.setNombre(nombre);
        receta.setDescripcion(descripcion);
        receta.setImagenBytes(imagenSeleccionada);

        // Llamar al método para guardar o modificar el receta en la base de datos
        CS_SQLite_GestorRecetasDB db = new CS_SQLite_GestorRecetasDB();
        int resultado;
        if (modificar) {
            resultado = db.modificarReceta(CP_NuevaReceta.this, receta);
        } else {
            resultado = db.insertaReceta(CP_NuevaReceta.this, receta);
            idReceta=receta.getId();

        }
        finish();
        return resultado;
    }

    @Override
    public void onItemClick(int position) {
        // Obtener el elemento seleccionado
        CS_RV_valores_receta selectedElement = ingredientes.get(position);

        // Crear un Intent para iniciar la actividad CP_NuevoIngrediente
        Intent intent = new Intent(CP_NuevaReceta.this, CP_NuevaReceta_Ingrediente.class);

        // Pasar los datos del ingrediente seleccionado como extras
        intent.putExtra("id_receta",idReceta);
        intent.putExtra("id_ingrediente",selectedElement.getId());
        intent.putExtra("cantidad", selectedElement.getDescripcion());
        intent.putExtra("seleccionador",true);

        // Iniciar la actividad
        startActivity(intent);
    }

    // Método para manejar la imagen seleccionada
    @Override
    public void onImageSelected(byte[] imageData) {
        // Guardar la imagen seleccionada
        imagenSeleccionada = Arrays.copyOf(imageData, imageData.length);

        // Mostrar la imagen en el ImageView
        fotoReceta.setImageBitmap(BitmapFactory.decodeByteArray(imageData, 0, imageData.length));
    }

    // Método para manejar el resultado de la selección de la imagen
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Llama al método mostrarResultado de la instancia de ImageSelector
        CSGestorImagenes.mostrarResultado(requestCode, resultCode, data, this);
    }

    //Botón Volver
    @Override
    public void onBackPressed() {
        // Creamos un Intent para iniciar la actividad deseada
        Intent intent = new Intent(this, CP_MisRecetas.class);
        startActivity(intent);

        // Finalizamos esta actividad
        finish();
    }

}