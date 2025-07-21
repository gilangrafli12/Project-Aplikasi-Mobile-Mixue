package com.example.mixueapp

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.NumberFormat
import java.util.*

class DetailProdukActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private lateinit var imageView: ImageView
    private lateinit var nameView: TextView
    private lateinit var priceView: TextView
    private lateinit var descView: TextView
    private lateinit var orderButton: Button

    private var productId: String? = null
    private var namaProduk: String = ""
    private var hargaProduk: Int = 0
    private var imageUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_produk)

        // Inisialisasi komponen UI
        imageView = findViewById(R.id.product_image)
        nameView = findViewById(R.id.product_name)
        priceView = findViewById(R.id.product_price)
        descView = findViewById(R.id.product_description)
        orderButton = findViewById(R.id.order_button)

        productId = intent.getStringExtra("productId")

        if (productId.isNullOrEmpty()) {
            Toast.makeText(this, "ID produk tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Ambil data produk dari Firestore
        db.collection("produk").document(productId!!).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    namaProduk = document.getString("nama") ?: "-"
                    val deskripsi = document.getString("deskripsi") ?: "-"
                    hargaProduk = document.getLong("harga")?.toInt() ?: 0
                    imageUrl = document.getString("imageUrl") ?: ""

                    nameView.text = namaProduk
                    descView.text = deskripsi
                    priceView.text =
                        "Rp ${NumberFormat.getNumberInstance(Locale("in", "ID")).format(hargaProduk)}"

                    Glide.with(this)
                        .load(imageUrl)
                        .into(imageView)
                } else {
                    Toast.makeText(this, "Produk tidak ditemukan", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal mengambil data produk", Toast.LENGTH_SHORT).show()
                finish()
            }

        // Aksi tombol "Pesan Sekarang"
        orderButton.setOnClickListener {
            val user = auth.currentUser
            if (user == null) {
                Toast.makeText(this, "Anda belum login", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val riwayatData = hashMapOf(
                "userId" to user.uid,
                "produkId" to productId,
                "namaProduk" to namaProduk,
                "harga" to hargaProduk,
                "imageUrl" to imageUrl,
                "tanggal" to Timestamp.now()
            )

            db.collection("riwayat")
                .add(riwayatData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Pesanan berhasil dicatat", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal mencatat pesanan", Toast.LENGTH_SHORT).show()
                }
        }

        // Tombol kembali
        findViewById<ImageView>(R.id.back_button).setOnClickListener {
            finish()
        }
    }
}
