package com.gonzalo.pruebafinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListarActivity extends AppCompatActivity {

    private static final String TAG = "ListarActivity"; // Etiqueta para Logcat

    // 1. Declaración de Variables de Clase (Fields)
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private ProductoAdapter adapter;
    private List<Producto> listaProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Asegúrate de que este layout exista y contenga el RecyclerView con ID 'recyclerView' y el FAB con ID 'fabAdd'
        setContentView(R.layout.activity_listar);

        // 2. Inicialización de Firebase y Vistas
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);

        // 3. Inicialización de la lista y el adaptador
        listaProductos = new ArrayList<>();
        // El 'this' pasa el contexto de la Activity, necesario para el Adapter
        // NOTA: Asegúrate de que ProductoAdapter y Producto existan para evitar errores
        adapter = new ProductoAdapter(listaProductos, this);

        // 4. Configuración del RecyclerView
        // Define cómo se mostrarán los ítems (en este caso, en una lista vertical)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 5. Configuración del Botón para ir a AgregarActivity (Requisito 3)
        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> {
            // NOTA: Asegúrate de que AgregarActivity exista para evitar errores
            startActivity(new Intent(this, AgregarActivity.class));
        });

        // 6. Carga inicial de datos
        obtenerDatosFirestore();
    }

    /**
     * Requisito 2: Lee los datos de la colección "productos" en Firestore y actualiza el RecyclerView.
     */
    private void obtenerDatosFirestore() {
        Log.d(TAG, "Iniciando lectura de datos desde Firestore...");

        db.collection("productos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listaProductos.clear(); // Limpia la lista anterior

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Mapea el documento de Firestore a la clase Producto
                            Producto producto = document.toObject(Producto.class);

                            // ¡IMPORTANTE! Asigna el ID del documento al objeto Producto para futuras operaciones CRUD
                            producto.setId(document.getId());

                            listaProductos.add(producto);
                        }

                        // Notifica al adaptador que los datos han cambiado para que el RecyclerView se refresque
                        adapter.notifyDataSetChanged();
                        Log.d(TAG, "Datos cargados: " + listaProductos.size() + " productos.");

                    } else {
                        Log.w(TAG, "Error al obtener documentos.", task.getException());
                    }
                });
    }

    /**
     * Se llama cuando la Activity regresa (ej. después de guardar en AgregarActivity).
     * Esto asegura que la lista se refresque para mostrar el nuevo/editado/eliminado producto.
     */
    @Override
    protected void onResume() {
        super.onResume();
        obtenerDatosFirestore();
    }
}