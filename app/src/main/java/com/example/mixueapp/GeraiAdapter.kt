package com.example.mixueapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mixueapp.R
import com.example.mixueapp.Gerai

class GeraiAdapter(private val geraiList: List<Gerai>) : RecyclerView.Adapter<GeraiAdapter.GeraiViewHolder>() {

    class GeraiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvGeraiName: TextView = itemView.findViewById(R.id.tvGeraiName)
        val tvGeraiLocation: TextView = itemView.findViewById(R.id.tvGeraiLocation)
        val tvGeraiJam: TextView = itemView.findViewById(R.id.tvGeraiJam)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeraiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gerai_list, parent, false)
        return GeraiViewHolder(view)
    }

    override fun onBindViewHolder(holder: GeraiViewHolder, position: Int) {
        val gerai = geraiList[position]
        holder.tvGeraiName.text = gerai.nama
        holder.tvGeraiLocation.text = gerai.lokasi
        holder.tvGeraiJam.text = "${gerai.jamBuka} - ${gerai.jamTutup}"
    }

    override fun getItemCount(): Int = geraiList.size
}
