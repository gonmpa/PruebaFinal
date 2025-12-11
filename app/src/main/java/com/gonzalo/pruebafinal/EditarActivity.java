package com.gonzalo.pruebafinal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditarActivity extends AppCompatActivity {

    private EditText editNombre, editPrecio, editDescripcion;
    private Button btnGuardarCambios;
    private FirebaseFirestore db;
    private String productoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar); // Usamos el mismo layout

        db = FirebaseFirestore.getInstance();

        // Obtener el ID
        productoId = getIntent().getStringExtra("PRODUCTO_ID");

        // Inicializar Vistas (Mismos IDs que AgregarActivity)
        editNombre = findViewById(R.id.editNombre);
        editPrecio = findViewById(R.id.editPrecio);
        editDescripcion = findViewById(R.id.editDescripcion);
        btnGuardarCambios = findViewById(R.id.btnGuardar);

        btnGuardarCambios.setText("GUARDAR CAMBIOS"); // Cambiamos el texto del botón

        // 1. Cargar datos existentes
        cargarDatosParaEdicion(productoId);

        // 2. Configurar Listener
        btnGuardarCambios.setOnClickListener(view -> actualizarProducto());
    }

    private void cargarDatosParaEdicion(String id) {
        db.collection("productos").document(id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Producto producto = documentSnapshot.toObject(Producto.class);
                        if (producto != null) {
                            editNombre.setText(producto.getNombre());
                            editPrecio.setText(String.valueOf(producto.getPrecio()));
                            editDescripcion.setText(producto.getDescripcion());
                        }
                    } else {
                        Toast.makeText(this, "Producto para editar no encontrado.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Requisito 5: Actualiza el documento en Firestore usando el ID.
     */
    private void actualizarProducto() {
        String nombre = editNombre.getText().toString().trim();
        String descripcion = editDescripcion.getText().toString().trim();
        String precioStr = editPrecio.getText().toString().trim();

        if (nombre.isEmpty() || precioStr.isEmpty()) {
            Toast.makeText(this, "Nombre y Precio son obligatorios.", Toast.LENGTH_SHORT).show();
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El precio debe ser un número válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un Map con los campos a actualizar
        Map<String, Object> updates = new HashMap<>();
        updates.put("nombre", nombre);
        updates.put("precio", precio);
        updates.put("descripcion", descripcion);

        db.collection("productos").document(productoId)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Producto actualizado con éxito", Toast.LENGTH_LONG).show();
                    finish(); // Regresa a VerActivity
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al actualizar el producto", Toast.LENGTH_LONG).show();
                });
    }
}