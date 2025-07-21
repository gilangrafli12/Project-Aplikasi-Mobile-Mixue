package com.example.mixueapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProfileEditActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var imageUri: Uri? = null
    private var currentProfileImageUrl: String? = null

    private lateinit var fullNameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var profileImageView: ImageView
    private lateinit var saveButton: Button
    private lateinit var backButton: ImageButton
    private lateinit var profileNameTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        fullNameEditText = findViewById(R.id.edit_full_name)
        ageEditText = findViewById(R.id.edit_age)
        phoneEditText = findViewById(R.id.edit_phone)
        emailEditText = findViewById(R.id.edit_email)
        addressEditText = findViewById(R.id.edit_address)
        profileImageView = findViewById(R.id.profile_image)
        saveButton = findViewById(R.id.save_button)
        backButton = findViewById(R.id.back_button)
        profileNameTextView = findViewById(R.id.profile_name)

        backButton.setOnClickListener {
            onBackPressed()
        }

        profileImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
            startActivityForResult(intent, 1)
        }

        saveButton.setOnClickListener {
            saveUserProfile()
        }

        loadUserProfile()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            profileImageView.setImageURI(imageUri)
        }
    }

    private fun loadUserProfile() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId).get()
            .addOnSuccessListener { doc ->
                fullNameEditText.setText(doc.getString("fullName"))
                profileNameTextView.text = doc.getString("fullName")
                ageEditText.setText(doc.getLong("age")?.toString())
                phoneEditText.setText(doc.getLong("phone")?.toString())
                emailEditText.setText(doc.getString("email"))
                addressEditText.setText(doc.getString("address"))

                currentProfileImageUrl = doc.getString("profileImageUrl")
                if (!currentProfileImageUrl.isNullOrEmpty()) {
                    Glide.with(this)
                        .load(currentProfileImageUrl)
                        .into(profileImageView)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal memuat data profil", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveUserProfile() {
        val userId = auth.currentUser?.uid ?: return

        val fullName = fullNameEditText.text.toString()
        val ageText = ageEditText.text.toString()
        val phoneText = phoneEditText.text.toString()
        val email = emailEditText.text.toString()
        val address = addressEditText.text.toString()

        if (ageText.isBlank() || !ageText.all { it.isDigit() }) {
            Toast.makeText(this, "Usia tidak valid", Toast.LENGTH_SHORT).show()
            return
        }

        if (phoneText.isBlank() || !phoneText.all { it.isDigit() }) {
            Toast.makeText(this, "Nomor telepon tidak valid", Toast.LENGTH_SHORT).show()
            return
        }

        val userData = hashMapOf(
            "fullName" to fullName,
            "age" to ageText.toInt(),
            "phone" to phoneText.toLong(),
            "email" to email,
            "address" to address,
            "profileImageUrl" to (currentProfileImageUrl ?: "")
        )

        db.collection("users").document(userId).set(userData)
            .addOnSuccessListener {
                if (imageUri != null) {
                    uploadProfileImage(userId)
                } else {
                    Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menyimpan profil", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadProfileImage(userId: String) {
        val ref = storage.reference.child("profileImages/$userId")
        imageUri?.let { uri ->
            ref.putFile(uri)
                .continueWithTask { task ->
                    if (!task.isSuccessful) throw task.exception!!
                    ref.downloadUrl
                }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUrl = task.result.toString()
                        db.collection("users").document(userId)
                            .update("profileImageUrl", downloadUrl)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Foto profil diperbarui", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Gagal menyimpan URL foto", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "Upload foto gagal", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
