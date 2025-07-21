package com.example.mixueapp

import com.google.firebase.Timestamp

data class Riwayat(
    val produkId: String = "",
    var namaProduk: String = "",
    val tanggal: Timestamp? = null,
    val harga: Int = 0,
    var imageUrl: String = "",
    val userId: String = ""
)
