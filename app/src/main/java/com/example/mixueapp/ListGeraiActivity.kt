package com.example.mixueapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mixueapp.adapter.GeraiAdapter
import com.example.mixueapp.Gerai

class ListGeraiActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GeraiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gerai_mixue)

        recyclerView = findViewById(R.id.rvGerai)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val data = generateDummyGerai()
        adapter = GeraiAdapter(data)
        recyclerView.adapter = adapter
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
