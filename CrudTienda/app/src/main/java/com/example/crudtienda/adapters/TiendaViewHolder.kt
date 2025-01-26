package com.example.crudtienda.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.crudtienda.databinding.TiendaLayoutBinding
import com.example.crudtienda.models.Tienda

class TiendaViewHolder (v: View): RecyclerView.ViewHolder(v){
    private val binding = TiendaLayoutBinding.bind(v)
    fun render(item: Tienda, onBorrar: (Tienda) -> Unit, onEditar: (Tienda) -> Unit){
        binding.tvNombre.text = item.nombre
        binding.tvDescripcion.text = item.descripcion
        binding.tvPrecio.text = String.format("%.2f €", item.precio)
        binding.btnBorrar.setOnClickListener{
            onBorrar(item)
        }
        binding.btnEditar.setOnClickListener{
            onEditar(item)
        }
    }
}
