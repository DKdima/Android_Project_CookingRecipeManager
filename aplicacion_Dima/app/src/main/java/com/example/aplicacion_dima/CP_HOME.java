package com.example.aplicacion_dima;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CP_HOME extends AppCompatActivity {


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cp_activity_home);

        //On Clik Botones
        // Variables
        Button misRecetasButton = findViewById(R.id.botonMisRecetas);
        Button ingredientesButton = findViewById(R.id.botonMisIngredientes);
        misRecetasButton.setOnClickListener(v -> {
            startActivity(new Intent(CP_HOME.this, CP_MisRecetas.class));
            finish(); // Finaliza la actividad actual
        });

        ingredientesButton.setOnClickListener(v -> {
            startActivity(new Intent(CP_HOME.this, CP_MisIngredientes.class));
            finish(); // Finaliza la actividad actual
        });

        //tool Bar
        new CS_Desplegable_ToolBar(this);
        // Obtener referencias a los elementos de tu layout
        ImageView iconoAtras = findViewById(R.id.icono_atras);
        TextView atras = findViewById(R.id.atras);
        // Ocultar los elementos
        iconoAtras.setVisibility(View.GONE);
        atras.setVisibility(View.GONE);

        // Text view Cálculo de días restantes  (Para indicar en rojo los días restentes para lanzar la app)
       /*TextView textViewDaysLeft = findViewById(R.id.dias);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            long diasFaltantes = CS_DiasRestantes.calcularDiasFaltantes();
            textViewDaysLeft.setText("Días restantes para 01/06/2024: " + diasFaltantes);
        }*/
    }
    //Botón Volver
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
    }
}
