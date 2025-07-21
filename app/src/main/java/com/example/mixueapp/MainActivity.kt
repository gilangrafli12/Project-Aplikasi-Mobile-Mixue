package com.example.mixueapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mixueapp.adapter.ProdukDetailAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var productAdapter: ProdukDetailAdapter
    private lateinit var searchEditText: EditText
    private lateinit var categoryAdapter: CategoryAdapter
    private val categoryList = mutableListOf<Category>()
    private var fullProductList = mutableListOf<Product>()
    private var selectedCategory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
        val fullName = sharedPref.getString("fullName", "User")
        findViewById<TextView>(R.id.tvUserName).text = fullName

        // Produk setup
        val recyclerView = findViewById<RecyclerView>(R.id.rvProducts)
        productAdapter = ProdukDetailAdapter(mutableListOf())
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = productAdapter

        // Search bar
        searchEditText = findViewById(R.id.search_bar)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = filterProducts()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Kategori setup
        val rvCategories = findViewById<RecyclerView>(R.id.rvCategories)
        rvCategories.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Adapter awal kosong, nanti diisi saat data produk di-load
        categoryAdapter = CategoryAdapter(categoryList, selectedCategory) { clickedCategoryId ->
            selectedCategory = if (clickedCategoryId == selectedCategory) null else clickedCategoryId
            updateCategoryAdapter()
            filterProducts()
        }

        rvCategories.adapter = categoryAdapter

        // Load produk + kategori dari koleksi produk (BUKAN kategori)
        loadProduk()

        // Bottom navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.selectedItemId = R.id.navigation_home
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> true
                R.id.navigation_pengiriman -> {
                    startActivity(Intent(this, ListGeraiActivity::class.java)); true
                }
                R.id.navigation_donation -> {
                    startActivity(Intent(this, ListProdukActivity::class.java)); true
                }
                R.id.navigation_history -> {
                    startActivity(Intent(this, RiwayatActivity::class.java)); true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java)); true
                }
                else -> false
            }
        }
    }

    private fun updateCategoryAdapter() {
        categoryAdapter = CategoryAdapter(categoryList, selectedCategory) { clickedCategory ->
            selectedCategory = if (clickedCategory == selectedCategory) null else clickedCategory
            updateCategoryAdapter()
            filterProducts()
        }
        findViewById<RecyclerView>(R.id.rvCategories).adapter = categoryAdapter
    }

    private fun loadProduk() {
        val db = FirebaseFirestore.getInstance()
        fullProductList.clear()
        categoryList.clear()

        db.collection("produk")
            .get()
            .addOnSuccessListener { result ->
                val tempList = mutableListOf<Product>()
                val kategoriSet = mutableSetOf<String>()

                for (doc in result) {
                    val produk = doc.toObject(Product::class.java).copy(id = doc.id)
                    tempList.add(produk)

                    // Ambil nama kategori langsung dari field
                    val kategori = produk.kategori.trim()
                    if (kategori.isNotEmpty()) {
                        val normalized = kategori.lowercase()
                        if (kategoriSet.none { it.lowercase() == normalized }) {
                            kategoriSet.add(kategori)
                        }
                    }

                }

                // Simpan produk
                fullProductList = tempList
                filterProducts()

                // Simpan kategori unik
                kategoriSet.forEach { kategoriNama ->
                    categoryList.add(Category(kategoriNama, kategoriNama))
                }
                updateCategoryAdapter()
            }
            .addOnFailureListener { e ->
                Log.e("MainActivity", "Gagal ambil produk: ", e)
            }
    }

    private fun filterProducts() {
        val keyword = searchEditText.text.toString().trim().lowercase()
        val filtered = fullProductList.filter { produk ->
            val cocokKategori = selectedCategory == null ||
                    produk.kategori.trim().equals(selectedCategory!!.trim(), ignoreCase = true)

            val cocokKeyword = keyword.isBlank() ||
                    produk.nama.lowercase().contains(keyword) ||
                    produk.deskripsi.lowercase().contains(keyword)

            cocokKategori && cocokKeyword
        }
        productAdapter.updateData(filtered)
    }
}
