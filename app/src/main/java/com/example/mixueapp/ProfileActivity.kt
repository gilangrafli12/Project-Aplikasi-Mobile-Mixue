package com.example.mixueapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {

    private lateinit var logoutLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Inisialisasi view untuk nama dan email
        val nameTextView = findViewById<TextView>(R.id.profile_name)
        val emailTextView = findViewById<TextView>(R.id.profile_email)

        // Ambil data dari SharedPreferences
        val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
        val name = sharedPref.getString("fullName", "Guest") ?: "Guest"
        val email = sharedPref.getString("email", "email@example.com") ?: "email@example.com"


        // Tampilkan data ke TextView
        nameTextView.text = name
        emailTextView.text = email

        // Bottom Navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.selectedItemId = R.id.navigation_profile

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.navigation_pengiriman -> {
                    startActivity(Intent(this, ListGeraiActivity::class.java))
                    true
                }
                R.id.navigation_donation -> {
                    startActivity(Intent(this, ListProdukActivity::class.java))
                    true
                }
                R.id.navigation_history -> {
                    startActivity(Intent(this, RiwayatActivity::class.java))
                    true
                }
                R.id.navigation_profile -> true
                else -> false
            }
        }

        // Edit Profile
        val editProfileLayout = findViewById<LinearLayout>(R.id.edit_profile)
        editProfileLayout.setOnClickListener {
            startActivity(Intent(this, ProfileEditActivity::class.java))
        }

// Tentang Kami
        val aboutUsLayout = findViewById<LinearLayout>(R.id.about_us)
        aboutUsLayout.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

// FAQ
        val faqLayout = findViewById<LinearLayout>(R.id.faq)
        faqLayout.setOnClickListener {
            startActivity(Intent(this, FaqActivity::class.java))
        }

        // Logout setup
        logoutLayout = findViewById(R.id.logout)
        logoutLayout.setOnClickListener {
            val editor = sharedPref.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
