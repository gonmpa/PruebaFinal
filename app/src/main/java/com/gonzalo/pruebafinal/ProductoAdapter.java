package com.gonzalo.pruebafinal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private List<Producto> listaProductos;
    private Context context;

    public ProductoAdapter(List<Producto> listaProductos, Context context) {
        this.listaProductos = listaProductos;
        this.context = context;
    }

    // 1. Crea la vista de la fila (item_producto.xml)
    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    // 2. Asigna los datos a la vista
    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = listaProductos.get(position);

        holder.tvNombre.setText(producto.getNombre());
        holder.tvPrecio.setText(String.format("$%.2f", producto.getPrecio())); // Formato de precio
        holder.tvDescripcionCorta.setText(producto.getDescripcion());

        // Manejar el clic para ir a VerActivity (Requisito 4)
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VerActivity.class);
            intent.putExtra("PRODUCTO_ID", producto.getId()); // Pasamos el ID del producto
            context.startActivity(intent);
        });
    }

    // 3. Devuelve el número de elementos
    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    // Clase interna ViewHolder (referencia a los elementos de item_producto.xml)
    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvPrecio, tvDescripcionCorta;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvDescripcionCorta = itemView.findViewById(R.id.tvDescripcionCorta);
        }
    }
}