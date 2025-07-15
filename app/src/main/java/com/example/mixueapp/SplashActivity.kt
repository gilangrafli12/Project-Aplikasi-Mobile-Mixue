    package com.example.mixueapp

    import android.content.Intent
    import android.os.Bundle
    import android.os.Handler
    import android.os.Looper
    import androidx.appcompat.app.AppCompatActivity

    class SplashActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.splash_screen)

            // Menjalankan OnboardingActivity setelah 3 detik
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, OnboardingActivity::class.java)
                startActivity(intent)
                finish()
            }, 1000) // 1 detik
        }
    }