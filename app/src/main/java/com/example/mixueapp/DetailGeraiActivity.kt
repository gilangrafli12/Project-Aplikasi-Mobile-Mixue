package com.example.mixueapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class DetailGeraiActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var lokasiGerai: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_gerai)

        // 💡 DATA DUMMY langsung hardcoded
        val nama = "Mixue Bintaro"
        val lokasi = "Jl. Raya Bintaro No.123, Tangerang Selatan"
        val jam = "10.00 - 22.00"
        val latitude = 0.5710
        val longitude = 101.4261

        lokasiGerai = LatLng(latitude, longitude)

        // Set ke UI
        findViewById<TextView>(R.id.tv_nama_gerai).text = nama
        findViewById<TextView>(R.id.tv_lokasi_gerai).text = lokasi
        findViewById<TextView>(R.id.tv_jam_operasional).text = "Buka: $jam"

        // Tombol back
        findViewById<ImageView>(R.id.back_button).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Init Map
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lokasiGerai, 15f))
        mMap.addMarker(MarkerOptions().position(lokasiGerai).title("Lokasi Gerai"))
    }
}
