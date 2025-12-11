package com.gonzalo.pruebafinal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class AgregarActivity extends AppCompatActivity {

    // 1. Declaración de Vistas y Firebase
    private EditText editNombre, editPrecio, editDescripcion;
    private Button btnGuardar;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Asegúrate de que 'activity_agregar.xml' exista y tenga los IDs correctos
        setContentView(R.layout.activity_agregar);

        // 2. Inicializar Vistas
        editNombre = findViewById(R.id.editNombre);
        editPrecio = findViewById(R.id.editPrecio);
        editDescripcion = findViewById(R.id.editDescripcion);
        btnGuardar = findViewById(R.id.btnGuardar);

        // 3. Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // 4. Configurar Listener del Botón Guardar
        btnGuardar.setOnClickListener(view -> {
            guardarProducto();
        });
    }

    /**
     * Requisito 3: Toma los datos del formulario y los agrega como un nuevo documento en Firestore.
     */
    private void guardarProducto() {
        // A. Obtener datos de los EditTexts
        String nombre = editNombre.getText().toString().trim();
        String descripcion = editDescripcion.getText().toString().trim();
        String precioStr = editPrecio.getText().toString().trim();

        // B. Validación básica
        if (nombre.isEmpty() || descripcion.isEmpty() || precioStr.isEmpty()) {
            Toast.makeText(this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El precio debe ser un número válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // C. Crear objeto Producto (el ID será generado por Firestore)
        Producto nuevoProducto = new Producto(nombre, precio, descripcion);

        // D. Guardar en Firestore
        db.collection("productos")
                .add(nuevoProducto) // .add() genera automáticamente un nuevo Document ID
                .addOnSuccessListener(documentReference -> {
                    // Éxito: Muestra un mensaje y cierra la Activity
                    Toast.makeText(this, "Producto agregado con éxito", Toast.LENGTH_LONG).show();
                    finish(); // Regresa a ListarActivity (que se refrescará con onResume)
                })
                .addOnFailureListener(e -> {
                    // Fallo: Muestra un mensaje de error
                    Toast.makeText(this, "Error al guardar el producto", Toast.LENGTH_LONG).show();
                });
    }
}