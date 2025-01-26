package com.example.crudtienda

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crudtienda.adapters.TiendaAdapter
import com.example.crudtienda.databinding.ActivityPrincipalBinding
import com.example.crudtienda.models.Tienda
import com.example.crudtienda.providers.TiendaProvider
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PrincipalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrincipalBinding
    var adapter = TiendaAdapter(mutableListOf<Tienda>(),
        { item -> borrarItem(item) },
        { item -> editarItem(item) })
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPrincipalBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance().getReference("tienda")
        setRecycler()
        setListeners()
        setMenuLateral()
    }

    private fun setMenuLateral() {
        binding.navigationview.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_logout -> {
                    auth.signOut()
                    finish()
                    true
                }

                R.id.item_salir -> {
                    finishAffinity()
                    true
                }

                R.id.item_borrar -> {
                    borrarTodo()
                    true
                }

                else -> false

            }
        }
    }

    private fun borrarTodo() {
        database.removeValue().addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(this, "Todo borrado", Toast.LENGTH_SHORT).show()
                recuperarDatosTienda()
            }else{
                Toast.makeText(this, "Error al borrar", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setRecycler() {
        val layoutManayer = LinearLayoutManager(this)
        binding.rectienda.layoutManager = layoutManayer

        binding.rectienda.adapter = adapter
        recuperarDatosTienda()
    }

    private fun recuperarDatosTienda() {
        val tiendaProvider = TiendaProvider()
        tiendaProvider.getDatos { todosLosRegistros ->
            binding.imageView.visibility =
                if (todosLosRegistros.isEmpty()) View.VISIBLE else View.INVISIBLE
            adapter.lista = todosLosRegistros
            adapter.notifyDataSetChanged()
        }
    }

    private fun setListeners() {
        binding.fabAdd.setOnClickListener {
            irActivityAdd()
        }
    }

    private fun irActivityAdd(bundle: Bundle? = null) {
        val i = Intent(this, AddActivity::class.java)
        if (bundle != null) {
            i.putExtras(bundle)
        }
        startActivity(i)
    }

    private fun borrarItem(articulo: Tienda) {
        val nombre = articulo.nombre // Usar el nombre como identificador único

        database.child(nombre).removeValue()
            .addOnSuccessListener {
                val position = adapter.lista.indexOf(articulo)
                if (position != -1) {
                    adapter.lista.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    Toast.makeText(this, "Artículo borrado", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al borrar el artículo", Toast.LENGTH_SHORT).show()
            }
    }

    private fun editarItem(item: Tienda) {
        val b = Bundle().apply {
            putSerializable("TIENDA", item)
        }
        irActivityAdd(b)
    }

    override fun onResume() {
        super.onResume()
        recuperarDatosTienda()
    }
}