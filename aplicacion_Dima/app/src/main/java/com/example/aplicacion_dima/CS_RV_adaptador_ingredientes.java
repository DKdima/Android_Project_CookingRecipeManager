package com.example.aplicacion_dima;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CS_RV_adaptador_ingredientes extends RecyclerView.Adapter<CS_RV_adaptador_ingredientes.MyViewHolder> {
    private final CS_RV_Interfaz CSRecyclerViewInterfaz;
    Context context;
    ArrayList<CS_RV_valores_ingredientes> adaptador_valoresRV;

    public CS_RV_adaptador_ingredientes(Context context, ArrayList<CS_RV_valores_ingredientes> adaptador_valoresRV, CS_RV_Interfaz CSRecyclerViewInterfaz){
        this.context = context;
        this.adaptador_valoresRV = adaptador_valoresRV;
        this.CSRecyclerViewInterfaz = CSRecyclerViewInterfaz;
    }

    @NonNull
    @Override
    public CS_RV_adaptador_ingredientes.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.cs_recycler_view_formato,parent, false);
        return new CS_RV_adaptador_ingredientes.MyViewHolder(view, CSRecyclerViewInterfaz);
    }

    @Override
    public void onBindViewHolder(@NonNull CS_RV_adaptador_ingredientes.MyViewHolder holder, int position) {
        CS_RV_valores_ingredientes ingrediente = adaptador_valoresRV.get(position);

        holder.tvName.setText(ingrediente.getNombre());
        holder.tv3Letter.setText(ingrediente.getDescripcion());
        byte[] imagenBytes = ingrediente.getImagenBytes();
        if (imagenBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length);
            holder.imageview.setImageBitmap(bitmap);
        } else {
            // Si la imagen es nula, establece una imagen de serie
            holder.imageview.setImageResource(R.drawable.baseline_image_search_24);
        }
    }

    @Override
    public int getItemCount() {
        return adaptador_valoresRV.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageview;
        TextView tvName, tv3Letter;

        public MyViewHolder(@NonNull View itemView, CS_RV_Interfaz CSRecyclerViewInterface){
            super(itemView);

            imageview = itemView.findViewById(R.id.icono_rv);
            tvName = itemView.findViewById(R.id.nombre_rv);
            tv3Letter = itemView.findViewById(R.id.descripcion_rv);

            itemView.setOnClickListener(view -> {
                if(CSRecyclerViewInterface !=null){
                    int pos = getAbsoluteAdapterPosition();

                    if(pos != RecyclerView.NO_POSITION){
                        CSRecyclerViewInterface.onItemClick(pos);
                    }
                }
            });
        }
    }
}

