package com.example.crudtienda.models

import java.io.Serializable

data class Tienda (
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Float=0F
): Serializable