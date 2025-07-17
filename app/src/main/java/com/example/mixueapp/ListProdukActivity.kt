package com.example.mixueapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListProdukActivity : AppCompatActivity() {

    private lateinit var rvProductList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mixue_products)

        // Bind RecyclerView
        rvProductList = findViewById(R.id.rvProductList)

        // Dummy data
        val dummyProducts = listOf(
            Product("Mixue Boba", "Minuman teh dengan boba kenyal", R.drawable.contoh_produk),
            Product("Ice Cream Cone", "Es krim vanilla di cone renyah", R.drawable.contoh_produk),
            Product("Strawberry Sundae", "Sundae segar rasa stroberi", R.drawable.contoh_produk),
            Product("Mixue Boba", "Minuman teh dengan boba kenyal", R.drawable.contoh_produk),
            Product("Ice Cream Cone", "Es krim vanilla di cone renyah", R.drawable.contoh_produk),
            Product("Strawberry Sundae", "Sundae segar rasa stroberi", R.drawable.contoh_produk)
        )

        // Set RecyclerView
        rvProductList.layoutManager = LinearLayoutManager(this)
        rvProductList.adapter = ProductListAdapter(dummyProducts) { product ->
            // Aksi saat item diklik (jika perlu)
        }
    }
}
