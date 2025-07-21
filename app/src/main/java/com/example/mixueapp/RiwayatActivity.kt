package com.example.mixueapp

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RiwayatActivity : AppCompatActivity() {

    private lateinit var rvRiwayat: RecyclerView
    private lateinit var adapter: RiwayatAdapterActivity
    private lateinit var searchBar: EditText

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private var fullRiwayatList = mutableListOf<Riwayat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat)

        rvRiwayat = findViewById(R.id.rvRiwayatPembelian)
        searchBar = findViewById(R.id.search_bar)

        adapter = RiwayatAdapterActivity(listOf()) { riwayat ->
            // Aksi klik detail (jika perlu)
        }

        rvRiwayat.layoutManager = LinearLayoutManager(this)
        rvRiwayat.adapter = adapter

        setupBottomNav()
        setupSearch()
        loadRiwayat()
    }

    private fun setupBottomNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.selectedItemId = R.id.navigation_history
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> { /* Home sudah dijalankan */ true }
                R.id.navigation_pengiriman -> {
                    startActivity(Intent(this, ListGeraiActivity::class.java)); true
                }
                R.id.navigation_donation -> {
                    startActivity(Intent(this, ListProdukActivity::class.java)); true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java)); true
                }
                else -> false
            }
        }
    }

    private fun setupSearch() {
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterRiwayat(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun isNetworkAvailable(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo?.isConnected == true
    }

    private fun loadRiwayat() {
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show()
            return
        }

        val currentUserId = auth.currentUser?.uid ?: run {
            Toast.makeText(this, "User belum login", Toast.LENGTH_SHORT).show()
            return
        }

        firestore.collection("riwayat")
            .whereEqualTo("userId", currentUserId)
            .get()
            .addOnSuccessListener { result ->
                val list = mutableListOf<Riwayat>()
                val total = result.size()
                var processed = 0

                for (doc in result) {
                    val riwayat = doc.toObject(Riwayat::class.java)

                    firestore.collection("produk")
                        .document(riwayat.produkId)
                        .get()
                        .addOnSuccessListener { prodDoc ->
                            riwayat.namaProduk = prodDoc.getString("namaProduk") ?: riwayat.namaProduk
                            riwayat.imageUrl = prodDoc.getString("imageUrl") ?: riwayat.imageUrl
                            list.add(riwayat)
                            processed++

                            if (processed == total) {
                                fullRiwayatList = list
                                adapter.updateData(fullRiwayatList)
                            }
                        }
                        .addOnFailureListener {
                            processed++
                            if (processed == total) {
                                fullRiwayatList = list
                                adapter.updateData(fullRiwayatList)
                            }
                        }
                }

                if (result.isEmpty) {
                    adapter.updateData(emptyList())
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal ambil data riwayat", Toast.LENGTH_SHORT).show()
            }
    }

    private fun filterRiwayat(query: String) {
        val filtered = fullRiwayatList.filter {
            it.namaProduk.contains(query, ignoreCase = true)
        }
        adapter.updateData(filtered)
    }
}
