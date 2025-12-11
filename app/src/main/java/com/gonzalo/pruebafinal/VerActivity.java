package com.gonzalo.pruebafinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class VerActivity extends AppCompatActivity {

    private static final String TAG = "VerActivity";
    private TextView tvNombre, tvPrecio, tvDescripcion;
    private Button btnEditar, btnEliminar;
    private FirebaseFirestore db;
    private String productoId; // Almacenará el ID del documento actual

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver);

        // Inicialización de Firestore
        db = FirebaseFirestore.getInstance();

        // 1. Obtener el ID del producto
        productoId = getIntent().getStringExtra("PRODUCTO_ID");
        if (productoId == null) {
            Toast.makeText(this, "Error: ID de producto no encontrado", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // 2. Inicialización de Vistas
        tvNombre = findViewById(R.id.tvDetalleNombre);
        tvPrecio = findViewById(R.id.tvDetallePrecio);
        tvDescripcion = findViewById(R.id.tvDetalleDescripcion);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);

        // 3. Configuración de Listeners
        btnEditar.setOnClickListener(v -> {
            // Requisito 5: Redirige a EditarActivity
            Intent intent = new Intent(this, EditarActivity.class);
            intent.putExtra("PRODUCTO_ID", productoId);
            startActivity(intent);
        });

        // Requisito 6: Llama al método de eliminación
        btnEliminar.setOnClickListener(v -> eliminarProducto());
    }

    // Asegura que los datos se recarguen al volver de EditarActivity
    @Override
    protected void onResume() {
        super.onResume();
        cargarDetalleProducto(productoId);
    }

    /**
     * Requisito 4: Carga los datos de un documento específico de Firestore
     */
    private void cargarDetalleProducto(String id) {
        db.collection("productos").document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Producto producto = documentSnapshot.toObject(Producto.class);
                        if (producto != null) {
                            // Muestra la información en los TextViews
                            tvNombre.setText("Nombre: " + producto.getNombre());
                            tvPrecio.setText(String.format("Precio: $%.2f", producto.getPrecio()));
                            tvDescripcion.setText("Descripción:\n" + producto.getDescripcion());
                        }
                    } else {
                        Toast.makeText(this, "Producto no encontrado.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al cargar el producto: ", e);
                    Toast.makeText(this, "Error de conexión.", Toast.LENGTH_SHORT).show();
                });
    }

    private void eliminarProducto() {
        db.collection("productos").document(productoId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Producto eliminado con éxito", Toast.LENGTH_LONG).show();

                    // Redireccionar a ListarActivity (Requisito 6)
                    Intent intent = new Intent(this, ListarActivity.class);
                    // Estas flags aseguran que las Activities intermedias (Ver/Editar) se cierren
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish(); // Cierra VerActivity
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al eliminar el producto", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Error deleting document", e);
                });
    }
}