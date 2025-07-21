package com.example.mixueapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class RiwayatAdapterActivity(
    private var riwayatList: List<Riwayat>,
    private val onDetailClick: (Riwayat) -> Unit
) : RecyclerView.Adapter<RiwayatAdapterActivity.RiwayatViewHolder>() {

    inner class RiwayatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val namaProduk: TextView = itemView.findViewById(R.id.nama_produk)
        val tanggalPembelian: TextView = itemView.findViewById(R.id.tanggal_pembelian)
        val hargaProduk: TextView = itemView.findViewById(R.id.harga_produk)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_riwayat, parent, false)
        return RiwayatViewHolder(view)
    }

    override fun onBindViewHolder(holder: RiwayatViewHolder, position: Int) {
        val r = riwayatList[position]

        Glide.with(holder.itemView.context)
            .load(r.imageUrl)
            .placeholder(R.drawable.contoh_produk)
            .into(holder.imageView)

        holder.namaProduk.text = r.namaProduk

        // Format tanggal biar jadi readable
        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        val formattedDate = r.tanggal?.toDate()?.let { formatter.format(it) } ?: "-"
        holder.tanggalPembelian.text = "Dibeli pada: $formattedDate"

        val formattedPrice = NumberFormat.getNumberInstance(Locale("in", "ID")).format(r.harga)
        holder.hargaProduk.text = "Rp $formattedPrice"


    }

    override fun getItemCount(): Int = riwayatList.size

    fun updateData(newList: List<Riwayat>) {
        riwayatList = newList
        notifyDataSetChanged()
    }
}
