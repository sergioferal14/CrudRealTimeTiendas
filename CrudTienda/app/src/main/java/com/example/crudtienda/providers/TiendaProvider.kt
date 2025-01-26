package com.example.crudtienda.providers

import com.example.crudtienda.models.Tienda
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TiendaProvider {
    private val database= FirebaseDatabase.getInstance().getReference("tienda")
    fun getDatos(datosTienda:(MutableList<Tienda>) -> Unit){
        database.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listado = mutableListOf<Tienda>()
                for(item in snapshot.children){
                    val valor = item.getValue(Tienda::class.java)
                    if (valor!=null){
                        listado.add(valor)
                    }
                }
                listado.sortBy { it.nombre }
                datosTienda(listado)

            }

            override fun onCancelled(error: DatabaseError) {
                println("Error al leer realtime: ${error.message}")
            }

        })
    }
}