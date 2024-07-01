package com.example.aplicacion_dima;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
public class CS_GestorImagenes {

    private static final int REQUEST_IMAGE_PICK = 1;
    private Uri imageUri;
    private int targetWidth;
    private int targetHeight;
    private OnImageSelectedListener listener;

    public interface OnImageSelectedListener {
        void onImageSelected(byte[] imageData);
    }

    public CS_GestorImagenes(){}

    public CS_GestorImagenes(int targetWidth, int targetHeight) {
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
    }


    @SuppressLint("QueryPermissionsNeeded")
    public void seleccionarImagen(Activity activity, int targetWidth, int targetHeight, OnImageSelectedListener listener) {
        this.listener = listener;
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(intent, REQUEST_IMAGE_PICK);
        } else {
            Toast.makeText(activity, "No hay aplicaciones disponibles para seleccionar una imagen.", Toast.LENGTH_SHORT).show();
        }
    }

    public void mostrarResultado(int requestCode, int resultCode, Intent data, Activity activity) {
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                imageUri = data.getData();
                byte[] imageData = obtenerBytesDeImagenSeleccionada(activity);
                if (imageData != null && listener != null) {
                    listener.onImageSelected(imageData);
                }
            }
        }
    }

    private byte[] obtenerBytesDeImagenSeleccionada(Activity activity) {
        if (imageUri != null) {
            try {
                @SuppressLint("Recycle") InputStream inputStream = activity.getContentResolver().openInputStream(imageUri);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while (true) {
                    assert inputStream != null;
                    if (!((bytesRead = inputStream.read(buffer)) != -1)) break;
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }

                // Decodificar la imagen y escalarla al tamaño deseado
                byte[] imageData = byteArrayOutputStream.toByteArray();
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);

                // Convertir el bitmap escalado a bytes
                ByteArrayOutputStream scaledByteArrayOutputStream = new ByteArrayOutputStream();
                scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, scaledByteArrayOutputStream);
                return scaledByteArrayOutputStream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

        // Método para mostrar la imagen en un ImageView
        public void mostrarImagen(byte[] imageData, ImageView imageView) {
            if (imageData != null) {
                // Decodificar la imagen
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

                // Mostrar la imagen en el ImageView
                imageView.setImageBitmap(bitmap);
            } else {
                // Mostrar un mensaje de error si no se pudo obtener la imagen
            }
        }


}
