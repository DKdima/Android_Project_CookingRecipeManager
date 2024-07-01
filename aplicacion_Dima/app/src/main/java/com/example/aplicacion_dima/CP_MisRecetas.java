package com.example.aplicacion_dima;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class CP_MisRecetas extends AppCompatActivity implements CS_RV_Interfaz{

    public ArrayList<CS_RV_valores_receta> recetas = new ArrayList<>();


    //Mi Main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cp_activity_mis_recetas);

        //On Clik Botones
        //Variables
        Button nuevaReceta = findViewById(R.id.botonNuevaReceta);
        nuevaReceta.setOnClickListener(v -> startActivity(new Intent(CP_MisRecetas.this, CP_NuevaReceta.class)));

        //tool Bar
        new CS_Desplegable_ToolBar(this);

        //Recycler View
        RecyclerView recyclerView = findViewById (R.id.mRecyclerView);
        mostrarRecetas();

        CS_RV_adaptador_recetas adapter = new CS_RV_adaptador_recetas(this,recetas,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    //Valores Recicle View
    private void mostrarRecetas(){
        ArrayList <Integer> id_rv=new ArrayList<>();
        ArrayList <String> nombres_rv=new ArrayList<>();
        ArrayList <String> descripciones_rv=new ArrayList<>();
        ArrayList <byte[]> imagen_rv=new ArrayList<>();

        CS_SQLite_GestorRecetasDB CSGestorRecetassDB = new CS_SQLite_GestorRecetasDB();
        List<CS_RV_valores_receta> listaRecetas = CSGestorRecetassDB.obtenerTodasRecetas(this);

        if (listaRecetas != null && !listaRecetas.isEmpty()) {
            for (CS_RV_valores_receta receta : listaRecetas) {
                // Crear un ListElement con los datos del ingrediente
                int id = receta.getId();
                id_rv.add(id);
                byte[] imagenBytes = receta.getImagenBytes(); // Obtener los bytes de la imagen
                imagen_rv.add(imagenBytes);
                String nombre = receta.getNombre();
                nombres_rv.add(nombre);
                String descripcion = new CS_SQLite_GestorRI_DB().obtenerStringIngredientesReceta(this, id);
                descripciones_rv.add(descripcion);
            }
        }
        for(int i=0; i<nombres_rv.size();i++){
            recetas.add(new CS_RV_valores_receta(id_rv.get(i),nombres_rv.get(i), descripciones_rv.get(i), imagen_rv.get(i)));
        }
    }
    @Override
    public void onItemClick(int position) {
        // Obtener el elemento seleccionado
        CS_RV_valores_receta selectedElement = recetas.get(position);

        // Crear un Intent para iniciar la actividad CP_NuevoIngrediente
        Intent intent = new Intent(CP_MisRecetas.this, CP_NuevaReceta.class);

        // Pasar los datos del ingrediente seleccionado como extras
        intent.putExtra("id_receta",selectedElement.getId());
        intent.putExtra("nombre", selectedElement.getNombre());
        intent.putExtra("descripcion", selectedElement.getDescripcion());
        intent.putExtra("imagen", selectedElement.getImagenBytes());

        // Pasar un indicador para identificar si se está modificando un ingrediente existente
        intent.putExtra("modificar", true);

        // Iniciar la actividad
        startActivity(intent);
    }

 //Botón Volver
    @Override
    public void onBackPressed() {
        // Creamos un Intent para iniciar la actividad deseada
        Intent intent = new Intent(this, CP_HOME.class);
        startActivity(intent);

        // Finalizamos esta actividad
        finish();
    }
}