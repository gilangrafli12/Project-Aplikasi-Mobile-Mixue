package com.example.mixueapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mixueapp.adapter.GeraiAdapter
import com.example.mixueapp.weather.WeatherService
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListGeraiActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GeraiAdapter
    private lateinit var tvWeatherInfo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gerai_mixue)


        recyclerView = findViewById(R.id.rvGerai)
        recyclerView.layoutManager = LinearLayoutManager(this)
        tvWeatherInfo = findViewById(R.id.tvWeatherInfo)

        val data = generateDummyGerai()
        adapter = GeraiAdapter(data)
        recyclerView.adapter = adapter

        val firstGerai = data.first()
        getWeather(firstGerai.latitude, firstGerai.longitude)

        // Bottom Navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.selectedItemId = R.id.navigation_pengiriman
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> { startActivity(Intent(this, MainActivity::class.java)); true }
                R.id.navigation_donation -> {
                    startActivity(Intent(this, ListProdukActivity::class.java)); true
                }
                R.id.navigation_history -> { startActivity(Intent(this, RiwayatActivity::class.java)); true }
                R.id.navigation_profile -> { startActivity(Intent(this, ProfileActivity::class.java)); true }
                else -> false
            }
        }

    }

    private fun getWeather(lat: Double, lon: Double) {
        WeatherService.api.getCurrentWeather(lat, lon).enqueue(object : Callback<com.example.mixueapp.weather.WeatherResponse> {
            override fun onResponse(
                call: Call<com.example.mixueapp.weather.WeatherResponse>,
                response: Response<com.example.mixueapp.weather.WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    val weather = response.body()?.current_weather
                    if (weather != null) {
                        val info = """
                            Suhu: ${weather.temperature}°C
                            Kecepatan Angin: ${weather.windspeed} km/h
                            Arah Angin: ${weather.winddirection}°
                            Kode Cuaca: ${weather.weathercode}
                            Siang/Malam: ${if (weather.is_day == 1) "Siang" else "Malam"}
                            Waktu Update: ${weather.time}
                        """.trimIndent()
                        tvWeatherInfo.text = info
                    } else {
                        tvWeatherInfo.text = "Data cuaca kosong"
                    }
                } else {
                    tvWeatherInfo.text = "Gagal ambil cuaca"
                    Log.e("WeatherAPI", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<com.example.mixueapp.weather.WeatherResponse>, t: Throwable) {
                tvWeatherInfo.text = "Koneksi gagal: ${t.localizedMessage}"
                Log.e("WeatherAPI", "Failed: ${t.localizedMessage}")
            }
        })
    }

    private fun generateDummyGerai(): List<Gerai> {
        return listOf(
            Gerai("Mixue Bintaro", "Jl. Raya Bintaro No. 1", "08:00", "21:00", -6.2765, 106.7113),
            Gerai("Mixue Pondok Indah", "Jl. Pondok Indah No. 5", "09:00", "22:00", -6.2607, 106.7804),
            Gerai("Mixue Sudirman", "Jl. Jendral Sudirman No. 100", "10:00", "20:00", -6.2088, 106.8456),
            Gerai("Mixue Tebet", "Jl. Tebet Barat No. 8", "07:30", "21:30", -6.2298, 106.8553)
        )
    }
}
