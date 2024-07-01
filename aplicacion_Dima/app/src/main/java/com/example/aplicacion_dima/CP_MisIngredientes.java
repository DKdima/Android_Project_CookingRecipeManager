package com.example.aplicacion_dima;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CP_MisIngredientes extends AppCompatActivity implements CS_RV_Interfaz {

    public ArrayList<CS_RV_valores_ingredientes> ingredientes = new ArrayList<>();
    public static final int DESDE_NUEVA_RECETA = 1;
    public static final int DESDE_OTRA_ACTIVIDAD = 2;
    private int direccionador = DESDE_OTRA_ACTIVIDAD;
    private int idReceta;

    //Mi Main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cp_activity_mis_ingredientes);

        // Obtener el ID de la receta si está presente en los extras
        if (getIntent().hasExtra("id_receta")) {
            idReceta = getIntent().getIntExtra("id_receta", -1);
            direccionador = getIntent().getIntExtra("direccionador", DESDE_OTRA_ACTIVIDAD);
        }
        //Tool Bar
        new CS_Desplegable_ToolBar(this);

        //On Click Botones
        Button nuevoIngrediente = findViewById(R.id.botonNuevoIngrediente);
        nuevoIngrediente.setOnClickListener(v -> {
            Intent intent;
            intent = new Intent(CP_MisIngredientes.this, CP_NuevoIngrediente.class);
            if (direccionador == DESDE_NUEVA_RECETA) {
                intent.putExtra("id_receta",idReceta);
                intent.putExtra("seleccionador",true);
            }
            // Iniciar la actividad
            startActivity(intent);
        });
        RecyclerView recyclerView = findViewById(R.id.mRecyclerView);
        mostrarIngredientes();

        CS_RV_adaptador_ingredientes adapter = new CS_RV_adaptador_ingredientes(this, ingredientes, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //Valores Recicle View
    private void mostrarIngredientes() {
        ArrayList<Integer> idRV = new ArrayList<>();
        ArrayList<String> nombresRV = new ArrayList<>();
        ArrayList<String> descripcionesRV = new ArrayList<>();
        ArrayList<byte[]> imagenRV = new ArrayList<>();

        CS_SQLite_GestorIngredientesDB CSGestorIngredientesDB = new CS_SQLite_GestorIngredientesDB();
        List<CS_RV_valores_ingredientes> listaIngredientes = CSGestorIngredientesDB.obtenerTodosIngredientes(this);

        if (listaIngredientes != null && !listaIngredientes.isEmpty()) {
            for (CS_RV_valores_ingredientes ingrediente : listaIngredientes) {
                int id = ingrediente.getId();
                idRV.add(id);
                byte[] imagenBytes = ingrediente.getImagenBytes();
                imagenRV.add(imagenBytes);
                String nombre = ingrediente.getNombre();
                nombresRV.add(nombre);
                String descripcion = ingrediente.getDescripcion();
                descripcionesRV.add(descripcion);
            }
        }

        for (int i = 0; i < nombresRV.size(); i++) {
            ingredientes.add(new CS_RV_valores_ingredientes(idRV.get(i), nombresRV.get(i), descripcionesRV.get(i), imagenRV.get(i)));
        }
    }

    @Override
    public void onItemClick(int position) {
        CS_RV_valores_ingredientes selectedElement = ingredientes.get(position);
        Intent intent;

        if (direccionador == DESDE_NUEVA_RECETA) {
            // Si viene desde NuevaReceta, vamos a CP_NuevaReceta_Ingrediente
            intent = new Intent(CP_MisIngredientes.this, CP_NuevaReceta_Ingrediente.class);
            intent.putExtra("id_receta", idReceta);
            intent.putExtra("id_ingrediente", selectedElement.getId());
            intent.putExtra("modificar", true);
            finish();
        } else {
            // Si viene desde otra actividad, vamos a CP_NuevoIngrediente
            intent = new Intent(CP_MisIngredientes.this, CP_NuevoIngrediente.class);
            intent.putExtra("id_ingrediente", selectedElement.getId());
            intent.putExtra("modificar", true);

        }

      // Pasar los datos del ingrediente seleccionado como extras
        intent.putExtra("nombre", selectedElement.getNombre());
        intent.putExtra("descripcion", selectedElement.getDescripcion());
        intent.putExtra("imagen", selectedElement.getImagenBytes());


        // Iniciar la actividad
        startActivity(intent);
    }
    //Botón Volver
    @Override
    public void onBackPressed() {
        // Creamos un Intent para iniciar la actividad deseada

        if (direccionador == DESDE_NUEVA_RECETA) {
            Intent intent = new Intent(this, CP_NuevaReceta.class);
            intent.putExtra("id_receta", idReceta);
            intent.putExtra("modificar", true);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, CP_HOME.class);
            startActivity(intent);
        }
        // Finalizamos esta actividad
        finish();
    }
}
