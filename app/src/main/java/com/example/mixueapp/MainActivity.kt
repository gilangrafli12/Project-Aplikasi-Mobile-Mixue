package com.example.mixueapp

import CategoryAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val productList = listOf(
            Product("Ice Cream Strawberry", "Segar dan manis", R.drawable.contoh_produk),
        Product("Ice Cream Matcha", "Rasa teh hijau khas Jepang", R.drawable.contoh_produk),
        Product("Bubble Tea", "Minuman teh dengan topping bubble", R.drawable.contoh_produk),
            Product("Ice Cream Strawberry", "Segar dan manis", R.drawable.contoh_produk),
            Product("Ice Cream Matcha", "Rasa teh hijau khas Jepang", R.drawable.contoh_produk),
            Product("Bubble Tea", "Minuman teh dengan topping bubble", R.drawable.contoh_produk),
            Product("Ice Cream Strawberry", "Segar dan manis", R.drawable.contoh_produk),
            Product("Ice Cream Matcha", "Rasa teh hijau khas Jepang", R.drawable.contoh_produk),
            Product("Bubble Tea", "Minuman teh dengan topping bubble", R.drawable.contoh_produk)
        )

        val recyclerView = findViewById<RecyclerView>(R.id.rvProducts)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = ProductAdapter(productList)

        val rvCategories = findViewById<RecyclerView>(R.id.rvCategories)
        rvCategories.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val categoryList = listOf(
            Category("Dessert"),
            Category("Boba"),
            Category("Ice Cream"),
            Category("Minuman Panas")
        )

        val adapter = CategoryAdapter(categoryList)
        rvCategories.adapter = adapter


    }
}
