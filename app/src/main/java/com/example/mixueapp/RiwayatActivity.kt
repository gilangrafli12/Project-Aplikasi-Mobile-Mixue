package com.example.mixueapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RiwayatActivity : AppCompatActivity() {

    private lateinit var rvRiwayat: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat)

        rvRiwayat = findViewById(R.id.rvRiwayatPembelian)

        val dummyRiwayat = listOf(
            Riwayat("10 Juli 2025", "Mixue Ice Cream", 1, 15000, R.drawable.contoh_produk),
            Riwayat("11 Juli 2025", "Mixue Boba", 2, 28000, R.drawable.contoh_produk),
            Riwayat("12 Juli 2025", "Strawberry Sundae", 1, 17000, R.drawable.contoh_produk)
        )

        rvRiwayat.layoutManager = LinearLayoutManager(this)
        rvRiwayat.adapter = RiwayatAdapterActivity(dummyRiwayat) { riwayat ->
            // TODO: Handle detail klik, misal ke DetailActivity
            // startActivity(Intent(this, DetailActivity::class.java))
        }
    }
}
