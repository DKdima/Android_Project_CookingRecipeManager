package com.example.aplicacion_dima;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

public class CS_Desplegable_ToolBar {

    public CS_Desplegable_ToolBar(Activity activity) {
        ImageView iconoAtras = activity.findViewById(R.id.icono_atras);
        TextView atras = activity.findViewById(R.id.atras);
        ImageView iconoMenu = activity.findViewById(R.id.icono_menu);

        // Configurar el comportamiento del botón de atrás
        iconoAtras.setOnClickListener(v -> activity.onBackPressed());
        atras.setOnClickListener(v -> activity.onBackPressed());

        // Mostrar el botón de atrás
        iconoAtras.setVisibility(View.VISIBLE);
        atras.setVisibility(View.VISIBLE);

        // Configurar el comportamiento del botón de menú
        iconoMenu.setOnClickListener(v -> CS_Desplegable_ToolBar.showPopupMenu(activity, v, getCurrentActivityId(activity)));
    }

    public static void showPopupMenu(Context context, View anchorView, int currentActivityId) {
        PopupMenu popupMenu = new PopupMenu(context, anchorView);
        popupMenu.getMenuInflater().inflate(R.menu.menu_desplegable, popupMenu.getMenu());

        // Ocultar el elemento del menú correspondiente a la actividad actual
        hideMenuItem(popupMenu.getMenu(), currentActivityId);

        popupMenu.setOnMenuItemClickListener(item -> {
            // Obtiene el ID del elemento seleccionado
            int itemId = item.getItemId();

            // Comprueba el ID y maneja la acción correspondiente
            if (itemId == R.id.opcion1) {
                context.startActivity(new Intent(context, CP_HOME.class));
                return true;
            } else if (itemId == R.id.opcion2) {
                context.startActivity(new Intent(context, CP_MisRecetas.class));
                return true;
            } else if (itemId == R.id.opcion3) {
                context.startActivity(new Intent(context, CP_MisIngredientes.class));
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }

    private static int getCurrentActivityId(Activity activity) {
        // Obtener el nombre de la actividad actual
        String currentActivityName = activity.getClass().getSimpleName();

        // Comparar el nombre de la actividad actual con los nombres de las actividades en tu menú desplegable
        if (currentActivityName.equals("CP_HOME")) {
            return R.id.opcion1;
        } else if (currentActivityName.equals("CP_MisRecetas")) {
            return R.id.opcion2;
        } else if (currentActivityName.equals("CP_MisIngredientes")) {
            return R.id.opcion3;
        } else {
            // Si no coincide con ninguna actividad conocida, devolver un valor predeterminado
            return R.id.opcion1; // Puedes cambiar esto según tu preferencia
        }
    }

    private static void hideMenuItem(Menu menu, int menuItemId) {
        MenuItem item = menu.findItem(menuItemId);
        if (item != null) {
            item.setVisible(false);
        }
    }
}
