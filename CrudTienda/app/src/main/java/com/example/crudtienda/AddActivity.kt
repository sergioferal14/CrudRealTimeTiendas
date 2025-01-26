package com.example.crudtienda

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.crudtienda.databinding.ActivityAddBinding
import com.example.crudtienda.models.Tienda
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding

    private var nombre = ""
    private var descripcion = ""
    private var precio = 0F
    private var editando=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recogerRegistro()
        setListeners()

        if (editando) {
            binding.tvTitulo.text = "Editar Articulo"
            binding.btnAdd.text = "EDITAR ARTICULO"
        }

    }

    private fun recogerRegistro() {
        val datos = intent.extras
        if (datos != null) {
            val tienda = datos.getSerializable("TIENDA") as Tienda
            editando = true
            nombre = tienda.nombre
            descripcion = tienda.descripcion
            precio = tienda.precio

            pintarDatos()
        }
    }

    private fun pintarDatos() {
        binding.etNombre.setText(nombre)
        binding.etDescripcion.setText(descripcion)
        binding.etPrecio.setText(precio.toString())
    }

    private fun setListeners() {
        binding.btnCancelar.setOnClickListener {
            finish()
        }
        binding.btnAdd.setOnClickListener {
            addItem()
        }
    }

    private fun addItem() {
        if (!datosOK()) return

        val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("tienda")

        val nombre = binding.etNombre.text.toString().trim()
        val descripcion = binding.etDescripcion.text.toString().trim()
        val precio = binding.etPrecio.text.toString().toFloatOrNull() ?: 0F

        // Crear el objeto Tienda
        val articulo = Tienda(nombre, descripcion, precio)

        // Verificar si el artículo ya existe
        database.child(nombre).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists() && !editando) {
                    Toast.makeText(this@AddActivity, "El artículo ya existe", Toast.LENGTH_SHORT).show()
                } else {
                    // Guardar el artículo en la base de datos usando el nombre como clave
                    database.child(nombre).setValue(articulo).addOnSuccessListener {
                        Toast.makeText(this@AddActivity, "Artículo agregado con éxito", Toast.LENGTH_SHORT).show()
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this@AddActivity, "Error al insertar el artículo", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AddActivity, "Error en la operación: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun datosOK(): Boolean {
        val nombre = binding.etNombre.text.toString().trim()
        val descripcion = binding.etDescripcion.text.toString().trim()
        val precio = binding.etPrecio.text.toString().toFloatOrNull() ?: 0F

        if (nombre.length < 3) {
            binding.etNombre.error = "Error, el nombre debe tener al menos 3 caracteres"
            return false
        }

        if (descripcion.length < 10) {
            binding.etDescripcion.error = "Error, la descripción debe tener al menos 10 caracteres"
            return false
        }

        if (precio < 1 || precio > 10000) {
            binding.etPrecio.error = "Error, el precio debe estar entre 1 y 10000"
            return false
        }

        return true
    }

}