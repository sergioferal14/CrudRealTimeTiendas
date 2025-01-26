package com.example.crudtienda.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.crudtienda.R
import com.example.crudtienda.models.Tienda

class TiendaAdapter (
    var lista: MutableList<Tienda>,
    private val onBorrar: (Tienda) -> Unit,
    private val onEditar: (Tienda) -> Unit
    ): RecyclerView.Adapter<TiendaViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TiendaViewHolder{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.tienda_layout, parent, false)
            return TiendaViewHolder(view)
        }

        override fun getItemCount()= lista.size

        override fun onBindViewHolder(holder: TiendaViewHolder, position: Int) {
            holder.render(lista[position], onBorrar, onEditar)
        }
}