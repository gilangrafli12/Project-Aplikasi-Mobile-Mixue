package com.example.mixueapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RiwayatAdapterActivity(
    private val riwayatList: List<Riwayat>,
    private val onDetailClick: (Riwayat) -> Unit
) : RecyclerView.Adapter<RiwayatAdapterActivity.RiwayatViewHolder>() {

    inner class RiwayatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val namaProduk: TextView = itemView.findViewById(R.id.nama_produk)
        val tanggalPembelian: TextView = itemView.findViewById(R.id.tanggal_pembelian)
        val hargaProduk: TextView = itemView.findViewById(R.id.harga_produk)
        val btnDetail: Button = itemView.findViewById(R.id.detail_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_riwayat, parent, false)
        return RiwayatViewHolder(view)
    }

    override fun onBindViewHolder(holder: RiwayatViewHolder, position: Int) {
        val riwayat = riwayatList[position]
        holder.imageView.setImageResource(riwayat.imageResId)
        holder.namaProduk.text = riwayat.productName
        holder.tanggalPembelian.text = "Dibeli pada: ${riwayat.date}"
        holder.hargaProduk.text = "Rp ${riwayat.totalPrice}"

        holder.btnDetail.setOnClickListener {
            onDetailClick(riwayat)
        }
    }

    override fun getItemCount(): Int = riwayatList.size
}
