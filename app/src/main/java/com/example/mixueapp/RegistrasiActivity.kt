package com.example.mixueapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegistrasiActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val fullName = findViewById<EditText>(R.id.fullName)
        val email = findViewById<EditText>(R.id.email)
        val phone = findViewById<EditText>(R.id.phone)
        val password = findViewById<EditText>(R.id.password)
        val confirmPassword = findViewById<EditText>(R.id.confirmPassword)
        val termsCheckBox = findViewById<CheckBox>(R.id.termsCheckBox)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val loginLink = findViewById<TextView>(R.id.loginLink)

        registerButton.setOnClickListener {
            val name = fullName.text.toString().trim()
            val emailText = email.text.toString().trim()
            val phoneText = phone.text.toString().trim()
            val pass = password.text.toString().trim()
            val confirmPass = confirmPassword.text.toString().trim()

            if (name.isEmpty() || emailText.isEmpty() || phoneText.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
                toast("Semua field harus diisi")
                return@setOnClickListener
            }

            if (!termsCheckBox.isChecked) {
                toast("Kamu harus setuju dengan syarat dan ketentuan")
                return@setOnClickListener
            }

            if (pass != confirmPass) {
                toast("Password dan konfirmasi tidak cocok")
                return@setOnClickListener
            }

            val phoneNumber = phoneText.toLongOrNull()
            if (phoneNumber == null) {
                toast("Nomor telepon tidak valid")
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(emailText, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                        val userData = hashMapOf(
                            "uid" to userId,
                            "fullName" to name,
                            "email" to emailText,
                            "phone" to phoneNumber
                        )
                        firestore.collection("users").document(userId).set(userData)
                            .addOnSuccessListener {
                                toast("Registrasi berhasil")
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                toast("Gagal simpan data user: ${e.message}")
                            }
                    } else {
                        toast("Registrasi gagal: ${task.exception?.message}")
                    }
                }
        }

        loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
