package com.example.mixueapp

data class Product(
    val id: String = "",         // <-- ini penting!
    val nama: String = "",
    val deskripsi: String = "",
    val harga: Int = 0,
    val imageUrl: String = "",
    val kategori: String = ""
)
